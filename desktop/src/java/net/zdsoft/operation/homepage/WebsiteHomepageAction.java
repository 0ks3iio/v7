package net.zdsoft.operation.homepage;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.UrlUtils;

/**
 * @author panlf 2018-12-29
 */
@Controller
@RequestMapping("/operation/website")
public class WebsiteHomepageAction extends BaseAction {
	//页面返回
	@GetMapping(value = "/{systemName}")
	public String doGetSystemInfo(@PathVariable("systemName") String name,
								HttpServletRequest request,
								Model model,
								@RequestParam(value = "pos", required = false) Integer pos) {
        if (isMobile(request) && "index".equals(name)) {

            return "/operation/website/app/xgk-app.ftl";
        }

        model.addAttribute("realName",getLoginInfo() == null ? "未登录" : getLoginInfo().getRealName());
        model.addAttribute("call", UrlUtils.getPrefix(getRequest()) + "/operation/website/index");
        model.addAttribute("pos",pos==null?-1:pos);
        if ("index".equals(name)) {
            return "redirect:http://www.yunschool.com";
        }
		return String.format("/operation/website/%s.ftl", name); 
	}

	//登录
    @RequestMapping("/loginForOperation")
    public String loginForOperation(HttpServletRequest request) {
        if (getLoginInfo() == null) {
            return "redirect:/fpf/login/loginForPassport.action?call=http://" +
                    request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/operation/website/index";
        } else {
            return "redirect:/operation/website/index";
        }

    }
    
   
    private boolean isMobile(HttpServletRequest request) {
        UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem os = ua.getOperatingSystem();
        if(DeviceType.MOBILE.equals(os.getDeviceType())) {
            return true;
        }
        return false;
    }
}
