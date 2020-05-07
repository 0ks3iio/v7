package net.zdsoft.framework.config;

import net.zdsoft.basedata.constant.SpagoBIConstants;
import net.zdsoft.framework.utils.EncryptAES;
import net.zdsoft.framework.utils.UuidUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 获取 spagobi接口url
 * @author 
 */
public class SpagoBIRemote {
	/* 约定 */
	static String encodingAesKey = "a59e741709e34e4a9b7767218d5cd95f";
	static String appid = "3f6648f5c35145ccaa3870374baf6320";
	static String token = "eisv7";
	
	/**
	 * 数据集接口(get/post) 接口有效timestamp开始2小时内 kettle接口(get/post) 接口有效timestamp开始2分钟内
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String biUrl = "http://localhost:8080";
		// 数据集接口(get/post) 接口有效timestamp开始2小时内
		System.out.println("dashboard学生性别分组");
		System.out.println(getBIDataUrl(biUrl,SpagoBIConstants.DATA_STUDENT_SEX_COUNT,null));

		System.out.println("dashboard学生总数");
		System.out.println(getBIDataUrl(biUrl,SpagoBIConstants.DATA_STUDENT_COUNT,null));

		JSONArray paramArray = new JSONArray();

		JSONObject param1 = new JSONObject();
		param1.put("name", "exam_id");
		param1.put("value", "14884212345021076396127000019611");
		param1.put("type", "String"); // Number
		paramArray.add(param1);

		JSONObject param2 = new JSONObject();
		param2.put("name", "grade_id");
		param2.put("value", "14781382457241076396216000012501");
		param2.put("type", "String"); // Number
		paramArray.add(param2);

		JSONObject param3 = new JSONObject();
		param3.put("name", "class_id");
		param3.put("value", "402880965A44E12C015A44E2AF830001");
		param3.put("type", "String"); // Number
		paramArray.add(param3);
		System.out.println("7选3");
		System.out.println(getBIDataUrl(biUrl,SpagoBIConstants.DATA_SCORE_7CHOOSE3,paramArray));

		// kettle接口(get/post) 接口有效timestamp开始2分钟内
		System.out.println("启动kettle");
		System.out.println(getControlKettleUrl(UuidUtils.generateUuid(),biUrl,SpagoBIConstants.JOB_SCOREDATA,SpagoBIConstants.OPERATE_START,null));
		
	}

	/**
	 * 获取数据集url
	 * 
	 * @param biUrl 
	 * @param label 从SpagoBIConstants常量里获取
	 * @param paramArray 传递的参数，可为null
	 * @return
	 * @throws Exception
	 */
	public static String getBIDataUrl(String biUrl,String label,JSONArray paramArray) throws Exception{
		String timestamp = System.currentTimeMillis() / 1000 + "";
		String randomStr = EncryptAES.getRandomStr();// 16位

		JSONObject json = new JSONObject();
		json.put("randomStr", randomStr);
		json.put("timestamp", timestamp);
		json.put("appid", appid);
		if(paramArray != null){
			json.put("params", paramArray);
		}
		String content = json.toJSONString();
		String encrypt = EncryptAES.aesEncryptBase64(content, encodingAesKey);

		String sha1 = EncryptAES.getSHA1(token, timestamp, randomStr, encrypt);

		String url = biUrl+"/biap/dataset/" + label + "?param=" + encrypt + "&sign=" + sha1;
		return url;
	}
	
	/**
	 * 获取操作kettle的url
	 * 
	 * @param unique 32位id区分
	 * @param biUrl 
	 * @param jobName 从SpagoBIConstants常量里获取
	 * @param operate 从SpagoBIConstants常量里获取
	 * @param paramArray 预留
	 * @return
	 * @throws Exception
	 */
	public static String getControlKettleUrl(String unique,String biUrl,String jobName,String operate,JSONArray paramArray) throws Exception {
		String timestamp = System.currentTimeMillis() / 1000 + "";
		String randomStr = EncryptAES.getRandomStr();// 16位

		JSONObject json = new JSONObject();
		json.put("execId", unique);//执行业务id32位
		json.put("randomStr", randomStr);
		json.put("timestamp", timestamp);
		json.put("appid", appid);

		json.put("operate", operate);

		String content = json.toJSONString();
		String encrypt = EncryptAES.aesEncryptBase64(content, encodingAesKey);

		String sha1 = EncryptAES.getSHA1(token, timestamp, randomStr, encrypt);

		String url = biUrl+"/biap/kettle/job/" + jobName + "?param=" + encrypt + "&sign=" + sha1;
		return url;
	}
	
	/**
	 * 获取kettle执行状态的url
	 * @param unique 32位id区分
	 * @param biUrl 
	 * @param jobName 从SpagoBIConstants常量里获取
	 * @return
	 * @throws Exception
	 */
	public static String queryControlKettleStatus(String unique,String biUrl,String jobName) throws Exception {
		String timestamp = System.currentTimeMillis() / 1000 + "";
		String randomStr = EncryptAES.getRandomStr();// 16位

		JSONObject json = new JSONObject();
		json.put("execId", unique);//执行业务id32位
		json.put("randomStr", randomStr);
		json.put("timestamp", timestamp);
		json.put("appid", appid);

		json.put("operate", SpagoBIConstants.OPERATE_QUERY);

		String content = json.toJSONString();
		String encrypt = EncryptAES.aesEncryptBase64(content, encodingAesKey);

		String sha1 = EncryptAES.getSHA1(token, timestamp, randomStr, encrypt);

		String url = biUrl+"/biap/kettle/job/" + jobName + "?param=" + encrypt + "&sign=" + sha1;
		return url;
	}
	
	/**
	 * 获取查看图表的url
	 * @param biUrl
	 * @param label
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public static String findDocumentUrl(String biUrl,String label,String parameters) throws Exception {
		String timestamp = System.currentTimeMillis() / 1000 + "";
		String randomStr = EncryptAES.getRandomStr();// 16位

		JSONObject json = new JSONObject();
		json.put("randomStr", randomStr);
		json.put("timestamp", timestamp);
		json.put("appid", appid);

		json.put("parameters", parameters);
		json.put("displayToolbar", true);
		

		String content = json.toJSONString();
		String encrypt = EncryptAES.aesEncryptBase64(content, encodingAesKey);

		String sha1 = EncryptAES.getSHA1(token, timestamp, randomStr, encrypt);

		String url = biUrl +"/biap/document/" + label + "?param=" + encrypt + "&sign=" + sha1;
		return url;
	}

}
