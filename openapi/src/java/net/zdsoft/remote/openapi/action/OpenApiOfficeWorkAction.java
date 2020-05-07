package net.zdsoft.remote.openapi.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.officework.remote.service.OfficeHealthDoinoutInfoRemoteService;

@Controller
@RequestMapping(value = { "/remote/openapi/office/h3chealth" })
public class OpenApiOfficeWorkAction extends BaseAction{
	
	/**aes约定加密key*/
//	public static final String OFFICE_HEALTH_H3C_ENCRYPTKEY = "roS4PbqzOCYrxai3";
	
	@Autowired
	@Lazy
	private OfficeHealthDoinoutInfoRemoteService officeHealthDoinoutInfoRemoteService;
	
	/**
	 * 进出校
	 * 不加密
	 */
	@ResponseBody
	@RequestMapping(value="/doInOut", consumes="application/json", method=RequestMethod.POST)
	public String doInOut(@RequestBody JSONObject data){
		try {
			if(data!=null){
				String jsonStr = officeHealthDoinoutInfoRemoteService.doInOut(data.toJSONString());
				JSONObject retJson = (JSONObject) JSONObject.parse(jsonStr);
				int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
				String message = retJson.containsKey("message")?retJson.getString("message"):"";
				
				if(code == 1){
					return successResultJson(message);
				}else{
					return errorMsg(message);
				}
			}else{
				return errorMsg("没有推送任何数据。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return errorMsg("程序异常："+e.getMessage());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}
	}
	/**
	 * 天波进出校
	 * 不加密
	 */
	@ResponseBody
	@RequestMapping(value="/doTBInOut", consumes="application/json", method=RequestMethod.POST)
	public String doTBInOut(@RequestBody JSONArray data){
		
		return officeHealthDoinoutInfoRemoteService.doTBInOut(data.toJSONString());
	}
	
	/**
	 * 进出校
	 * aes加密
	 */
//	@ResponseBody
//	@RequestMapping(value="/doInOut", consumes="text/plain", method=RequestMethod.POST)
//	public String doInOut(@RequestBody String dataStr){
//		try {
//			if(StringUtils.isNotBlank(dataStr)){
//				String aesStr = EncryptAES.aesDecrypt(dataStr, OfficeConstants.OFFICE_HEALTH_H3C_ENCRYPTKEY);
//				String jsonStr = officeHealthDoinoutInfoRemoteService.doInOut(aesStr);
//				JSONObject retJson = (JSONObject) JSONObject.parse(jsonStr);
//				int code = retJson.containsKey("code")?retJson.getIntValue("code"):-1;
//				String message = retJson.containsKey("message")?retJson.getString("message"):"";
//				
//				if(code == 1){
//					return encodeSuccessResultJson(message);
//				}else{
//					return encodeErrorMsg(message);
//				}
//			}else{
//				return encodeErrorMsg("没有推送任何数据。");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			try {
//				return encodeErrorMsg("程序异常："+e.getMessage());
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				return null;
//			}
//		}
//	}
	
	private String errorMsg(String msg) throws Exception{
		JSONObject json = new JSONObject();
        json.put(RemoteCallUtils.JSON_RESULT_STATUS, -1);
        json.put(RemoteCallUtils.JSON_RESULT_STR, msg);
        json.put(RemoteCallUtils.JSON_RESULT_JSON, new JSONObject());
        
		return SUtils.s(json);
	}

	private String successResultJson(String msg) throws Exception {
		JSONObject jsonj = new JSONObject();
		jsonj.put(RemoteCallUtils.JSON_RESULT_JSON, new JSONObject());
		jsonj.put(RemoteCallUtils.JSON_RESULT_STATUS, 1);
		jsonj.put(RemoteCallUtils.JSON_RESULT_STR, msg);
		return SUtils.s(jsonj);
	}
	
//	private String encodeErrorMsg(String msg) throws Exception{
//		JSONObject json = new JSONObject();
//        json.put(RemoteCallUtils.JSON_RESULT_STATUS, -1);
//        json.put(RemoteCallUtils.JSON_RESULT_STR, msg);
//        json.put(RemoteCallUtils.JSON_RESULT_JSON, new JSONObject());
//        
//		return EncryptAES.aesEncrypt(SUtils.s(json), OFFICE_HEALTH_H3C_ENCRYPTKEY);
//	}
//
//	private String encodeSuccessResultJson(String msg) throws Exception {
//		JSONObject jsonj = new JSONObject();
//		jsonj.put(RemoteCallUtils.JSON_RESULT_JSON, new JSONObject());
//		jsonj.put(RemoteCallUtils.JSON_RESULT_STATUS, 1);
//		jsonj.put(RemoteCallUtils.JSON_RESULT_STR, msg);
//		return EncryptAES.aesEncrypt(SUtils.s(jsonj), OFFICE_HEALTH_H3C_ENCRYPTKEY);
//	}
	
	
//	public static void main(String[] args) {
//		CloseableHttpResponse response = null;
//		BufferedReader in = null;
//		try {
//			JSONArray stuArray = new JSONArray(); 
//			JSONObject stu1 = new JSONObject();
//			stu1.put("studentId", "9990000003");
//			stu1.put("wristbandId", "ZY994531");
//			stu1.put("inOut", 0);
//			JSONObject stu2 = new JSONObject();
//			stu2.put("studentId", "9990000004");
//			stu2.put("wristbandId", "ZY318258");
//			stu2.put("inOut", 0);
//			
//			stuArray.add(stu1);
//			stuArray.add(stu2);
//			
//			JSONObject json = new JSONObject();
//			json.put("devSn", "210235A1JMC16B000100");
//			json.put("studentGateInfo", stuArray);
//			
//			CloseableHttpClient client = HttpClients.createDefault();
//			
//			//post请求
//			HttpPost postRequst = new HttpPost("http://192.168.0.123:8090/remote/openapi/office/h3chealth/doInOut");
//			
//			StringEntity sEntity = new StringEntity(EncryptAES.aesEncrypt(json.toString(), "roS4PbqzOCYrxai3"),"utf-8");
//			sEntity.setContentType("text/plain");
//			postRequst.setEntity(sEntity);
//			
//			response = client.execute(postRequst);
//			
//			System.out.println(response.getStatusLine());
//			StringBuilder result = new StringBuilder();
//			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//				HttpEntity entity = response.getEntity();
//				if(entity!=null){
//					in = new BufferedReader(new InputStreamReader(entity.getContent()));
//					String line = null;
//                 	while((line = in.readLine())!=null){
//                 		System.out.println(result.toString());
//                 		result.append(line);
//                 	}
//				}
//			}
//			
//			System.out.println(EncryptAES.aesDecrypt(result.toString(), "roS4PbqzOCYrxai3"));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}
	
}
