package net.zdsoft.system.service.sms;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.service.config.SystemIniService;
import net.zdsoft.system.service.sms.entity.Account;
import net.zdsoft.system.service.sms.entity.JSONConverter;
import net.zdsoft.system.service.sms.entity.JSONUtils;
import net.zdsoft.system.service.sms.entity.ReceiveResult;
import net.zdsoft.system.service.sms.entity.Receiver;
import net.zdsoft.system.service.sms.entity.SendResult;
import net.zdsoft.system.service.sms.entity.ZDConstant;
import net.zdsoft.system.service.sms.entity.ZDPack;

public class ZDResponse implements Serializable {
	private static final long serialVersionUID = 4957013906874406792L;

	private static SystemIniService systemIniService;

	/**
	 * 发送消息
	 * 
	 * @param account
	 *            帐号信息
	 * @param zdPack
	 *            短信包
	 * @return
	 * @throws IOException
	 */
	public static SendResult post(Account account, ZDPack zdPack) throws IOException {
		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isBlank(account.getSmsServerUrl())) {
			try {
				account.setSmsServerUrl(RedisUtils.get("EIS.BASE.PATH.V6"));
			} catch (Exception e) {

			}
		}

		if (StringUtils.isBlank(account.getSmsServerUrl())) {
			account.setSmsServerUrl(getSystemIniService().findValue("SMS.SERVER.URL"));
		}

		map.put(ZDConstant.SMS_RECEIVE_PARAMETER_NAME, JSONConverter.transferToJSON(account, zdPack));
		String s = ZDUtils.readContent(account.getSmsServerUrl() + ZDConstant.SMS_SERVER_URL_POSTFIX,
				ZDConstant.METHOD_POST, map, null, null);

