package net.zdsoft.basedata.remote.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;


public interface DingdingMsgRemoteService {
 
	/**
	 * 推送消息到钉钉
	 * @param unitId
	 * @param contents
	 * @return
	 */
	public String addDingDingMsgs(String unitId,List<JSONObject> contents);
	
	/**
	 * 推送消息到钉钉--文本
	 * @param userIds----数字校园用户Ids
	 * @param partyIds----部门，暂时为空数组或null
	 * @param content
	 * @return
	 */
	public String sendDingDingTextMsgs(String unitId,String[] userIds,String[] partyIds,String content);
}
