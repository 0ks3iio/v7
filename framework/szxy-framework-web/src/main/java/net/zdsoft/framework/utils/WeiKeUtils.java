package net.zdsoft.framework.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class WeiKeUtils {
	
//	 private static Logger log = Logger.getLogger(getClass());
	public static final String msgType = "7";//消息类型，HTML类型：7，图文类型：3
	public static final String isNeedAppendToken = "1";//判断是否要在推送内容的链接中带上
	public static final String singlePush =  "/api/personalMsgPush.json";
	public static final String batchPush ="/api/personalBatchMsgPush.json";
	/**
	 * 初始化构造方法 
	 * userids.length ==1 contents.length==1 推送为单个用户单条内容
	 * userids.length ==1 contents.length>1 推送为单个用户多条内容
	 * userids.length >1 contents.length==1 推送为多个用户相同内容
	 * userids.length >1 contents.length>1 推送为多个用户多条内容
	 * @param param 可以传参数过来
	 * @param userIds
	 * @param contents
	 */
	public static void push(final Map<String, String> param, final String[] userIds, final String[] contents){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String weikeUrl = Evn.getString(Constant.WEIKE_URL);
				String weikeAppId = Evn.getString(Constant.WEIKE_APP_ID);
				String weikePublicId = Evn.getString(Constant.WEIKE_PUBLIC_ID);
//				if(StringUtils.isBlank(weikeUrl)||StringUtils.isBlank(weikeAppId)||StringUtils.isBlank(weikePublicId)){
//					log.info("推送失败,微课推送参数设置异常");
//				}
//				if(param==null){
//					
//				}
				Map<String, String> pushparam = new HashMap<String, String>();
				//weikeUrl = "http://192.168.22.65:8080";
				if(StringUtils.isBlank(param.get("appId"))){
					pushparam.put("appId", weikeAppId);
				}else{
					pushparam.put("appId", param.get("appId"));
				}
				
				if(StringUtils.isBlank(param.get("publicId"))){
					pushparam.put("publicId",weikePublicId);
				}else{
					pushparam.put("publicId",param.get("publicId"));
				}
				//if(StringUtils.isNotBlank(param.get("auth"))){
				pushparam.put("auth",getVerifyKey(param.get("auth")));
				//}else{
				//	getVerifyKey(param.get("auth"));
				//}
				if(StringUtils.isBlank(param.get("isNeedAppendToken"))){
					pushparam.put("isNeedAppendToken",isNeedAppendToken);
				}else{
					pushparam.put("isNeedAppendToken",param.get("isNeedAppendToken"));
				}
				try {
					if(userIds.length ==1 && contents.length==1){
						if(StringUtils.isBlank(param.get("msgType"))){
							pushparam.put("msgType",msgType);
						}else{
							pushparam.put("msgType",param.get("msgType"));
						}
						pushparam.put("tokens","['"+userIds[0]+"']");
						pushparam.put("content",URLEncoder.encode(contents[0],"UTF-8"));
						weikeUrl = weikeUrl+singlePush;
					}else if(userIds.length ==1 && contents.length>1){
//						StringUtils.join(arg0, arg1, arg2, arg3)
						if(StringUtils.isBlank(param.get("msgType"))){
							pushparam.put("msgType",msgType);
						}else{
							pushparam.put("msgType",param.get("msgType"));
						}
						List<String> tokenString = new ArrayList<String>();
						for (String content : contents) {
							content = URLEncoder.encode(content,"UTF-8");
							tokenString.add("{'token':'"+userIds[0]+"','content':'"+content+"'}");
						}
						pushparam.put("msgObj","['"+StringUtils.join(tokenString, ",")+"']");
						weikeUrl = weikeUrl+batchPush;
					}else if(userIds.length >1 && contents.length==1){
						if(StringUtils.isBlank(param.get("msgType"))){
							pushparam.put("msgType",msgType);
						}else{
							pushparam.put("msgType",param.get("msgType"));
						}
						pushparam.put("tokens","['"+StringUtils.join(userIds, "','")+"']");
						pushparam.put("content",URLEncoder.encode(contents[0],"UTF-8"));
						weikeUrl = weikeUrl+singlePush;
					}else if(userIds.length >1 && contents.length>1 && userIds.length == contents.length){
						if(StringUtils.isBlank(param.get("msgType"))){
							pushparam.put("msgType",msgType);
						}else{
							pushparam.put("msgType",param.get("msgType"));
						}
						List<String> tokenString = new ArrayList<String>();
						for (int i = 0; i < contents.length; i++) {
							String content = URLEncoder.encode(contents[i],"UTF-8");
							tokenString.add("{'token':'"+userIds[i]+"','content':'"+content+"'}");
						}
						pushparam.put("msgObj","["+StringUtils.join(tokenString, ",")+"]");
						weikeUrl = weikeUrl+batchPush;
					}else{
						
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				try {
					//System.out.println(weikeUrl);
					//for (String string : pushparam.keySet()) {
					//	System.out.println(pushparam.get(string));
					//}
					UrlUtils.readContent(weikeUrl, pushparam, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
    public static String getVerifyKey(String verifyKey) {
        // code = 约定字符串+时间戳
        // 密文 = MD5（微课约定字符串 + 时间戳） + 时间戳
        String updatestamp = String.valueOf(System.currentTimeMillis());
//       System.out.println(updatestamp);
//        updatestamp ="1470996129069";
        if(StringUtils.isBlank(verifyKey)){
        	verifyKey = Evn.getString(Constant.WEIKE_VERIFY_CODE);
        }
        String newVerifyKey = DigestUtils.md5Hex(verifyKey+updatestamp)+updatestamp;
        return newVerifyKey;
    }
    
    
    public static void main(String[] args) {
//		String[] x = {"1111","22222","33333","44444"};
//		String y = "00";
//		List<String> tokenString = new ArrayList<String>();
//		for (String content : x) {
//			try {
//				content = URLEncoder.encode(content,"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			tokenString.add("{token:"+y+",content:"+content+"}");
//		}
//		System.out.println(RandomStringUtils.randomAlphabetic(6));
//		System.out.println("["+StringUtils.join(tokenString, ",")+"]");
    	Map<String,String>param = new HashMap<String, String>();
    	param.put("appId", "402896C156790D3301567D90F0590060");
    	param.put("publicId", "402896C156790D3301567D90F0590061");
    	param.put("auth",getVerifyKey("weike_dglx"));
    	System.out.println(getVerifyKey("weike_dglx"));
    	push(param, new String[]{"2YPH+ZMVVfWbA2h1uuhToRexEtutHaxivSmn3Jq3paEtGcSzSu6TSQ=="}, new String[]{"您的孩子 *** 已从****学校离校，离校原因：转学（系统读取离校原因），离校验证码：******，请您知悉并妥善保管验证码。调入学校将根据您提供的验证码对您孩子的信息进行调入操作。如有疑问，请联系原班主任。"});
	}
}
