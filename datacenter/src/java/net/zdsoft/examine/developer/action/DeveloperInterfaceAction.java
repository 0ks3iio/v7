package net.zdsoft.examine.developer.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.base.dto.EntityDto;
import net.zdsoft.base.dto.InterfaceDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiEntity;
import net.zdsoft.base.entity.eis.OpenApiEntityTicket;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.base.service.OpenApiEntityTicketService;
import net.zdsoft.base.service.OpenApiInterfaceCountService;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiInterfaceTypeService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.openapi.remote.openapi.action.OpenApiBaseAction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/datacenter/examine/developer")
public class DeveloperInterfaceAction extends OpenApiBaseAction{

	@Autowired
    @Lazy
    private DeveloperService developerService;
    @Autowired
	private OpenApiInterfaceCountService openApiInterfaceCountService;
    @Autowired
    private OpenApiEntityTicketService openApiEntityTicketService;
    @Autowired
    private OpenApiApplyService openApiApplyService;
    @Autowired
    private OpenApiInterfaceTypeService openApiInterfaceTypeService;
    @Autowired
    private OpenApiInterfaceService openApiInterfaceService;
    @Autowired
    private OpenApiEntityService openApiEntityService;

    @RequestMapping("/interface/index")
    public String developerManageIndex(ModelMap map, String developerId) {
        Developer developer = developerService.findOne(developerId);
        map.put("developerDto", developer);
        return "/examine/developer/developerInterface.ftl";
    }
    /**
     * 开发者详细信息
     * 
     * @author chicb
     * @param map
     * @param ticketKey
     * @param developerId
     * @return
     */
    @RequestMapping("/interface")
    public String findInterface(ModelMap map, String activeId, String state,String developerDto,String developerId) {
    	List<InterfaceDto> interfaceDtos = getInterfaceDtos(developerId,Integer.valueOf(state));
        map.put("activeId", activeId);
        map.put("interFaceList", interfaceDtos);
        return "/examine/developer/developerInterfaceList.ftl";
    }
	/**
     * 接口申请通过审核
     */
    @ResponseBody
    @RequestMapping("/passApply")
    public String passApply(String passInterfaceDtos, String ticketKey) {
    	List<InterfaceDto> allInterfaceDtos = JSON.parseArray(passInterfaceDtos, InterfaceDto.class);
    	openApiApplyService.updateApplyInterface(ticketKey, allInterfaceDtos);
        return returnSuccess();
    }

    /**
     * 接口申请不通过审核
     * @param types
     * @param ticketKey
     * @return
     */
    @ResponseBody
    @RequestMapping("/unpassApply")
    public String unpassApply(String types, String ticketKey) {
        openApiApplyService.updateApplyInterface(types.split(","), ticketKey, ApplyStatusEnum.UNPASS_VERIFY.getValue());
        //删除base_openapi_entity_ticketkey
//        openApiEntityTicketService.deleteByTypeInAndTicketKey(types.split(","),ticketKey);
        return returnSuccess();
    }
    
    /**
     * 删除已订阅接口
     * @param type
     * @param ticketKey
     * @return
     */
    @ResponseBody
    @RequestMapping("/delInterface")
    public String delInterface(String type, String ticketKey) {
        openApiApplyService.deleteByTicketAndTypeIn(ticketKey,type.split(","));
        return returnSuccess();
    }


    /**
     * 修改限制条件
     * @param developerId type
     * @return
     */
    @RequestMapping("/editInterfaceLimit")
    @ControllerInfo("修改接口限制条件")
    public String editInterface(String ticketKey,String type,ModelMap map) {
    	List<OpenApiApply> openApiApplies = openApiApplyService.findByTicketKeyAndTypeIn(ticketKey, type);
    	map.put("openApiApplie", openApiApplies.get(0));
		return "/examine/developer/openApiApply/openApiApplyEdit.ftl";
    }
    
