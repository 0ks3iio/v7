package net.zdsoft.desktop.action;

import javax.servlet.http.HttpSession;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/useStatistic")
public class UseStatisticAction extends BaseAction{

	@ResponseBody
	@RequestMapping("/index")
	@ControllerInfo(value = "使用统计")
	public String showConList(ModelMap map,HttpSession httpSession,String acadyear){
		JSONObject json = new JSONObject();
		try {
			String unitNum=RedisUtils.get("use-statistic-unit-timer-v7");
			String stuNum=RedisUtils.get("use-statistic-student-timer-v7");
			String teaNum=RedisUtils.get("use-statistic-teacher-timer-v7");
			String useSchoolsNum=RedisUtils.get("use-statistic-use-school-timer-v7");
			String webNum=RedisUtils.get("use-statistic-web-timer-v7");
			String mobileNum=RedisUtils.get("use-statistic-mobile-timer-v7");
			String totalNum=RedisUtils.get("use-statistic-total-timer-v7");
			json.put("unitNum", unitNum);
			json.put("stuNum", stuNum);
			json.put("teaNum", teaNum);
			json.put("useSchoolsNum", useSchoolsNum);
			json.put("webNum", webNum);
			json.put("mobileNum", mobileNum);
			json.put("totalNum", totalNum);
			json.put("col", 3);
		} catch (Exception e) {
		} finally {
			json.put("title", "使用统计");
		}
		return json == null ? StringUtils.EMPTY : json.toJSONString();
	}
}
