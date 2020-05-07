package net.zdsoft.basedata.dingding.service.impl;

import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.basedata.dingding.service.DingDingSyncdataLogService;
import net.zdsoft.basedata.dingding.service.DingDingUserService;
import net.zdsoft.basedata.dingding.utils.DingDingUtils;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("dingDingUserService")
public class DingDingUserServiceImpl implements DingDingUserService {
	private static Logger log = Logger.getLogger(DingDingUserServiceImpl.class);
	public static final String DINGDING_ADD_USER_URL = "https://oapi.dingtalk.com/user/create?access_token=";// 钉钉添加用户ＵＲＬ
	public static final String DINGDING_UPDATE_USER_URL = "https://oapi.dingtalk.com/user/update?access_token=";// 钉钉修改用户ＵＲＬ
	public static final String DINGDING_DELETE_USER_URL = "https://oapi.dingtalk.com/user/delete?access_token=";// 钉钉删除用户ＵＲＬ

	@Autowired
	UserService userService;
	@Autowired
	DingDingSyncdataLogService dingDingSyncdataLogService;

	public void getUsers() {

//		String accessToken = DingDingUtils.getAccessToken();
//		if (StringUtils.isBlank(accessToken)) {
//			log.error("------获取令牌失败------");
//			return;
//		}
//		String url = "https://oapi.dingtalk.com/user/simplelist?access_token="
//				+ accessToken;
//		url += "&department_id=22802129";
//		String result = DingDingUtils.push(url);
//		if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
//			System.out.println(result);
//		} else {
//			log.error("钉钉获取用户出错啦！");
//		}
	}

	public void addUser(String unitId,String accessToken,String userId, String userName, String content) {
		DdSyncdataLog dataLog = new DdSyncdataLog();
		dataLog.setUnitId(unitId);
		dataLog.setDataType("user");
		dataLog.setObjectName(userName);
		dataLog.setHandlerType("新增");

		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			dataLog.setResult(0);
			dataLog.setRemark("获取令牌失败");
			dingDingSyncdataLogService.insertLogs(dataLog);
			return;
		}
		String url = DINGDING_ADD_USER_URL + accessToken;
		String result = DingDingUtils.push(url, content);
		if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
			JSONObject jsonOjbect = Json.parseObject(result);
			if ("0".equals(jsonOjbect.getString("errcode"))) {
				String dingdingId = jsonOjbect.getString("userid");
				userService.updateDingDingIdById(dingdingId, userId);
				dataLog.setResult(1);
			} else {
				log.error(jsonOjbect.getString("errmsg"));
				dataLog.setResult(0);
				dataLog.setRemark(jsonOjbect.getString("errmsg"));
			}
		} else {
			log.error("钉钉同步新增用户出错啦！");
			dataLog.setResult(0);
			dataLog.setRemark("钉钉同步新增用户出错啦！");
		}
		dingDingSyncdataLogService.insertLogs(dataLog);
	}

	public void updateUser(String unitId,String accessToken,String userName, String content) {
		DdSyncdataLog dataLog = new DdSyncdataLog();
		dataLog.setUnitId(unitId);
		dataLog.setDataType("user");
		dataLog.setObjectName(userName);
		dataLog.setHandlerType("更新");
		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			dataLog.setResult(0);
			dataLog.setRemark("获取令牌失败");
			dingDingSyncdataLogService.insertLogs(dataLog);
			return;
		}
		String url = DINGDING_UPDATE_USER_URL + accessToken;
		String result = DingDingUtils.push(url, content);
		if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
			JSONObject jsonOjbect = Json.parseObject(result);
			if (!"0".equals(jsonOjbect.getString("errcode"))) {
				log.error(jsonOjbect.getString("errmsg"));
				dataLog.setResult(0);
				dataLog.setRemark(jsonOjbect.getString("errmsg"));
			} else {
				dataLog.setResult(1);
			}
		} else {
			log.error("钉钉同步修改用户出错啦！");
			dataLog.setResult(0);
			dataLog.setRemark("钉钉同步修改用户出错啦！");

		}
		dingDingSyncdataLogService.insertLogs(dataLog);
	}

	public void deleteUser(String unitId,String accessToken,String userName, String userId) {
		DdSyncdataLog dataLog = new DdSyncdataLog();
		dataLog.setUnitId(unitId);
		dataLog.setDataType("user");
		dataLog.setObjectName(userName);
		dataLog.setHandlerType("删除");

		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			dataLog.setResult(0);
			dataLog.setRemark("获取令牌失败");
			dingDingSyncdataLogService.insertLogs(dataLog);
			return;
		}
		String url = DINGDING_DELETE_USER_URL + accessToken;
		url += "&userid=" + userId;
		String result = DingDingUtils.push(url);
		if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
			JSONObject jsonOjbect = Json.parseObject(result);
			if (!"0".equals(jsonOjbect.getString("errcode"))) {
				log.error(jsonOjbect.getString("errmsg"));
				dataLog.setResult(0);
				dataLog.setRemark(jsonOjbect.getString("errmsg"));
			} else {
				dataLog.setResult(1);
			}
		} else {
			log.error("钉钉同步删除用户出错啦！");
			dataLog.setResult(0);
			dataLog.setRemark("钉钉同步删除用户出错啦！");
		}
		dingDingSyncdataLogService.insertLogs(dataLog);
	}

}
