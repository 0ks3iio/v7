package net.zdsoft.basedata.dingding.service;

import java.util.List;

import net.zdsoft.basedata.dingding.entity.DingDingMsg;
import net.zdsoft.basedata.service.BaseService;

import com.alibaba.fastjson.JSONObject;

public interface DingDingMsgService extends BaseService<DingDingMsg, String>{

	/**
	 * 往钉钉发送消息 
	 * @param unitId　单位Id
	 * @param content　消息格式文本　有文本、链接消息等 　DdMsgUtils
	 * 
	 * @如果有错误信息　返回错误信息　成功则返回空
	 */
	public String addMessages(String unitId,
			List<JSONObject> content) ;
}
