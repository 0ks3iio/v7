package net.zdsoft.remote.openapi.action;

import java.util.List;

import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceService;
import net.zdsoft.remote.openapi.service.OpenApiParameterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yangsj 
 * 页面中维护接口
 */
@Controller
@RequestMapping("/developer/interface/")
public class OpenApiInterfaceAction extends OpenApiBaseAction{

	@Autowired
	private OpenApiInterfaceService openApiInterfaceService;
	@Autowired
	private OpenApiParameterService openApiParameterService;
	@Autowired
	private OpenApiEntityService openApiEntityService;
	
	/**
     * 开发者管理
     * 
     * @author chicb
     * @param map
     * @return
     */
    @RequestMapping("/findList")
    public String showInterfaceList(ModelMap map) {
    	List<OpenApiInterface> interfaces = openApiInterfaceService.findAll();
    	map.put("interfaces", interfaces);
        return "/openapi/interfaceManage/interfaceIndex.ftl";
    }
}