    /**
     * 保存类型
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveInterfaceLimit")
    @ControllerInfo("保存接口限制条件")
    public String saveParam(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		String openApiId = jsonObject.getString("openApiId");
    		OpenApiApply openApiApply = openApiApplyService.findOne(openApiId);
    		openApiApply.setMaxNumDay(Integer.parseInt(jsonObject.getString("maxNumDay")));
    		openApiApply.setLimitEveryTime(Integer.parseInt(jsonObject.getString("limitEveryTime")));
    		openApiApplyService.save(openApiApply);
		} catch (Exception e) {
			return error("保存接口限制条件失败！"+e.getMessage());
		}
        return success("保存接口限制条件成功");
    }
    
    /**
     * 查询审核中的接口即可或的敏感字段信息
     * 
     * @author chicb
     * @return
     */
    @RequestMapping("/checkInVerify")
    public String checkInVerify(String developerId,String type, ModelMap map) {
    	List<InterfaceDto> dtos = new ArrayList<>();
    	if(StringUtils.isNotBlank(type)) {
    		dtos = checkInVerify(developerId,type.split(","));
    	}else {
    		dtos = checkInVerify(developerId,StringUtils.EMPTY);
    	}
        map.put("interfaceDtos", dtos);
		return "/examine/developer/interface/checkParam.ftl";
    }

    public List<InterfaceDto> checkInVerify(String developerId,String... type) {
    	List<String> types = new ArrayList<>();
    	if(type != null) {
    		types.addAll(Arrays.asList(type));
    	}else {
    		types = openApiApplyService.getTypes(ApplyStatusEnum.IN_VERIFY.getValue(), developerId);
    	}
    	// 开发者基本信息
        Developer developer = developerService.findOne(developerId);
        Map<String, List<OpenApiEntity>> tyMap = getTypeMap(types, developer.getTicketKey());
        return convertInterfaceDtos(tyMap);
    }
    
    //-----------------------------------------私有方法区 -----------------------------------------
    
    private List<InterfaceDto> getInterfaceDtos(String developerId, int state) {
    	List<InterfaceDto> interfaceDtos = new ArrayList<>();
    	// 开发者基本信息
        Developer one = developerService.findOne(developerId);
        // 申请的接口状态信息
        List<OpenApiApply> applys = openApiApplyService.findByTicketKeyAndStatus(one.getTicketKey(),state);
        if (CollectionUtils.isEmpty(applys)) {
            return null;
        }
        // 接口状态和对应的名称
        Set<String> type = EntityUtils.getSet(applys, OpenApiApply::getType);
        List<OpenApiEntityTicket> allEntityTickets = openApiEntityTicketService.findByTicketKeyAndTypeIn(one.getTicketKey(),type.toArray(new String[type.size()]));
        Map<String, List<String>> typeEntityIdMap = EntityUtils.getListMap(allEntityTickets, OpenApiEntityTicket::getType, OpenApiEntityTicket::getEntityId);
        Set<String> entitySet = EntityUtils.getSet(allEntityTickets, OpenApiEntityTicket::getEntityId);
        List<OpenApiEntity> allEntities = openApiEntityService.findListByIdIn(entitySet.toArray(new String[entitySet.size()]));
        Map<String, OpenApiEntity> idMap = EntityUtils.getMap(allEntities, OpenApiEntity::getId);
        Map<String, List<OpenApiEntity>> typeMap = new HashMap<>();
        for (Entry<String, List<String>> entry : typeEntityIdMap.entrySet()) { 
        	List<String> enList =  entry.getValue();
        	List<OpenApiEntity> entities = new ArrayList<OpenApiEntity>();
        	enList.forEach(c->{
        		entities.add(idMap.get(c));
        	});
        	typeMap.put(entry.getKey(), entities);
        }
        // 已订阅的接口名称
        Map<String, String> allTypeMap =  getAllTypeNameMap();
        for (OpenApiApply openApiApply : applys) {
        	InterfaceDto dto = new InterfaceDto();
        	String type1 = openApiApply.getType();
        	dto.setType(type1);
        	dto.setTypeName(allTypeMap.get(type1));
        	dto.setApplyId(openApiApply.getId());
        	dto.setLimitEveryTime(openApiApply.getLimitEveryTime());
        	dto.setMaxNumDay(openApiApply.getMaxNumDay());
        	dto.setId(openApiApply.getInterfaceId());
        	dto.setEntitys(convertEntityDto(typeMap.get(type1)));
        	interfaceDtos.add(dto);
		}
		return interfaceDtos;
	}
    
