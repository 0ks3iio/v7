package net.zdsoft.examine.inte.action.interfacemanage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.base.dto.OpenEntityDto;
import net.zdsoft.base.entity.eis.OpenApiEntity;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = "/datacenter/examine/interface/entity")
public class InterfaceEntityAction extends InterfaceBaseAction{

	@Autowired 
	private OpenApiEntityService openApiEntityService;
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map,String type) {
		map.put("type", type);
        return "/examine/interfaceManage/entity/entityIndex.ftl";
    }
	
    /**
	 * 类型管理
	 */
	@RequestMapping("/showResultTypeList")
    public String showIntefaceTypes(HttpServletRequest request, ModelMap map) {
		List<OpenApiInterfaceType> openApiInterfaceTypes = getResultTypes();
    	map.put("openInterfaceDtos", openApiInterfaceTypes);
        return "/examine/interfaceManage/entity/resultTypeList.ftl";
    }

	/**
	  * 清除类型，会删除这个类型下面的所有属性和 调用属性的开发者
	  * @param id
	  * @return
	  */
	 @ResponseBody
	 @RequestMapping("/delEntityByType")
	 @ControllerInfo("清空类型")
	 public String delType(String type) {
	 	try {
		 		if(isNotExistOpenApiApply(type)){
		 			openApiEntityService.deleteByType(type);
		 		}else{
		 			return error("属性正在使用，不能清空！");
		 		}
			} catch (Exception e) {
				return error("清空类型属性失败！"+e.getMessage());
			}
	     return success("清空类型属性成功");
	 }
	
	@RequestMapping("/showEntityList")
    public String showEntityList(ModelMap map,String type) {
    	List<OpenEntityDto> openEntityDtos = new ArrayList<>();
    	if(StringUtils.isNotBlank(type)){
    		List<OpenApiEntity> openApiEntities = openApiEntityService.findByTypeAll(type);
    		for (OpenApiEntity entity : openApiEntities) {
    			OpenEntityDto openEntityDto = new OpenEntityDto();
    			openEntityDto.setId(entity.getId());
    			openEntityDto.setDisplayName(entity.getDisplayName());
    			openEntityDto.setEntityComment(entity.getEntityComment());
    			openEntityDto.setEntityType(entity.getEntityType());
    			openEntityDto.setEntityName(entity.getEntityName());
    			openEntityDto.setIsSensitive(entity.getIsSensitive());
    			openEntityDto.setMandatory(entity.getMandatory());
    			openEntityDto.setIsUsing(entity.getIsUsing());
    			openEntityDto.setMcodeId(entity.getMcodeId());
    			openEntityDto.setType(entity.getType());
    			openEntityDtos.add(openEntityDto);
			}
    	}
        map.put("openEntityDtos", openEntityDtos);
        return "/examine/interfaceManage/entity/entityList.ftl";
    }
	
	/**
     * 修改参数
     * @param entityId  type
     * @return
     */
    @RequestMapping("/editEntity")
    @ControllerInfo("查找属性")
    public String editEntity(String entityId, String type,ModelMap map) {
    	if(StringUtils.isNotBlank(entityId)){
    		OpenApiEntity openApiEntity = openApiEntityService.findOne(entityId);
    		map.put("entity", openApiEntity);
    		map.put("type", openApiEntity.getType());
    	}
    	if(StringUtils.isNotBlank(type)){
    		map.put("type", type);
    	}
		return "/examine/interfaceManage/entity/entityEdit.ftl";
    }
	
	/**
     * 删除接口
     * @param entityId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delEntity")
    @ControllerInfo("删除属性")
    public String delEntity(String entityId) {
    	try {
    		openApiEntityService.delete(entityId);
		} catch (Exception e) {
			return error("删除属性失败！"+e.getMessage());
		}
        return success("删除属性成功");
    }
    
    /**
     * 属性启用和不启用
     * @param entityId isUsing
     * @return
     */
    @ResponseBody
    @RequestMapping("/isUsingEntity")
    @ControllerInfo("启用属性")
    public String isUsingEntity(String entityId, int isUsing) {
    	try {
    		openApiEntityService.updateEntityById(isUsing,entityId);
		} catch (Exception e) {
			return error("启用接口失败！"+e.getMessage());
		}
    	return success(OpenApiEntity.TRUE_IS_USING == isUsing ? "启用属性成功" : "停用属性成功");
    }
    
    /**
     * 保存属性
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveEntity")
    @ControllerInfo("超管保存参数")
    public String saveInterface(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		openApiEntityService.save(getApiEntity(jsonObject));
		} catch (Exception e) {
			return error("保存属性失败！"+e.getMessage());
		}
        return success("保存属性成功");
    }
    
    private OpenApiEntity getApiEntity(JSONObject jsonObject){
    	OpenApiEntity openApiEntity =new OpenApiEntity();
    	String entityId = jsonObject.getString("entityId");
    	if(StringUtils.isBlank(entityId)){
    		entityId = UuidUtils.generateUuid();
    	}
    	openApiEntity.setId(entityId);
    	openApiEntity.setType(jsonObject.getString("type"));
    	openApiEntity.setEntityName(jsonObject.getString("entityName"));
    	openApiEntity.setEntityColumnName(jsonObject.getString("entityColumnName"));
    	openApiEntity.setEntityType(jsonObject.getString("entityType"));
    	openApiEntity.setEntityLength(Integer.valueOf(jsonObject.getString("entityLength")));
    	openApiEntity.setRelationColumn(jsonObject.getString("relationColumn"));
    	openApiEntity.setDisplayName(jsonObject.getString("displayName"));
    	openApiEntity.setMcodeId(jsonObject.getString("mcodeId"));
    	openApiEntity.setEntityComment(jsonObject.getString("entityComment"));
    	openApiEntity.setColumnProp(Integer.valueOf(jsonObject.getString("columnProp")));
    	String mandatory = jsonObject.getString("mandatory");
    	if(StringUtils.isBlank(mandatory)){
    		mandatory = "0";
    	}
    	openApiEntity.setMandatory(Integer.valueOf(mandatory));
    	String isUsing = jsonObject.getString("isUsing");
    	if(StringUtils.isBlank(isUsing)){
    		isUsing = "1";
    	}
    	openApiEntity.setIsUsing(Integer.valueOf(isUsing));
    	String isSensitive = jsonObject.getString("isSensitive");
    	if(StringUtils.isBlank(isSensitive)){
    		isSensitive = "0";
    	}
    	openApiEntity.setIsSensitive(Integer.valueOf(isSensitive));
    	String isDefault = jsonObject.getString("isDefault");
    	if(StringUtils.isBlank(isDefault)){
    		isDefault = "0";
    	}
    	openApiEntity.setIsDefault(Integer.valueOf(isDefault));
    	return  openApiEntity;
    }
}
