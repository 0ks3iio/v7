package net.zdsoft.desktop.action;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("")
public class WelcomeAction extends BaseAction{

	@Autowired private UserRemoteService userRemoteService;
	
	@ResponseBody
	@RequestMapping("/checkLive")
	@ControllerInfo(ignoreLog=ControllerInfo.LOG_FORCE_IGNORE)
	public String checkLive() {
		return "";
	}

    @ResponseBody
    @RequestMapping("/common/heartbeat")
    @ControllerInfo(ignoreLog=ControllerInfo.LOG_FORCE_IGNORE)
    public String heartbeat() {
        Json result =new Json();
        result.put("status","success");
        return result.toJSONString();
    }

	@RequestMapping("/smart.html")
	public String index(ModelMap map){
		map.put("login", getSession() != null && ServletUtils.getLoginInfo(getSession()) != null);
		if(getLoginInfo()!=null){
			User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()),User.class);
			map.put("realName",user!=null?user.getRealName():"");
		}
		return "/desktop/smart-index.ftl";
	}

}
