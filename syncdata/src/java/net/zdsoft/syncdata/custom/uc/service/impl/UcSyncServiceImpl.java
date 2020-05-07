package net.zdsoft.syncdata.custom.uc.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.syncdata.custom.uc.constant.UcConstant;
import net.zdsoft.syncdata.custom.uc.service.UcSyncService;
import net.zdsoft.syncdata.custom.uc.util.EncryptAESUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service("ucSyncService")
public class UcSyncServiceImpl implements UcSyncService{
	private  Logger log = Logger.getLogger(UcSyncServiceImpl.class);
	
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void saveDate(String apCode, String verifyKey,
			String nonceStr) {
		log.info("------- 开始同步用户数据");
		try {
			//获取token
			ParamInfo paramInfo = new ParamInfo();
			paramInfo.setApCode(apCode);
			paramInfo.setNonceStr(nonceStr);
			paramInfo.setVerifyKey(verifyKey);
			String accessToken = getAccessToken(apCode, nonceStr, verifyKey);
			if(StringUtils.isNotBlank(accessToken)){
				paramInfo.setAccessToken(accessToken);
			}
			if(StringUtils.isBlank(accessToken)){
				log.error("token的值的获取是空----" + accessToken);
				throw new ParseException(accessToken, 0);
			}
			//获取时间戳
			String modifyTimeKey = UcConstant.UC_BEFORE_USER_REDIS_KEY + ".modifyTime." + apCode;
			String mtime = RedisUtils.get(modifyTimeKey);
			Integer isDeleted = null;
			if (StringUtils.isBlank(mtime)) {
				mtime = "19000101000000.000000";
				isDeleted =BaseSaveConstant.DEFAULT_IS_DELETED_VALUE;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSSSSS");
			Date date = sdf.parse(mtime);
			//根据时间来区分调用那个接口
			String endTime = mtime;
			for (int i = 1; i < 10000; i++) {
				Pagination page= new Pagination();
				page.setPageSize(1000);
				page.setPageIndex(i);
				List<User> userList = User.dt(userRemoteService.findByModifyTimeGreaterThan(date,isDeleted, SUtils.s(page)), page);
				if(CollectionUtils.isNotEmpty(userList)){
					System.out.println("用户总的数据是--------" + userList.size() + "-------------");
					List<User> deUserList = new ArrayList<>();
					List<User> adUserList = new ArrayList<>();
					List<User> upUserList = new ArrayList<>();
					if(CollectionUtils.isNotEmpty(userList)){
						for (User u : userList) {
							String creDate = sdf.format(u.getCreationTime());
							String modDate = sdf.format(u.getModifyTime());
							if(mtime.compareTo(creDate) < 0 && u.getIsDeleted() == BaseSaveConstant.DEFAULT_IS_DELETED_VALUE){
								adUserList.add(u);
							}else if (mtime.compareTo(modDate) < 0 && u.getIsDeleted() == BaseSaveConstant.DEFAULT_IS_DELETED_VALUE) {
								upUserList.add(u);
							}else if (u.getIsDeleted() == BaseSaveConstant.TRUE_IS_DELETED_VALUE) {
								deUserList.add(u);
							}
							if(endTime.compareTo(modDate) < 0)
								endTime = modDate;
						}
						System.out.println("添加用户总的数据是--------" + adUserList.size() + "-------------");
						System.out.println("修改用户总的数据是--------" + upUserList.size() + "-------------");
						System.out.println("删除用户总的数据是--------" + deUserList.size() + "-------------");
					}
					//调用接口来保存数据
					String code = null;
					if(CollectionUtils.isNotEmpty(adUserList))
						code = addUserList(adUserList,paramInfo);
					if(CollectionUtils.isNotEmpty(upUserList))
						code = updateUserList(upUserList,paramInfo);
					if(CollectionUtils.isNotEmpty(deUserList))
						code = deleteUserList(deUserList,paramInfo);
				}else{
					mtime = endTime;
					RedisUtils.set(modifyTimeKey, mtime);
					System.out.println("这次更新的最后时间是--------" + mtime + "-------------");
					break;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String deleteUserList(List<User> deUserList,ParamInfo paramInfo) {
		List<String> uidList = EntityUtils.getList(deUserList, User::getId);
        return doInterface(UcConstant.UC_DELETE_USER_URL,paramInfo,uidList,Boolean.FALSE,Boolean.FALSE);
	}

	private String updateUserList(List<User> upUserList,ParamInfo paramInfo) {
		return doInterface(UcConstant.UC_UPDATE_USER_URL,paramInfo,upUserList,Boolean.TRUE,Boolean.FALSE);
	}

	private String addUserList(List<User> adUserList,ParamInfo paramInfo) {
		return doInterface(UcConstant.UC_ADD_USER_URL,paramInfo,adUserList,Boolean.TRUE,Boolean.TRUE);
	}
	
	
	/**
	 * 数据的封装和加密
	 * @param url       接口地址
	 * @param paramInfo 参数对象
	 * @param dataList  数据集合
	 * @param encrypt   是否加密
	 * @param isAdd     是否新增
	 * @return
	 */
    private String doInterface(String url, ParamInfo paramInfo, List<?> dataList, boolean encrypt, boolean isAdd) {
    	 return doInterface(url,paramInfo.getApCode(),paramInfo.getAccessToken(),
    			 encrypt? getEncryptJson(dataList,paramInfo.getVerifyKey(),isAdd)  : getJSONArrayByList(dataList,isAdd).toJSONString());
	}
	
	private String doInterface(String url, String apCode, String accessToken, String data){
		url = url + "?apCode=" + apCode + "&accessToken=" +  accessToken;
		String backCode = null;
		if(StringUtils.isNotBlank(data)){
			try {
				String jsonStr = doPost(url,data);
				JSONObject json = Json.parseObject(jsonStr);
				backCode = json.getString("code");
			} catch (Exception e) {
				log.error("------- 调用接口失败-------"+e.getMessage());
				backCode = UcConstant.UC_FALSE_CODE_VALUE;
			}
		}
		return backCode;
	}

	private String getEncryptJson(List<?> dataList,String verifyKey, boolean isAdd) {
		String data = encryptData(dataList,verifyKey,isAdd);
		JSONObject jsonObject =  new JSONObject();
		jsonObject.put(UcConstant.RESOLVE_DATA_NAME, data);
		return jsonObject.toJSONString();
	}
	
	//数据进行加密
	private String encryptData(List<?> deList, String verifyKey, boolean isAdd){
		JSONArray jsonArray = getJSONArrayByList(deList,isAdd);
		String data = jsonArray.toJSONString();
		String encrypt = null;
		if(StringUtils.isNotBlank(data)){
			try {
				encrypt = EncryptAESUtil.aesEncryptBase64(data, verifyKey);
			} catch (Exception e) {
				log.error("------- 加密数据失败-------"+e.getMessage());
				return null;
			}
		}
		return encrypt;
	}
	
	 /**
     * 根据List获取到对应的JSONArray
     * @param list
     * @return
     */
    private JSONArray getJSONArrayByList(List<?> list, boolean isAdd){
        JSONArray jsonArray = new JSONArray();
        if (list==null ||list.isEmpty()) {
            return jsonArray;
        }
        for (Object object : list) {
        	JSONObject jsonObject = new JSONObject();
        	if(object instanceof User){
        		jsonObject.put("userId", ((User) object).getId());
        		jsonObject.put("jsonUserData", object);
        		if(isAdd){
        			jsonObject.put("password", ((User) object).getPassword());
        		}
        	}
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
	
	private  String getSign(String apCode, String nonceStr, long timeStamp, String verifyKey){
		timeStamp = System.currentTimeMillis();
		String plain = "apCode=" + apCode + "&nonceStr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
					+ "&verifyKey=" + verifyKey;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return byteToHex(sha1.digest());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.error("------- 计算sign失败");
			return null;
		}
	}
	 
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	public  String getAccessToken(String apCode, String nonceStr, String verifyKey ) {
		String accessTokenKey = UcConstant.UC_BEFORE_TOKEN_REDIS_KEY + apCode ;
		String accessToken = RedisUtils.get(accessTokenKey);
		if (StringUtils.isBlank(accessToken)) {
			try {
				long timeStamp = System.currentTimeMillis();
				String sign = getSign(apCode, nonceStr, timeStamp, verifyKey);
				System.out.println(sign + "-----------" + timeStamp);
				if(StringUtils.isNotBlank(sign)){
					String url = UcConstant.UC_GET_TOKEN_URL + "?apCode=" + apCode + "&nonceStr=" + nonceStr
							+ "&sign=" + sign + "&timestamp=" + timeStamp;
					System.out.println(url + "----------------------------");
//					 HttpHeaders headers = new HttpHeaders();
//					 headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//					 HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
//					 String jsonStr1  =  restTemplate.getForObject(url, String.class);
					String jsonStr  = UrlUtils.get(url, StringUtils.EMPTY);
					JSONObject json = Json.parseObject(jsonStr);
					if(UcConstant.UC_TRUE_CODE_VALUE.equals(json.getString("code"))){
						accessToken = URLEncoder.encode(json.getString("accessToken"), "utf-8");
					}
				}
			} catch (IOException e) {
//				log.error("------- 获取accessToken失败");
				accessToken = null;
			}
			//提前一分钟过期
			if(StringUtils.isNotBlank(accessToken))
			    RedisUtils.set(accessTokenKey, accessToken, (int)TimeUnit.MINUTES.toSeconds(119));
		}
		return accessToken;
	}
	
	class ParamInfo{
		private String apCode;
		private String nonceStr;
		private String verifyKey;
		private String accessToken;
		
		public String getApCode() {
			return apCode;
		}
		public void setApCode(String apCode) {
			this.apCode = apCode;
		}
		public String getNonceStr() {
			return nonceStr;
		}
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		public String getVerifyKey() {
			return verifyKey;
		}
		public void setVerifyKey(String verifyKey) {
			this.verifyKey = verifyKey;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
	}
	
	private String doPost(String url,String data) throws IOException {
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		 HttpEntity<String> requestEntity = new HttpEntity<String>(data,headers);
		 return restTemplate.postForObject(url, requestEntity, String.class);
	}
	
	public static void main(String[] args) {
		String  ssString  = "ss,ss1,ss2";
		String[]  ssStrings =ssString.split(",");
		System.out.println(ssStrings.toString());
	}
}
