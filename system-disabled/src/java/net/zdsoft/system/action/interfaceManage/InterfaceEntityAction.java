package net.zdsoft.system.action.interfaceManage;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityRemoteService;
import net.zdsoft.system.dto.interfaceManage.OpenEntityDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = "/system/interface/entity")
public class InterfaceEntityAction extends BaseAction{

	@Autowired 
	private OpenApiEntityRemoteService openApiEntityRemoteService;
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map,String type) {
		map.put("type", type);
        return "/system/interfaceManage/entity/entityIndex.ftl";
    }
	
	@RequestMapping("/showEntityList")
    public String showEntityList(ModelMap map,String type) {
    	List<OpenEntityDto> openEntityDtos = new ArrayList<>();
    	if(StringUtils.isNotBlank(type)){
    		List<OpenApiEntity> openApiEntities = SUtils.dt(openApiEntityRemoteService.findByTypeAll(type), OpenApiEntity.class);
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
        return "/system/interfaceManage/entity/entityList.ftl";
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
    		OpenApiEntity openApiEntity = SUtils.dc(openApiEntityRemoteService.findOneById(entityId), OpenApiEntity.class);
    		map.put("entity", openApiEntity);
    		map.put("type", openApiEntity.getType());
    	}
    	if(StringUtils.isNotBlank(type)){
    		map.put("type", type);
    	}
		return "/system/interfaceManage/entity/entityEdit.ftl";
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
    		openApiEntityRemoteService.deleteById(entityId);
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
    		openApiEntityRemoteService.updateEntityById(isUsing,entityId);
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
    		openApiEntityRemoteService.save(getApiEntity(jsonObject));
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
    	openApiEntity.setDisplayName(jsonObject.getString("displayName"));
    	openApiEntity.setMcodeId(jsonObject.getString("mcodeId"));
    	openApiEntity.setEntityComment(jsonObject.getString("entityComment"));
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
    	return  openApiEntity;
    }
}
