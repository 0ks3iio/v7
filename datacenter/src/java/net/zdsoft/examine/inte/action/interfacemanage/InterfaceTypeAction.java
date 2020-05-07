package net.zdsoft.examine.inte.action.interfacemanage;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.base.service.OpenApiInterfaceService;
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
@RequestMapping(value = "/datacenter/examine/interface/type")
public class InterfaceTypeAction extends InterfaceBaseAction{
	
	@Resource
	private OpenApiInterfaceService openApiInterfaceService;
	@Autowired 
	private OpenApiEntityService openApiEntityService;
	
	@RequestMapping("/showIndex")
    public String showIndexManageIndex(ModelMap map) {
        return "/examine/interfaceManage/type/typeIndex.ftl"; 
    }
	/**
     * 类型管理
     */
    @RequestMapping("/showIntefaceTypes")
    public String showInterfaces(HttpServletRequest request,String typeName, Integer classify, ModelMap map) {
        List<OpenApiInterfaceType> interfaceTypes = openApiInterfaceTypeService.getInterfaceTypes(typeName, classify);
    	map.put("openInterfaceTypes", interfaceTypes);
        return "/examine/interfaceManage/type/typeList.ftl";
    }
    /**
     * 修改参数
     * @param typeId
     * @return
     */
    @RequestMapping("/editType")
    @ControllerInfo("查找接口类型")
    public String editEntity(String typeId, ModelMap map) {
    	if(StringUtils.isNotBlank(typeId)){
    		OpenApiInterfaceType openApiInterfaceType = openApiInterfaceTypeService.findOne(typeId);
    		map.put("interfaceType", openApiInterfaceType);
    	}
		return "/examine/interfaceManage/type/typeEdit.ftl";
    }
	
	/**
     * 删除类型 1：清除接口 2：清除属性
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delType")
    @ControllerInfo("删除类型")
    public String delType(String typeId) {
    	try {
    		OpenApiInterfaceType openApiInterfaceType = openApiInterfaceTypeService.findOne(typeId);
    		if(isNotExistOpenApiApply(openApiInterfaceType.getType())){
    			if(isInterfaceType(openApiInterfaceType.getClassify()))
    				openApiInterfaceService.deleteByType(openApiInterfaceType.getType());
    			if(isResultType(openApiInterfaceType.getClassify()))
    				openApiInterfaceService.deleteByResultType(openApiInterfaceType.getType());
    				openApiEntityService.deleteByType(openApiInterfaceType.getType());
    				openApiInterfaceTypeService.delete(typeId);
    		}else{
    			return error("类型正在使用，不能删除！");
    		}
		} catch (Exception e) {
			return error("删除类型失败！"+e.getMessage());
		}
        return success("删除类型成功");
    }
	
	/**
     * 保存类型
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveType")
    @ControllerInfo("保存接口类型")
    public String saveParam(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		OpenApiInterfaceType openApiInterfaceType = getApiType(jsonObject); 
    		String  oldType  = jsonObject.getString("oldType");
    		String  newType  = openApiInterfaceType.getType();
    		boolean isHvingType = false;
    		if(StringUtils.isBlank(oldType) || !oldType.equals(newType)){
    			isHvingType = isExistInterfaceType(openApiInterfaceType);
    		}
    		if(!isHvingType){
    			openApiInterfaceTypeService.save(openApiInterfaceType);
				if(StringUtils.isNotBlank(oldType)) {
					if(isResultType(openApiInterfaceType.getClassify()))
						openApiEntityService.updateType(newType, oldType);
					    openApiInterfaceService.updateResultType(newType, oldType);
					if(isInterfaceType(openApiInterfaceType.getClassify()))
						openApiInterfaceService.updatetTypeNameAndType(openApiInterfaceType.getTypeName(), newType, oldType);
				}
			}else{
				return error("类型已经存在，不能重复添加！");
			}
		} catch (Exception e) {
			return error("保存类型失败！"+e.getMessage());
		}
        return success("保存类型成功");
    }
    
    //-----------------------------------------------------------------私有方法区 -----------------------------------------------
    private OpenApiInterfaceType getApiType(JSONObject jsonObject){
    	OpenApiInterfaceType openApiInterfaceType =new OpenApiInterfaceType();
    	String typeId = jsonObject.getString("typeId");
    	if(StringUtils.isBlank(typeId)){
    		typeId = UuidUtils.generateUuid();
    	}
    	openApiInterfaceType.setId(typeId);
    	openApiInterfaceType.setClassify(Integer.valueOf(jsonObject.getString("classify")));
    	openApiInterfaceType.setType(jsonObject.getString("type"));
    	openApiInterfaceType.setTypeName(jsonObject.getString("typeName"));
    	return  openApiInterfaceType;
    }
    
    /**
     * 是否已经存在类型
     * @param OpenApiInterfaceType
     * @return
     */
	private boolean isExistInterfaceType(OpenApiInterfaceType openApiInterfaceType) {
		List<OpenApiInterfaceType> typeList = openApiInterfaceTypeService.findByTypeAndByClassify(openApiInterfaceType.getType(),
				openApiInterfaceType.getClassify());
		return CollectionUtils.isNotEmpty(typeList);
	}
}
