package net.zdsoft.api.index.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.openapi.remote.openapi.action.ApiBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * api 首页
 */
@Controller
@RequestMapping(value = "/bigdata/api")
public class ApiController extends ApiBaseAction {

	@Resource
	private ApiDeveloperService apiDeveloperService;
	
	@ControllerInfo(value = "进入首页")
    @RequestMapping("/index")
    public String execute(HttpServletRequest request, ModelMap map) {
		ApiDeveloper developer = getDeveloper();
		if(developer != null){
			map.put("developer", developer);
			map.put("isLogin", Boolean.TRUE);
		}
        return "/api/homepage/index.ftl";
    }
	
	@ControllerInfo(value = "开发者注册")
    @RequestMapping("/developer/page")
    public String agreementPage(HttpServletRequest request, ModelMap map) {
        return "/api/homepage/agreement.ftl";
    }
	
	@RequestMapping("/apply/index")
	public String execute(ModelMap map,HttpServletRequest request, String type) {
		ApiDeveloper developer = getDeveloper();
		if(developer == null){
			return "/api/homepage/index.ftl";
		}
		map.put("username", developer.getUsername());
		map.put("type", type);
		return "/api/index/api-index.ftl";
	}
}
