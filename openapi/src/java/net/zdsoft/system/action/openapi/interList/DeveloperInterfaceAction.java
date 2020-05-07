package net.zdsoft.system.action.openapi.interList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.dto.EntityDto;
import net.zdsoft.remote.openapi.dto.InterfaceDto;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.remote.service.DeveloperRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityTicketRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceCountRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/system/developer/")
public class DeveloperInterfaceAction extends OpenApiBaseAction{

	@Autowired
    @Lazy
    private DeveloperRemoteService developerRemoteService;
    @Autowired
    private ServerRemoteService serverRemoteService;
    @Autowired
	private OpenApiInterfaceCountRemoteService openApiInterfaceCountRemoteService;
    @Autowired
    private OpenApiEntityTicketRemoteService openApiEntityTicketRemoteService;
    @Autowired
    private OpenApiApplyService openApiApplyService;

    /**
     * 开发者详细信息
     * 
     * @author chicb
     * @param map
     * @param ticketKey
     * @param developerId
     * @return
     */
    @RequestMapping("/info")
    public String manageInfo(ModelMap map, String developerId) {
        DeveloperDto developer = developerRemoteService.getDeveloperDto(developerId);
        getInterfaceCountAndEntity(developer);
        List<Server> apps = SUtils.dt(serverRemoteService.getAppsByDevId(developerId), Server.class);
        map.put("apps", apps);
        map.put("developerDto", developer);
        return "/openapi/system/developer/developerInfo.ftl";
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
    public String findInterface(ModelMap map, String activeId,String type,String developerDto,String developerId) {
    	DeveloperDto developer = developerRemoteService.getDeveloperDto(developerId);
    	getInterfaceCountAndEntity(developer);
    	Map<String, List<InterfaceDto>> interfaces = developer.getInterfaces();
        map.put("activeId", activeId);
        List<InterfaceDto> interFaceList = new ArrayList<>();
        if(interfaces != null) {
        	interFaceList= interfaces.get(type);
        }
        map.put("interFaceList", interFaceList);
        return "/openapi/system/developer/developerInterfaceList.ftl";
    }
    
	/**
     * 接口申请通过审核
     */
    @ResponseBody
    @RequestMapping("/passApply")
    public String passApply(String developerDto) {
    	developerDto = StringEscapeUtils.unescapeHtml(developerDto);
        DeveloperDto deserialize = SUtils.deserialize(developerDto, DeveloperDto.class);
        developerRemoteService.passApplyInterface(deserialize);
        return returnSuccess();
    }

    /**
     * 接口申请不通过审核
     * 
     * @author chicb
     * @param types
     * @param developerId
     * @return
     */
    @ResponseBody
    @RequestMapping("/unpassApply")
    public String unpassApply(String types, String developerId) {
        developerRemoteService.modifyApplyInterface(types.split(","), developerId, "",
                ApplyStatusEnum.UNPASS_VERIFY.getValue());
        //删除base_openapi_entity_ticketkey
        DeveloperDto developer = developerRemoteService.getDeveloperDto(developerId);
        openApiEntityTicketRemoteService.deleteByTypeInAndTicketKey(types.split(","),developer.getTicketKey());
        return returnSuccess();
    }
    
    /**
     * 删除已订阅接口
     * 
     * @author chicb
     * @param type
     * @param tickerKey
     * @param developerId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delInterface")
    public String delInterface(String type, String ticketKey, String developerId) {
        developerRemoteService.delInterface(type.split(","), ticketKey, developerId);
        return returnSuccess();
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
    		dtos = developerRemoteService.checkInVerify(developerId,type.split(","));
    	}else {
    		dtos = developerRemoteService.checkInVerify(developerId,StringUtils.EMPTY);
    	}
        map.put("interfaceDtos", dtos);
		return "/openapi/system/developer/interface/checkParam.ftl";
    }


    /**
     * 查看敏感字段
     * 
     * @author chicb
     * @param type
     * @param tickerKey
     * @return
     */
    @RequestMapping("/checkSensitive")
    public String checkSensitive(String type, String ticketKey,int isSensitive, ModelMap map) {
        List<EntityDto> entitys = developerRemoteService.checkEntity(type, ticketKey, isSensitive);
    	map.put("entitys", entitys);
    	map.put("type", type);
    	if(EntityTicket.IS_SENSITIVE_TRUE == isSensitive){
    		map.put("title", "敏感字段");
    	}
    	map.put("isSensitive", isSensitive);
		return "/openapi/system/developer/interface/checkSensitive.ftl";
    }

    /**
     * 修改通过后的字段
     * @author chicb
     * @param type
     * @param ticketKey
     * @param columnNames
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyEntity")
    public String modifyEntity(String type, String ticketKey, @RequestParam("columnNames[]") String[] columnNames,int isSensitive ) {
        developerRemoteService.modifyEntityTicket(type, ticketKey, columnNames,isSensitive);
        return returnSuccess();
    }

    /**
     * 修改限制条件
     * @param developerId type
     * @return
     */
    @RequestMapping("/editInterfaceLimit")
    @ControllerInfo("修改接口限制条件")
    public String editInterface(String developerId,String type,ModelMap map) {
    	List<OpenApiApply> openApiApplies = openApiApplyService.findByDeveloperIdAndTypeIn(developerId, type);
    	map.put("openApiApplie", openApiApplies.get(0));
		return "/openapi/system/developer/openApiApply/openApiApplyEdit.ftl";
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
    
    //-----------------------------------------私有方法区 -----------------------------------------
    
    /**
	 * @param developer
	 */
	private void getInterfaceCountAndEntity(DeveloperDto developer) {
		List<InterfaceDto> passList = new ArrayList<>();
        Map<String, List<InterfaceDto>> interfaces = developer.getInterfaces();
        if (MapUtils.isNotEmpty(interfaces)) {
            List<InterfaceDto> list = interfaces.get(Integer.toString(ApplyStatusEnum.IN_VERIFY.getValue()));
            developer.setInVerifyTypes(toStingType(list));
            passList = interfaces.get(Integer.toString(ApplyStatusEnum.PASS_VERIFY.getValue()));
        }
       //查找每个接口的调用次数
        if(CollectionUtils.isNotEmpty(passList)) {
        	Set<String> passTypes = EntityUtils.getSet(passList, InterfaceDto::getType);
        	Map<String, Integer> typeCountMap = openApiInterfaceCountRemoteService.getTypeCountMap(developer.getTicketKey(),passTypes.toArray(new String[passTypes.size()]));
//        	List<OpenApiInterfaceCount> counts = SUtils.dt(openApiInterfaceCountRemoteService.findByTicketKeyAndTypeIn(
//        			developer.getTicketKey(),passTypes.toArray(new String[passTypes.size()])), OpenApiInterfaceCount.class);
//        	Map<String,List<OpenApiInterfaceCount>> typeCountMap = counts.stream().collect(Collectors.groupingBy(OpenApiInterfaceCount::getType));
            Map<String, List<EntityDto>> entityMap = SUtils.dt(developerRemoteService.findEntityMapByTicketKeyAndTypeIn
            		(developer.getTicketKey(),passTypes.toArray(new String[passTypes.size()])), new TR<Map<String, List<EntityDto>>>() {});
            for (InterfaceDto interfaceDto : passList) {
            	String type = interfaceDto.getType();
				if(typeCountMap == null || typeCountMap.isEmpty() || typeCountMap.get(type) == null) {
					interfaceDto.setCount(0);
				}else {
					interfaceDto.setCount(typeCountMap.get(type));
				}
				if(entityMap != null && !entityMap.isEmpty() && entityMap.get(type) != null) {
					interfaceDto.setEntitys(entityMap.get(type));
				}
			}
        }
	}
	
	/**
     * @author chicb
     * @param list
     * @return
     */
    private String toStingType(List<InterfaceDto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (InterfaceDto dto : list) {
            sb.append(dto.getType()).append(",");
        }
        return sb.substring(0, sb.lastIndexOf(","));
    }
}
