package net.zdsoft.basedata.action;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.action.BaseAction;

@Controller
@RequestMapping("/basedata")
public class ModelAction extends BaseAction {

	@Autowired
	private UnitService unitService;

	@RequestMapping("/model/index/page")
	public String index(ModelMap map) {
		Unit topUnit = unitService.findTopUnit(getLoginInfo().getUnitId());
		if (topUnit == null || !StringUtils.equalsIgnoreCase(topUnit.getId(), getLoginInfo().getUnitId())) {
			addErrorFtlOperation(map, "返回", "/basedata/model/index/page");
			addErrorFtlOperation(map, "成绩录入", "/scoremanage/scoreInfo/index/page");
			return errorFtl(map, "只能是顶级单位才能维护！");
		}
		return "somepage.ftl";
	}
}
