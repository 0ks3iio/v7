package net.zdsoft.system.remote.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.service.sms.ZDResponse;

@Service("smsRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SmsRemoteServiceImpl implements SmsRemoteService {

	@Override
	public String sendSms(String[] phones, String msg, String sendTime) {
		JSONObject json = new JSONObject();
		json.put("msg", msg);
		if (StringUtils.isNotBlank(sendTime))
			json.put("sendTime", sendTime);
		// 这个参数可以外面传进来，也可以不写， 
		//json.put("smsServerUrl","http://192.168.0.22:8080/v6");
		// 这个参数可以外面传进来，也可以不写， 
		//json.put("ticket","8BE7AE990E8566A2E050A8C09B0072B0");
		JSONArray receivers = new JSONArray();
		for (String phone : phones) {
			JSONObject receiverJ = new JSONObject();
			receiverJ.put("phone", phone);
			receivers.add(receiverJ);
		}
		// 下面可以为空
		// receiverJ.put("unitName", "浙大万朋");
		// receiverJ.put("unitId", "浙大万朋ID");
		// receiverJ.put("username", "linqz");
		// receiverJ.put("userId", "linqzID");
		json.put("receivers", receivers);
		return SUtils.s(ZDResponse.post(json).getCode());
	}
	@Override
	public String sendSms(String[] phones, String msg, String ticket, String sendTime) {
		JSONObject json = new JSONObject();
		json.put("msg", msg);
		if (StringUtils.isNotBlank(sendTime))
			json.put("sendTime", sendTime);
		// 这个参数可以外面传进来，也可以不写， 
		//json.put("smsServerUrl","http://192.168.0.22:8080/v6");
		// 这个参数可以外面传进来，也可以不写， 
		json.put("ticket",ticket);
		JSONArray receivers = new JSONArray();
		for (String phone : phones) {
			JSONObject receiverJ = new JSONObject();
			receiverJ.put("phone", phone);
			receivers.add(receiverJ);
		}
		// 下面可以为空
		// receiverJ.put("unitName", "浙大万朋");
		// receiverJ.put("unitId", "浙大万朋ID");
		// receiverJ.put("username", "linqz");
		// receiverJ.put("userId", "linqzID");
		json.put("receivers", receivers);
		return SUtils.s(ZDResponse.post(json).getCode());
	}
	@Override
	public String sendSmsByUnitId(String[] phones, String msg, String sendTime, String unitId) {
		JSONObject json = new JSONObject();
		json.put("msg", msg);
		json.put("unitId", unitId);
		if (StringUtils.isNotBlank(sendTime))
			json.put("sendTime", sendTime);
		JSONArray receivers = new JSONArray();
		for (String phone : phones) {
			JSONObject receiverJ = new JSONObject();
			receiverJ.put("phone", phone);
			receivers.add(receiverJ);
		}
		json.put("receivers", receivers);
		return SUtils.s(ZDResponse.post(json).getCode());
	}
	
	
}
