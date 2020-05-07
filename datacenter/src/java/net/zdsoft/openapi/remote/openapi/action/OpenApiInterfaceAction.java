package net.zdsoft.openapi.remote.openapi.action;

import java.util.List;

import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiParameterService;

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
