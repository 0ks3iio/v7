package net.zdsoft.bigdata.data.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.export.Statistical;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/bigdata/user/dashboard")
public class UserDashboardController extends BigdataBaseAction {

	@Resource
	private Statistical statistical;

	@RequestMapping("/index")
	public String index(ModelMap map) {
		return "/bigdata/user/userDashboard.ftl";
	}

	@RequestMapping("/reportMark")
	public String reportMark(ModelMap map) {
		List<Json> reportMarkList = new ArrayList<Json>();
		List<String> resultList = RedisUtils.lrange(getLoginInfo().getUserId()
				+ "-desktop-report-mark");
		Set<String> businessIds = new HashSet<String>();
		for (String str : resultList) {
			Json reportMark = JSON.parseObject(str, Json.class);
			if (!businessIds.contains(reportMark.getString("businessId"))) {
				businessIds.add(reportMark.getString("businessId"));
				reportMarkList.add(reportMark);
			}
		}
		map.put("reportMarkList", reportMarkList);
		return "/bigdata/user/component/reportMark.ftl";
	}

	@RequestMapping("/reportStat")
	public String reportStat(ModelMap map) {
		String key = getLoginInfo().getUserId() + "-user-report-stat";
		Json statData = RedisUtils.getObject(key, Json.class);
		if (statData == null) {
			statData = new Json();
			statData.put("user_cockpit_num", statistical.countUserReport(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					ChartBusinessType.COCKPIT.getBusinessType()));
			statData.put("user_chart_num", statistical.countUserReport(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					ChartBusinessType.CHART.getBusinessType()));
			statData.put("user_report_num", statistical.countUserReport(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					ChartBusinessType.REPORT.getBusinessType()));
			statData.put("user_model_report_num", statistical.countUserReport(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					ChartBusinessType.MODEL_REPORT.getBusinessType()));
			statData.put("user_data_board_num", statistical.countUserReport(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					ChartBusinessType.DATA_BOARD.getBusinessType()));
			statData.put("user_data_report_num", statistical.countUserReport(
					getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					ChartBusinessType.DATA_REPORT.getBusinessType()));
			RedisUtils.set(key, statData.toJSONString(), 600);
		}
		map.put("statData", statData);
		return "/bigdata/user/component/reportStat.ftl";
	}

	@RequestMapping("/operationMark")
	public String operationMark(ModelMap map) {
		List<Json> operationMarkList = new ArrayList<Json>();
		List<String> resultList = RedisUtils.lrange(getLoginInfo().getUserId()
				+ "-desktop-operation-mark");
		Set<String> moduleIds = new HashSet<String>();
		for (String str : resultList) {
			Json operationMark = JSON.parseObject(str, Json.class);
			if (!moduleIds.contains(operationMark.getString("modelId"))) {
				moduleIds.add(operationMark.getString("modelId"));
				operationMarkList.add(operationMark);
			}
		}
		map.put("operationMarkList", operationMarkList);
		return "/bigdata/user/component/operationMark.ftl";
	}

}
