package net.zdsoft.framework.utils;


import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class DdMsgUtils {
	private static Logger log = Logger.getLogger(DdMsgUtils.class);

	// text消息、image消息、voice消息、file消息、link消息、OA消息、markdown消息
	public static final String DINGDING_MSG_TYPE_TEXT = "text";
	public static final String DINGDING_MSG_TYPE_LINK = "link";

	public static final String DINGDING_TO_USER = "touser";
	public static final String DINGDING_TO_PARTY = "toparty";
	public static final String DINGDING_MSGTYPE = "msgtype";

	/**
	 * 文本消息
	 * 
	 * @param userIds
	 *            用户 userId1|userId2|userId3
	 * @param partyIds
	 *            部门partyId1|partyId2|partyId3
	 * @param msgType
	 *            消息类型
	 * @param content
	 *            　消息内容
	 * @return
	 */
	public static JSONObject toDingDingTextJson(final String userIds,
			final String partyIds, final String content) {
		JSONObject dingdingJson = new JSONObject();
		dingdingJson.put(DINGDING_TO_USER, userIds);
		dingdingJson.put(DINGDING_TO_PARTY, partyIds);

		dingdingJson.put(DINGDING_MSGTYPE, DINGDING_MSG_TYPE_TEXT);

		JSONObject textJson = new JSONObject();
		textJson.put("content", content);

		dingdingJson.put("text", textJson);
		log.debug(dingdingJson.toJSONString());
		return dingdingJson;
	}

	/**
	 * 链接消息
	 * 
	 * @param userIds
	 *            用户 userId1|userId2|userId3
	 * @param partyIds
	 *            部门partyId1|partyId2|partyId3
	 * @param msgType
	 *            消息类型
	 * @param content
	 *            　消息内容
	 * @return
	 */
	public static JSONObject toDingDingLinkJson(final String userIds,
			final String partyIds, final String title, final String url,
			final String picUrl, final String content) {
		JSONObject dingdingJson = new JSONObject();
		dingdingJson.put(DINGDING_TO_USER, userIds);
		dingdingJson.put(DINGDING_TO_PARTY, partyIds);
		dingdingJson.put(DINGDING_MSGTYPE, DINGDING_MSG_TYPE_LINK);

		JSONObject linkJson = new JSONObject();
		linkJson.put("messageUrl", content);
		linkJson.put("picUrl", "@lALOACZwe2Rk");
		linkJson.put("title", title);
		linkJson.put("text", content);

		dingdingJson.put("link", linkJson);
		log.debug(dingdingJson.toJSONString());
		return dingdingJson;
	}
}
