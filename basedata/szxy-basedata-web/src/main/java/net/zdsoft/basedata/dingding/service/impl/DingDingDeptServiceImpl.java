package net.zdsoft.basedata.dingding.service.impl;

import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.basedata.dingding.service.DingDingDeptService;
import net.zdsoft.basedata.dingding.service.DingDingSyncdataLogService;
import net.zdsoft.basedata.dingding.utils.DingDingUtils;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("dingDingDeptService")
public class DingDingDeptServiceImpl implements DingDingDeptService {
	private static Logger log = Logger.getLogger(DingDingDeptServiceImpl.class);
	public static final String DINGDING_ADD_DEPT_URL = "https://oapi.dingtalk.com/department/create?access_token=";// 钉钉添加用户ＵＲＬ
	public static final String DINGDING_UPDATE_DEPT_URL = "https://oapi.dingtalk.com/department/update?access_token=";// 钉钉修改用户ＵＲＬ
	public static final String DINGDING_DELETE_DEPT_URL = "https://oapi.dingtalk.com/department/delete?access_token=";// 钉钉删除用户ＵＲＬ

	@Autowired
	DeptService deptService;
	@Autowired
	DingDingSyncdataLogService dingDingSyncdataLogService;

	public void getDepts() {
		//String accessToken = DingDingUtils.getAccessToken("dingaf9209227b155eef","kYkMpTNspxxgMznq1oROWMqM_CuHQEAYqSIgl_My35JvaNS5tfA20WUziWvDhy7h");
		String accessToken = DingDingUtils.getAccessToken("dingaee8b7e82e22643335c2f4657eb6378f","tB6-Lu1w4inQ6ymvYlDZepp1WjqhPMTRD0HBGjVDM2Ee3rq9oIVbEIxAh_HlovF6");
		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			return;
		}
		String url = "https://oapi.dingtalk.com/department/list?access_token="
				+ accessToken;
		String result = DingDingUtils.push(url);
		if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
			System.out.println(result);
		} else {
			log.error("钉钉获取用户出错啦！");
		}
	}

	public String addDept(String unitId,String accessToken,String deptId, String deptName, String content) {
		String dingdingId="";
		DdSyncdataLog dataLog = new DdSyncdataLog();
		dataLog.setUnitId(unitId);
		dataLog.setDataType("dept");
		dataLog.setObjectName(deptName);
		dataLog.setHandlerType("新增");
		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			dataLog.setResult(0);
			dataLog.setRemark("获取令牌失败");
			dingDingSyncdataLogService.insertLogs(dataLog);
			return dingdingId;
		}

		String url = DINGDING_ADD_DEPT_URL + accessToken;
		String result = DingDingUtils.push(url, content);
		if (StringUtils.isNotBlank(result) && !"error".equals(result)) {
			JSONObject jsonOjbect = Json.parseObject(result);
			if ("0".equals(jsonOjbect.getString("errcode"))) {
				dingdingId = jsonOjbect.getString("id");
				deptService.updateDingDingIdById(dingdingId, deptId);
				dataLog.setResult(1);
			} else {
				log.error(jsonOjbect.getString("errmsg"));
				dataLog.setResult(0);
				dataLog.setRemark(jsonOjbect.getString("errmsg"));
			}
		} else {
			log.error("钉钉同步新增部门出错啦！");
			dataLog.setResult(0);
			dataLog.setRemark("钉钉同步新增部门出错啦！");
		}
		dingDingSyncdataLogService.insertLogs(dataLog);
		return dingdingId;
	}

	public void updateDept(String unitId,String accessToken,String deptName, String content) {
		DdSyncdataLog dataLog = new DdSyncdataLog();
		dataLog.setUnitId(unitId);
		dataLog.setDataType("dept");
		dataLog.setObjectName(deptName);
		dataLog.setHandlerType("更新");
		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			dataLog.setResult(0);
			dataLog.setRemark("获取令牌失败");
			dingDingSyncdataLogService.insertLogs(dataLog);
			return;
		}

		String url = DINGDING_UPDATE_DEPT_URL + accessToken;
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
			log.error("钉钉同步更新部门出错啦！");
			dataLog.setResult(0);
			dataLog.setRemark("钉钉同步更新部门出错啦！");
		}
		dingDingSyncdataLogService.insertLogs(dataLog);
	}

	public void deleteDept(String unitId,String accessToken,String deptName, String deptId) {
		DdSyncdataLog dataLog = new DdSyncdataLog();
		dataLog.setUnitId(unitId);
		dataLog.setDataType("dept");
		dataLog.setObjectName(deptName);
		dataLog.setHandlerType("删除");
		if (StringUtils.isBlank(accessToken)) {
			log.error("------获取令牌失败------");
			dataLog.setResult(0);
			dataLog.setRemark("获取令牌失败");
			dingDingSyncdataLogService.insertLogs(dataLog);
			return;
		}
		String url = DINGDING_DELETE_DEPT_URL + accessToken;
		url += "&id=" + deptId;
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
			log.error("钉钉同步删除部门出错啦！");
			dataLog.setResult(0);
			dataLog.setRemark("钉钉同步删除部门出错啦！");
		}
		dingDingSyncdataLogService.insertLogs(dataLog);
	}
}
