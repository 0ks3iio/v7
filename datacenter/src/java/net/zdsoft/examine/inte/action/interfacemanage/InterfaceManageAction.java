package net.zdsoft.examine.inte.action.interfacemanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.base.dto.OpenInterfaceDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.enums.InterfaceDateTypeEnum;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiInterfaceTypeService;
import net.zdsoft.base.service.OpenApiParameterService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = "/datacenter/examine/interface")
public class InterfaceManageAction extends InterfaceBaseAction{

	@Autowired
	private OpenApiInterfaceService openApiInterfaceService;
	@Autowired
    private OpenApiParameterService openApiParameterService;
	@Autowired
	private OpenApiInterfaceTypeService openApiInterfaceTypeService;
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map) {
        return "/examine/interfaceManage/inteTab.ftl";
    }
	
	@RequestMapping("/showIndex")
    public String showIndexManageIndex(ModelMap map) {
        return "/examine/interfaceManage/interface/intefaceIndex.ftl";
    }
	/**
     * 接口管理
     */
    @RequestMapping("/showInterfaces")
    public String showInterfaces(HttpServletRequest request,String typeName, Integer isUsing, Integer dataType, ModelMap map) {
        List<OpenApiInterface> interfaces = openApiInterfaceService.getAllInterfaces(typeName, isUsing, dataType, null);
        ArrayList<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
    	if(CollectionUtils.isNotEmpty(interfaces)){
    		 for (OpenApiInterface openApiInterface : interfaces) {
    			 OpenInterfaceDto openInterfaceDto = new OpenInterfaceDto();
    			 openInterfaceDto.setId(openApiInterface.getId());
    			 openInterfaceDto.setDescription(openApiInterface.getDescription());
    			 String dataTypeName = InterfaceDateTypeEnum.get(openApiInterface.getDataType()).getDescription();
    			 openInterfaceDto.setDataTypeName(dataTypeName);
    			 openInterfaceDto.setMethodType(openApiInterface.getMethodType());
    			 openInterfaceDto.setUrlString(openApiInterface.getUri());
    			 openInterfaceDto.setState(openApiInterface.getIsUsing() == 1 ? "启用" : "停用");
    			 openInterfaceDto.setIsUsing(openApiInterface.getIsUsing());
    			 openInterfaceDto.setInterfaceName(openApiInterface.getTypeName());
    			 openInterfaceDto.setType(openApiInterface.getType());
    			 openInterfaceDto.setResultType(openApiInterface.getResultType());
    			 openInterfaceDtos.add(openInterfaceDto);
    		 }
    	}
    	map.put("openInterfaceDtos", openInterfaceDtos);
        return "/examine/interfaceManage/interface/intefaceList.ftl";
    }

    /**
     * 删除接口
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delInterface")
    @ControllerInfo("删除接口")
    public String delInterface(String interfaceId) {
    	try {
    		OpenApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
    		if(isNotExistOpenApiApply(openApiInterface.getResultType())){
    			openApiInterfaceService.delete(interfaceId);
    		}else{
    			return error("接口正在使用，不能删除！");
    		}
    		//删除关联数据
    		openApiParameterService.deleteByUri(openApiInterface.getUri());
		} catch (Exception e) {
			return error("删除接口失败！"+e.getMessage());
		}
        return success("删除接口成功");
    }
   
    /**
     * 接口启用和不启用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/isUsingInterface")
    @ControllerInfo("启用和停用接口")
    public String isUsingInterface(String interfaceId, int isUsing) {
    	try {
    		//先判断停用的接口是否正在审核或使用
    		OpenApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
    		if(isNotExistOpenApiApply(openApiInterface.getResultType()) || isUsing == OpenApiInterface.TRUE_IS_USING){
    			openApiInterfaceService.updateInterfaceById(isUsing,interfaceId);
    		}else{
    			return error("接口正在使用，不能停用！");
    		}
		} catch (Exception e) {
			return error("启用接口失败！"+e.getMessage());
		}
    	return success(OpenApiInterface.TRUE_IS_USING == isUsing ?"启用接口成功" : "停用接口成功");
    }
    
    /**
     * 修改接口
     * @param interfaceId
     * @return
     */
    @RequestMapping("/editInterface")
    @ControllerInfo("修改接口")
    public String editInterface(String interfaceId,ModelMap map) {
    	if(StringUtils.isNotBlank(interfaceId)){
    		OpenApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
    		map.put("interface", openApiInterface);
    	}
    	map.put("interTypeList", getInterfaceTypes());
    	map.put("resultTypeList", getResultTypes());
		return "/examine/interfaceManage/interface/intefaceEdit.ftl";
    }
    
    /**
     * 保存参数
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveInterface")
    @ControllerInfo("超管保存接口")
    public String saveInterface(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		OpenApiInterface openApiInterface;
    		String interfaceId = jsonObject.getString("interfaceId");
        	if(StringUtils.isBlank(interfaceId)){
        		openApiInterface =new OpenApiInterface();
        	}else{
        		openApiInterface = openApiInterfaceService.findOne(interfaceId);
        	}
    		openApiInterfaceService.save(getApiInterface(openApiInterface,jsonObject));
		} catch (Exception e) {
			return error("保存接口失败！"+e.getMessage());
		}
        return success("保存接口成功");
    }
    
    
//    ---------------------------------------------------------私有的方法区 -----------------------------------------------------
    private OpenApiInterface getApiInterface(OpenApiInterface openApiInterface,JSONObject jsonObject){
    	Developer developer = getDeveloper();
    	openApiInterface.setTicketKey(developer.getTicketKey());
    	String interfaceId = jsonObject.getString("interfaceId");
    	if(StringUtils.isBlank(interfaceId)){
    		interfaceId = UuidUtils.generateUuid();
    		openApiInterface.setCreationTime(new Date());
    	}
    	openApiInterface.setModifyTime(new Date());
    	openApiInterface.setId(interfaceId);
    	openApiInterface.setDataType(Integer.valueOf(jsonObject.getString("dataType")));
    	String isUsing = jsonObject.getString("isUsing");
    	if(StringUtils.isBlank(isUsing)){
    		isUsing = "1";
    	}
    	openApiInterface.setIsUsing(Integer.valueOf(isUsing));
    	openApiInterface.setMethodType(jsonObject.getString("methodType"));
    	openApiInterface.setResultType(jsonObject.getString("resultType"));
    	openApiInterface.setTableName(jsonObject.getString("tableName"));
    	String[] paramString  = jsonObject.getString("type").split(",");
    	openApiInterface.setType(paramString[0]);
    	openApiInterface.setTypeName(paramString[1]);
    	openApiInterface.setUri(jsonObject.getString("uri"));
    	openApiInterface.setDescription(jsonObject.getString("description"));
    	openApiInterface.setFpkColumnName(jsonObject.getString("fpkColumnName"));
    	return  openApiInterface;
    }
}
