package net.zdsoft.studevelop.mobile.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("/mobile/open/studevelop/hoilday")
public class StuHolidayAction extends MobileAction {
	
	
	
	@RequestMapping("/index")
    @ControllerInfo(value = "我的假期")
	public String showMyHoliday(ModelMap map){
		
		return "/studevelop/mobile/myHoliday.ftl";
	}
	
}