		JSONObject json = JSONObject.parseObject(s);
		SendResult sendResult = new SendResult();
		sendResult.setCode(JSONUtils.get(json, ZDConstant.JSON_RESULT_CODE));
		sendResult.setDescription(JSONUtils.get(json, ZDConstant.JSON_RESULT_DESCRIPTION));
		if (json.containsKey(ZDConstant.JSON_RESULT_BATCHID))
			sendResult.setBatchId(JSONUtils.get(json, ZDConstant.JSON_RESULT_BATCHID));
		return sendResult;
	}

	public static SendResult post(JSONObject json) {
		String smsServerUrl = null, ticket = null;
		if (!json.containsKey("smsServerUrl")) {
			smsServerUrl = getSystemIniService().findValue("SMS.SERVER.URL");
			json.put("smsServerUrl", smsServerUrl);
		} else {
			smsServerUrl = json.getString("smsServerUrl");
		}
		System.out.println("smsServerUrl-----"+smsServerUrl);
		if (StringUtils.isBlank(smsServerUrl)) {
			SendResult sendResult = new SendResult();
			sendResult.setCode(ZDConstant.JSON_RESULT_CODE_07);
			sendResult.setDescription(ZDConstant.resultDescriptionMap.get(sendResult.getCode()));
			return sendResult;
		}
		if (!json.containsKey("ticket")) {
			ticket = getSystemIniService().findValue("SMS.TICKET");
			json.put("ticket", ticket);
		} else {
			ticket = json.getString("ticket");
		}
		System.out.println("ticket-----"+ticket);
		if (StringUtils.isBlank(ticket)) {
			SendResult sendResult = new SendResult();
			sendResult.setCode(ZDConstant.JSON_RESULT_CODE_02);
			sendResult.setDescription(ZDConstant.resultDescriptionMap.get(sendResult.getCode()));
			return sendResult;
		}
		System.out.println(smsServerUrl + "/smsplatform/smsclient/sentSmsWithURL.action");
		String s="";
		try {
			Map<String,String> paramMap = new HashMap<String, String>();
			for (String key : json.keySet()) {
				paramMap.put(key, json.getString(key));
			}
			
			s = UrlUtils.readContent(smsServerUrl + "/smsplatform/smsclient/sentSmsWithURL.action", paramMap, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SendResult sendResult = new SendResult();
		if (StringUtils.startsWith(s, "{")) {
			JSONObject resultJson = JSONObject.parseObject(s);
			System.out.println(s.toString());
			if (resultJson.containsKey("code")) {
//				JSONObject resultJson2 = resultJson.getJSONObject("code");
//				sendResult.setCode(resultJson2.getString("code"));
//				sendResult.setDescription(resultJson2.getString("description"));
				sendResult.setCode(resultJson.getString("code"));
				sendResult.setDescription(resultJson.getString("description"));
				return sendResult;
			}
		}
		sendResult.setCode(ZDConstant.JSON_RESULT_CODE_99);
		sendResult.setDescription(ZDConstant.resultDescriptionMap.get(sendResult.getCode()));
		return sendResult;
	}

	/**
	 * 获取发送结果
	 * 
	 * @param smsServerUrl
	 *            短信服务器地址（只需要到contextPath）
	 * @param batchId
	 *            短信ID
	 * @return
	 * @throws IOException
	 */
	public static ReceiveResult getResult(String smsServerUrl, String batchId) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put(ZDConstant.SMS_RECEIVE_RESULT_PARAMETER_NAME, batchId);
		String s = ZDUtils.readContent(smsServerUrl + ZDConstant.SMS_RECEIVE_RESULT_POSTFIX, ZDConstant.METHOD_POST,
				map, null, null);
		if (StringUtils.isBlank(s)) {
			return new ReceiveResult();
		}
		JSONObject json = JSONObject.parseObject(s);
		ReceiveResult rr = new ReceiveResult();
		rr.setBatchCode(JSONUtils.get(json, ZDConstant.JSON_RESULT_CODE));
		rr.setBatchDescription(JSONUtils.get(json, ZDConstant.JSON_RESULT_DESCRIPTION));
		JSONObject jo = json.getJSONObject(ZDConstant.JSON_RESULT_RECEIVESTATE);
		for (Object key : jo.keySet()) {
			rr.putState(key.toString(), jo.getInteger(key.toString()));
		}
		return rr;
	}

	public static void main(String[] args) {

		JSONObject json = new JSONObject();
		// json.put("smsServerUrl", "http://192.168.0.15:8088/eisu");
		// json.put("ticket", "1E5B96AED4D55FBCE050A8C09B004F13");
		JSONArray receivers = new JSONArray();
		JSONObject receiverJ = new JSONObject();
		receiverJ.put("phone", "13857124050");
		json.put("msg", "测试短信");
		// 下面可以为空
		json.put("sendTime", "20150907111111");
		receiverJ.put("unitName", "浙大万朋");
		receiverJ.put("unitId", "浙大万朋ID");
		receiverJ.put("username", "linqz");
		receiverJ.put("userId", "linqzID");
		receivers.add(receiverJ);
		json.put("receivers", receivers);

		SendResult sr2 = post(json);

		int i = 0;
		if (i == 0) {
			return;
		}

		// --------------------------------获取短信发送结果
		try {
			// 1.获取发送结果，两个参数，第一个是短信平台服务器地址，只需要到contextPath就行，第二个参数是消息的batchId。
			// 返回这一批的
			ReceiveResult rr = ZDResponse.getResult("http://192.168.0.15:8088", "0A2E7785A77D43E39D7A4A72F467F060");
			for (String phone : rr.getPhones()) {
				System.out.println("phone: " + phone + ", state: " + rr.getState(phone));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// --------------------------------获取短信发送结果---------END

		// --------------------------------发送短信
		// 1.发送消息
		// 创建帐号信息
		Account account = new Account();
		// 短信服务器地址，只要主域名+contextPath就可以，后面具体目录不需要
		account.setSmsServerUrl("http://192.168.0.15:8088");
		// ticket是短信服务器生成，用之前，需要申请
		account.setTicket("1E5B96AED4D55FBCE050A8C09B004F13");

		// 2.组建短信包
		ZDPack zdPack = new ZDPack();
		// 接收人信息
		Receiver receiver = new Receiver();
		// 手机号码，不能为空
		receiver.setPhone("13857124050");
		// 接收人所在单位名称，可以为空
		receiver.setUnitName("浙大万朋");
		// 接收人单位ID，可以为空
		receiver.setUnitId("XXXXXX");
		// 接收人名字，可以为空
		receiver.setUsername("林清振");
		// 接收人ID，可以为空
		receiver.setUserId("XXXXXX");
		// 加入列表
		zdPack.addReciever(receiver);
		// 第二个接收人
		receiver = new Receiver();
		receiver.setPhone("13957124050");
		receiver.setUnitName("浙大网络");
		receiver.setUsername("林清振");
		zdPack.addReciever(receiver);

		// 3.短信信息
		// 短信内容，不能为空
		zdPack.setMsg("今天天气不错，没有雨，风也不大，重要的是，路上不堵，大家可以放心出游！！");
		// 定时发送时间，格式为yyyyMMddHHmmss，如果为空，表示马上发送
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		zdPack.setSendTime(sdf.format(new Date()));
		// 发送者用户ID，必须要写，如果是系统发送，填写默认用户ZDConstant.DEFAULT_USER_ID
		zdPack.setSendUserId(ZDConstant.DEFAULT_USER_ID);
		// 发送人所在单位名称，可以空
		zdPack.setSendUnitName("浙江浙大万朋软件有限公司");
		// 发送人姓名，可以为空
		zdPack.setSendUsername("林清振");
		// 采用同步或者异步方式发送，0表示异步，1或者为空表示同步
		// 如果采用异步方式发送，需要再定时调用短信结果接口来获取发送结果。
		zdPack.setIsSync("1");
		try {
			SendResult sr = ZDResponse.post(account, zdPack);
			System.out.println(sr.getCode());
			System.out.println(sr.getDescription());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// --------------------------------发送短信---------END
	}

	public static SystemIniService getSystemIniService() {
		if (systemIniService == null)
			systemIniService = Evn.getBean("systemIniService");
		return systemIniService;
	}

}
