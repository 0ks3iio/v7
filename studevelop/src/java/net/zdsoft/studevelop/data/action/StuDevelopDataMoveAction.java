package net.zdsoft.studevelop.data.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.studevelop.data.service.StudevelopTemplateDataService;

/**
 * 
 * 
 * @author weixh
 * 2019年6月17日	
 */
@Controller
@RequestMapping("/studevelop/common/data")
public class StuDevelopDataMoveAction extends BaseAction {
	@Autowired
	private StudevelopTemplateDataService studevelopTemplateDataService;
	
	@RequestMapping("/move")
	public String move(HttpServletRequest req, ModelMap map) {
		String unitId = req.getParameter("unitId");
		if(StringUtils.isEmpty(unitId) && getLoginInfo().getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
			//return promptFlt(map, "参数错误：unitId为空");
			unitId = getLoginInfo().getUnitId();
		}
		if(StringUtils.isEmpty(unitId)) {
			return promptFlt(map, "参数错误：unitId为空");
		}
		String acadyear=req.getParameter("acadyear");
		String semester = req.getParameter("semester");
		if(StringUtils.isEmpty(acadyear)) {
			acadyear = "2018-2019";
		}
		if(StringUtils.isEmpty(semester)) {
			semester = "1";
		}
		try {
			studevelopTemplateDataService.saveTempData(unitId, acadyear, semester);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "数据迁移失败！"+e.getMessage());
		}
		return promptFlt(map, "数据迁移成功！");
	}
	
	@RequestMapping("/copy")
	public String copy(HttpServletRequest req, ModelMap map) {
		String unitId = req.getParameter("unitId");
		if(StringUtils.isEmpty(unitId) && getLoginInfo().getUnitClass() == Unit.UNIT_CLASS_SCHOOL) {
			unitId = getLoginInfo().getUnitId();
		}
		if(StringUtils.isEmpty(unitId)) {
			return promptFlt(map, "参数错误：unitId为空");
		}
		String acadyear=req.getParameter("acadyear");
		if(StringUtils.isEmpty(acadyear)) {
			acadyear = "2018-2019";
		}
		boolean force = NumberUtils.toInt(req.getParameter("force"))==1;
		try {
			studevelopTemplateDataService.saveCopyTempData(unitId, acadyear, force);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "数据拷贝失败！"+e.getMessage());
		}
		return promptFlt(map, "数据拷贝成功！");
	}

}
