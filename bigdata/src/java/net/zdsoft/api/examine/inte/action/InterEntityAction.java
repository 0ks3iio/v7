package net.zdsoft.api.examine.inte.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.api.base.dto.OpenEntityDto;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.api.base.service.ApiEntityService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/bigdata/api/interEntity")
public class InterEntityAction extends InterBaseAction{

	@Autowired 
	private ApiEntityService openApiEntityService;
	@Autowired
    private MetadataTableColumnService metadataTableColumnService;
	@Resource
	private BigLogService bigLogService;
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map,String type, String metadataId) {
		map.put("metadataId", metadataId);
		map.put("type", type);
        return "/api/examine/interfaceManage/entity/entityIndex.ftl";
    }
	
    /**
	 * 类型管理
	 */
	@RequestMapping("/showResultTypeList")
    public String showIntefaceTypes(HttpServletRequest request, ModelMap map) {
		List<ApiInterfaceType> openApiInterfaceTypes = getResultTypes();
    	map.put("openInterfaceDtos", openApiInterfaceTypes);
        return "/api/examine/interfaceManage/entity/resultTypeList.ftl";
    }

	/**
	  * 清除类型，会删除这个类型下面的所有属性和 调用属性的开发者
	  * @param id
	  * @return
	  */
	 @ResponseBody
	 @RequestMapping("/delEntityByType")
	 @ControllerInfo("清空类型")
	 public Response delType(String type) {
	 	try {
		 		if(isNotExistApplyByType(type)){
		 			openApiEntityService.deleteByType(type);
		 		}else{
		 			return Response.error().message("属性正在使用，不能清空！").build();
		 		}
			} catch (Exception e) {
				return Response.error().message(e.getMessage()).build();
			}
	    	 return Response.ok().build();
	 }
	
	@RequestMapping("/showEntityList")
    public String showEntityList(ModelMap map,String type, String metadataId) {
    	List<OpenEntityDto> openEntityDtos = new ArrayList<>();
    	if(StringUtils.isNotBlank(type)){
    		List<ApiEntity> openApiEntities = openApiEntityService.findByTypeAll(type);
    		for (ApiEntity entity : openApiEntities) {
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
        return "/api/examine/interfaceManage/entity/entityList.ftl";
    }
	
	/**
     * 修改属性
     * @param entityId  type
     * @return
     */
    @RequestMapping("/editEntity")
    @ControllerInfo("修改属性")
    public String editEntity(String entityId, String type,String metadataId, ModelMap map) {
    	if(StringUtils.isNotBlank(entityId) && !"undefined".equalsIgnoreCase(entityId)){
    		ApiEntity openApiEntity = openApiEntityService.findOne(entityId);
    		map.put("entity", openApiEntity);
    		map.put("type", openApiEntity.getType());
    	}
    	if(StringUtils.isNotBlank(type)){
    		map.put("type", type);
    	}
    	List<MetadataTableColumn> columnList = metadataTableColumnService.findByMetadataId(metadataId);
    	map.put("columnList", columnList);
    	map.put("metadataId", metadataId);
		return "/api/examine/interfaceManage/entity/entityEdit.ftl";
    }
	
	/**
     * 删除属性
     * @param entityId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delEntity")
    @ControllerInfo("删除属性")
    public Response delEntity(String entityId) {
    	try {
			ApiEntity apiEntity = openApiEntityService.findOne(entityId);
			openApiEntityService.delete(entityId);
			//业务日志埋点  删除
			LogDto logDto=new LogDto();
			logDto.setBizCode("delete-openApiEntity");
			logDto.setDescription("属性 "+apiEntity.getEntityName());
			logDto.setBizName("数据接口管理");
			logDto.setOldData(apiEntity);
			bigLogService.deleteLog(logDto);
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
    	 return Response.ok().build();
    }
    
    /**
     * 属性启用和不启用
     * @param entityId isUsing
     * @return
     */
    @ResponseBody
    @RequestMapping("/isUsingEntity")
    @ControllerInfo("启用属性")
    public Response isUsingEntity(String entityId, int isUsing) {
    	try {
			ApiEntity entity = openApiEntityService.findOne(entityId);
			openApiEntityService.updateEntityById(isUsing,entityId);
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-isUsingEntity");
			logDto.setDescription("属性"+entity.getEntityName()+"的启用状态");
			logDto.setOldData(entity.getIsUsing());
			logDto.setNewData(isUsing);
			logDto.setBizName("数据接口管理");
			bigLogService.updateLog(logDto);
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
    	 return Response.ok().build();
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
    
    private ApiEntity getApiEntity(JSONObject jsonObject){
    	ApiEntity openApiEntity =new ApiEntity();
    	String entityId = jsonObject.getString("entityId");
    	boolean flag=true;
		ApiEntity oldApiEntity=new ApiEntity();
    	if(StringUtils.isBlank(entityId)){
    		entityId = UuidUtils.generateUuid();
    		flag=false;
    	}else {
			 oldApiEntity = openApiEntityService.findOne(entityId);
		}
    	openApiEntity.setId(entityId);
    	openApiEntity.setType(jsonObject.getString("type"));
    	openApiEntity.setEntityName(jsonObject.getString("entityName"));
    	String columnId = jsonObject.getString("columnId");
    	MetadataTableColumn column = metadataTableColumnService.findOne(columnId);
    	openApiEntity.setEntityColumnName(column.getColumnName());
    	openApiEntity.setEntityType(column.getColumnType());
    	openApiEntity.setDisplayName(column.getName());
    	openApiEntity.setEntityLength(Integer.valueOf(jsonObject.getString("entityLength")));
    	openApiEntity.setRelationColumn(jsonObject.getString("relationColumn"));
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
    	openApiEntity.setMetadataId(jsonObject.getString("metadataId"));
    	if (flag){
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-openApiEntity");
			logDto.setDescription("属性 "+oldApiEntity.getEntityName());
			logDto.setOldData(oldApiEntity);
			logDto.setNewData(openApiEntity);
			logDto.setBizName("数据接口管理");
			bigLogService.updateLog(logDto);
		}else {
			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-openApiEntity");
			logDto.setDescription("属性 "+openApiEntity.getEntityName());
			logDto.setNewData(openApiEntity);
			logDto.setBizName("数据接口管理");
			bigLogService.insertLog(logDto);
		}
    	return  openApiEntity;
    }
}
