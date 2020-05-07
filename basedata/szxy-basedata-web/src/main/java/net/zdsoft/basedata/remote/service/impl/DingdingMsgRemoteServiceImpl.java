package net.zdsoft.basedata.remote.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.dingding.entity.DingDingMsg;
import net.zdsoft.basedata.dingding.service.DingDingMsgService;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DingdingMsgRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.DdMsgUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Service("dingdingMsgRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class DingdingMsgRemoteServiceImpl extends BaseRemoteServiceImpl<DingDingMsg,String> implements DingdingMsgRemoteService{

	@Autowired
	private DingDingMsgService dingDingMsgService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Override
	public String sendDingDingTextMsgs(String unitId,String[] userIds,String[] partyIds,String content){
		if(userIds==null || userIds.length == 0){
			return "没有要推送的用户";
		}
		List<User> userList = SUtils.dt(userRemoteService.findListByIds(userIds), new TypeReference<List<User>>() {});
		String dingdingUserIds = "";
		String dingdingDeptIds = "";//部门id暂为""
		for (User dingUser : userList) {
			if(StringUtils.isNotBlank(dingUser.getDingdingId())){
				dingdingUserIds += "|" + dingUser.getDingdingId();
			}
		}
		if (StringUtils.isNotBlank(dingdingUserIds)) {
			dingdingUserIds = dingdingUserIds.substring(1);
		}else{
			return "没有要推送的钉钉账号";
		}
		JSONObject textJson = DdMsgUtils.toDingDingTextJson(dingdingUserIds, dingdingDeptIds,
				content);
		List<JSONObject> contents = new ArrayList<JSONObject>();
		contents.add(textJson);
		return dingDingMsgService.addMessages(unitId, contents);
	}
	
	@Override
	public String addDingDingMsgs(String unitId, List<JSONObject> contents) {
		return dingDingMsgService.addMessages(unitId, contents);
	}

	@Override
	protected BaseService<DingDingMsg, String> getBaseService() {
		return dingDingMsgService;
	}

}
