package net.zdsoft.system.action.interfaceManage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.remote.service.OpenApiApplyRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiParameterRemoteService;
import net.zdsoft.system.dto.interfaceManage.OpenInterfaceDto;

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
@RequestMapping(value = "/system/interface")
public class InterfaceManageAction extends BaseAction{

	@Resource
	private OpenApiInterfaceRemoteService openApiInterfaceRemoteService;
	@Autowired
    private OpenApiParameterRemoteService openApiParameterRemoteService;
	@Autowired
	private OpenApiApplyRemoteService openApiApplyRemoteService;
	
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map) {
        return "/system/interfaceManage/inteManageIndex.ftl";
    }
	
	@RequestMapping("/showIndex")
    public String showIndexManageIndex(ModelMap map) {
        return "/system/interfaceManage/interface/intefaceIndex.ftl";
    }
	/**
     * 接口管理
     */
    @RequestMapping("/findInterfaces")
    public String findInterfaces(HttpServletRequest request,String typeName, Integer isUsing, Integer dataType, ModelMap map) {
//    	Pagination page = createPagination();
//		Map<String, String> paramMap = syncParameters(request);
//		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
//		if (row <= 0) {
//			page.setPageSize(20);
//		}
        List<OpenApiInterface> interfaces = SUtils.dt(openApiInterfaceRemoteService.getAllInterfaces(typeName, isUsing, dataType, null),
        		OpenApiInterface.class);
        ArrayList<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
    	if(CollectionUtils.isNotEmpty(interfaces)){
    		 for (OpenApiInterface openApiInterface : interfaces) {
    			 OpenInterfaceDto openInterfaceDto = new OpenInterfaceDto();
    			 openInterfaceDto.setId(openApiInterface.getId());
    			 openInterfaceDto.setDescription(openApiInterface.getDescription());
    			 Integer daty = openApiInterface.getDataType();
    			 String dataTypeName = daty == 1 ? "获取基础数据" : daty == 2 ? "获取业务数据" : "推送基础数据";
    			 openInterfaceDto.setDataTypeName(dataTypeName);
    			 openInterfaceDto.setMethodType(openApiInterface.getMethodType());
    			 openInterfaceDto.setUrlString(openApiInterface.getUri());
    			 openInterfaceDto.setState(openApiInterface.getIsUsing() == 1 ? "启用" : "不启用");
    			 openInterfaceDto.setIsUsing(openApiInterface.getIsUsing());
    			 openInterfaceDto.setInterfaceName(openApiInterface.getTypeName());
    			 openInterfaceDto.setType(openApiInterface.getType());
    			 openInterfaceDto.setResultType(openApiInterface.getResultType());
    			 openInterfaceDtos.add(openInterfaceDto);
    		 }
    	}
    	map.put("openInterfaceDtos", openInterfaceDtos);
//    	sendPagination(request, map, page);
        return "/system/interfaceManage/interface/intefaceList.ftl";
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
    		OpenApiInterface openApiInterface = SUtils.dc(openApiInterfaceRemoteService.findOneById(interfaceId), OpenApiInterface.class);
    		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
    		List<OpenApiApply> applys = SUtils.dt(openApiApplyRemoteService.findByTypeAndStatusIn(openApiInterface.getType(),status), OpenApiApply.class);
    		if(CollectionUtils.isEmpty(applys)){
    			openApiInterfaceRemoteService.deleteById(interfaceId);
    		}else{
    			return error("接口正在使用，不能停用！");
    		}
    		//删除关联数据
    		openApiParameterRemoteService.deleteByUri(openApiInterface.getUri());
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
    		OpenApiInterface openApiInterface = SUtils.dc(openApiInterfaceRemoteService.findOneById(interfaceId), OpenApiInterface.class);
    		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
    		List<OpenApiApply> applys = SUtils.dt(openApiApplyRemoteService.findByTypeAndStatusIn(openApiInterface.getType(),status), OpenApiApply.class);
    		if(CollectionUtils.isEmpty(applys) || isUsing == OpenApiInterface.TRUE_IS_USING){
    			openApiInterfaceRemoteService.updateInterfaceById(isUsing,interfaceId);
    		}else{
    			return error("接口正在使用，不能停用！");
    		}
		} catch (Exception e) {
			return error("启用接口失败！"+e.getMessage());
		}
    	return success(OpenApiInterface.TRUE_IS_USING == isUsing ?"启用接口成功" : "停用接口成功");
    }
    
    /**
     * 修改参数
     * @param paramId
     * @return
     */
    @RequestMapping("/editInterface")
    @ControllerInfo("查找参数")
    public String findParam(String interfaceId,ModelMap map) {
    	if(StringUtils.isNotBlank(interfaceId)){
    		OpenApiInterface openApiInterface = SUtils.dc(openApiInterfaceRemoteService.findOneById(interfaceId), OpenApiInterface.class);
    		map.put("interface", openApiInterface);
    	}
		return "/system/interfaceManage/interface/intefaceEdit.ftl";
    }
    /**
     * 更新interface
     * @param interface
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateInterface")
    @ControllerInfo("超管更新接口")
    public String updateInterface(OpenApiInterface inter) {
    	try {
    		openApiInterfaceRemoteService.save(inter);
		} catch (Exception e) {
			return error("更新接口失败！"+e.getMessage());
		}
    	return success("更新接口成功");
    }
    
    /**
     * 保存参数
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveInterface")
    @ControllerInfo("超管保存参数")
    public String saveInterface(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		String interfaceId = jsonObject.getString("interfaceId");
    		String oldUri = null;
        	if(StringUtils.isBlank(interfaceId)){
        		interfaceId = UuidUtils.generateUuid();
        	}else{
        		OpenApiInterface openApiInterface = SUtils.dc(openApiInterfaceRemoteService.findOneById(interfaceId), OpenApiInterface.class);
        		oldUri = openApiInterface.getUri();
        	}
    		openApiInterfaceRemoteService.save(getApiInterface(jsonObject));
    		//修改地址需要更新参数的uri
        	if(StringUtils.isNotBlank(jsonObject.getString("interfaceId"))){
        		String newUri = jsonObject.getString("uri");
        		if(!oldUri.equals(newUri)){
        			openApiParameterRemoteService.updateUriByOlduri(newUri,oldUri);
        		}
        	}
		} catch (Exception e) {
			return error("保存参数失败！"+e.getMessage());
		}
        return success("保存参数成功");
    }
    
    private OpenApiInterface getApiInterface(JSONObject jsonObject){
    	OpenApiInterface openApiInterface =new OpenApiInterface();
    	String interfaceId = jsonObject.getString("interfaceId");
    	if(StringUtils.isBlank(interfaceId)){
    		interfaceId = UuidUtils.generateUuid();
    	}
    	openApiInterface.setId(interfaceId);
    	openApiInterface.setDataType(Integer.valueOf(jsonObject.getString("dataType")));
    	openApiInterface.setFpkColumnName(jsonObject.getString("fpkColumnName"));
    	String isUsing = jsonObject.getString("isUsing");
    	if(StringUtils.isBlank(isUsing)){
    		isUsing = "1";
    	}
    	openApiInterface.setIsUsing(Integer.valueOf(isUsing));
    	openApiInterface.setMethodType(jsonObject.getString("methodType"));
    	openApiInterface.setResultType(jsonObject.getString("resultType"));
    	openApiInterface.setTableName(jsonObject.getString("tableName"));
    	openApiInterface.setType(jsonObject.getString("type"));
    	openApiInterface.setTypeName(jsonObject.getString("typeName"));
    	String unitColumnName = jsonObject.getString("unitColumnName");
    	if (!StringUtils.startsWithIgnoreCase(unitColumnName, "in_")) {
    		unitColumnName = "in_" + unitColumnName;
    	}
    	openApiInterface.setUnitColumnName(unitColumnName);
    	openApiInterface.setUri(jsonObject.getString("uri"));
    	openApiInterface.setDescription(jsonObject.getString("description"));
    	return  openApiInterface;
    }
    
    /**
     * 得到数据类型
     * @param dataType
     * @return
     */
	private String getDataTypeName(Integer dataType) {
		String  name = null;
		if(1 == dataType){
			name = "基础";
		}
		if(2 == dataType){
			name = "业务";
		}
		if(3 == dataType){
			name = "推送";
		}
		return name;
	}
}
