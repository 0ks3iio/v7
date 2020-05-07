package net.zdsoft.bigdata.data.action;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/bigdata/common/")
public class BigdataIndexController extends BigdataBaseAction {

	@Autowired
	private OptionService optionService;

	@Autowired
	private BgUserAuthService bgUserAuthService;

	/**
	 * 大数据首页面板
	 */
	@RequestMapping("/index")
	public String execute(ModelMap map) {
		OptionDto dashboardDto = null;
		if (getLoginInfo().getUserType() == User.USER_TYPE_TOP_ADMIN) {
			return "/bigdata/introduction.ftl";
		} else if (bgUserAuthService.isBackgroundUser(getLoginInfo()
				.getUserId(), getLoginInfo().getUserType())) {
			dashboardDto = optionService.getAllOptionParam("dashboard4bc");
		} else {
			dashboardDto = optionService.getAllOptionParam("dashboard4front");
		}
		if (dashboardDto != null
				&& StringUtils.isNotBlank(dashboardDto.getFrameParamMap().get(
						"dashboard_url"))) {
			return "redirect:"
					+ dashboardDto.getFrameParamMap().get("dashboard_url");
		}
		return "/bigdata/introduction.ftl";
	}

	/**
	 * 大数据展示页
	 */
	@RequestMapping("/introduction")
	public String introduction(ModelMap map) {
		return "/bigdata/introduction.ftl";
	}
}
