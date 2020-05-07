package net.zdsoft.framework.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.annotation.ControllerInfo;

@RequestMapping("/common/support")
@Controller
public class SupportAction {

	@RequestMapping("/index")
	@ResponseBody
	@ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_IGNORE)
	public String index(String str) {
		return str;
	}
}