    /**
	 * 得到所有的接口类型名称 根据 type-->key typeName -->value
	 * @return
	 */
	private Map<String, String> getAllTypeNameMap() {
		Integer[] types = {OpenApiInterfaceType.INTERFACE_TYPE,OpenApiInterfaceType.PUBLIC_TYPE};
		List<OpenApiInterfaceType>  interfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
		return EntityUtils.getMap(interfaceTypes, OpenApiInterfaceType::getType, OpenApiInterfaceType::getTypeName);
	}
	
	private List<EntityDto> convertEntityDto(List<OpenApiEntity> entities) {
	    	List<EntityDto> entityDtos = new ArrayList<>();
	    	for (OpenApiEntity openApiEntity : entities) {
	    		EntityDto dto = new EntityDto();
	    		dto.setColumnName(openApiEntity.getEntityColumnName());
	    		dto.setDisplayName(openApiEntity.getDisplayName());
	    		dto.setEntityId(openApiEntity.getId());
	    		entityDtos.add(dto);
			}
	    	
			return entityDtos;
	}
	
	 /**
	 *得到 type --list<OpenApiEntity>
     * @author yangsj
     * @param types
     * @param ticketKey
     * @return
     */
    private Map<String, List<OpenApiEntity>> getTypeMap(List<String> types, String ticketKey) {
    	Map<String, List<OpenApiEntity>> tyMap = new HashMap<>();
		List<OpenApiEntityTicket> entityTickets =  openApiEntityTicketService.findByTicketKeyAndTypeIn(ticketKey, types.toArray(new String[types.size()]));
		Map<String, List<String>> typeMap = EntityUtils.getListMap(entityTickets, OpenApiEntityTicket::getType, OpenApiEntityTicket::getEntityId);
		for (Entry<String, List<String>> entry : typeMap.entrySet()) {
			List<String> entitySet = entry.getValue();
			List<OpenApiEntity> entitys = openApiEntityService.findListByIdIn(entitySet.toArray(new String[entitySet.size()]));
			tyMap.put(entry.getKey(), entitys);
		}
		return tyMap;
	}
    
    private List<InterfaceDto> convertInterfaceDtos(Map<String, List<OpenApiEntity>> typeMap) {
   	    List<InterfaceDto> dtos = new ArrayList<InterfaceDto>();
        if (MapUtils.isEmpty(typeMap)) {
            return null;
        }
        Map<String, String> typeNameMap = getAllTypeNameMap();
        for (Entry<String, List<OpenApiEntity>> entry : typeMap.entrySet()) {
        	String t = entry.getKey();
        	InterfaceDto dto = new InterfaceDto();
        	dto.setType(t);
            dto.setTypeName(typeNameMap.get(t));
            List<OpenApiEntity> list = typeMap.get(t);
            dto.setEntitys(convertEntityDto(list));
            dto.setMaxNumDay(OpenApiApply.DEFAULT_MAX_NUM_EVERYDAY);
            dto.setLimitEveryTime(OpenApiApply.DEFAULT_LIMIT_EVERY_TIME);
            dtos.add(dto);
        }
        return dtos;
    }
    
    private List<InterfaceDto> convertInterfaceDtos(List<String> types) {
        List<InterfaceDto> dtos = new ArrayList<InterfaceDto>();
        Map<String, String> typeNameMap = getAllTypeNameMap();
        for (String type : types) {
            InterfaceDto dto = new InterfaceDto();
            dto.setType(type);
            dto.setTypeName(typeNameMap.get(type));
            dto.setMaxNumDay(OpenApiApply.DEFAULT_MAX_NUM_EVERYDAY);
            dto.setLimitEveryTime(OpenApiApply.DEFAULT_LIMIT_EVERY_TIME);
            dtos.add(dto);
        }
        return dtos;
    }
}
