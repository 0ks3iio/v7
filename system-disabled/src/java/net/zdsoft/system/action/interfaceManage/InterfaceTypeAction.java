package net.zdsoft.system.action.interfaceManage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceRemoteService;
import net.zdsoft.system.dto.interfaceManage.OpenInterfaceDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/system/interface/type")
public class InterfaceTypeAction extends BaseAction{
	
	@Resource
	private OpenApiInterfaceRemoteService openApiInterfaceRemoteService;
	@Autowired 
	private OpenApiEntityRemoteService openApiEntityRemoteService;
	/*
	 * 类型管理
	 */
	@RequestMapping("/findInterface")
    public String findInteTypes(HttpServletRequest request,Integer dataType, ModelMap map) {
        List<String[]> interfaceTypes = SUtils.dt(openApiInterfaceRemoteService.findDistinctTypeByDataType(dataType),
        		String[].class);
        List<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
        for(Object[] type : interfaceTypes) {
        	 OpenInterfaceDto dto = new OpenInterfaceDto();
        	 dto.setType(String.valueOf(type[0]));
        	 dto.setInterfaceName(String.valueOf(type[1]));
        	 openInterfaceDtos.add(dto);
        }
    	map.put("openInterfaceDtos", openInterfaceDtos);
        return "/system/interfaceManage/interTypeList.ftl";
    }
	
	
	/**
     * 删除类型，会删除这个类型下面的所有属性和 调用属性的开发者
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delType")
    @ControllerInfo("删除类型")
    public String delType(String type) {
    	try {
    		openApiEntityRemoteService.deleteByType(type);
		} catch (Exception e) {
			return error("删除类型失败！"+e.getMessage());
		}
        return success("删除类型成功");
    }
}
