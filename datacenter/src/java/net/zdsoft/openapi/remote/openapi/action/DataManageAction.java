package net.zdsoft.openapi.remote.openapi.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.http.HttpSession;

import net.zdsoft.base.dto.EntityDto;
import net.zdsoft.base.dto.InterfaceDto;
import net.zdsoft.base.dto.OpenInterfaceDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiEntity;
import net.zdsoft.base.entity.eis.OpenApiEntityTicket;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.entity.eis.OpenApiParameter;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.enums.YesNoEnum;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.base.service.OpenApiEntityTicketService;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiParameterService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/data/manage")
public class DataManageAction extends OpenApiBaseAction {
	@Autowired
    private OpenApiInterfaceService openApiInterfaceService;
    @Autowired
    private OpenApiParameterService paramService;
    @Autowired
    private OpenApiApplyService openApiApplyService;
    @Autowired
    private OpenApiEntityService openApiEntityService;
    @Autowired
    private OpenApiEntityTicketService entityTicketService;

    /**
     * 进入数据管理模块
     * @return
     */
    @RequestMapping("/page")
    public String dataManageShow(@RequestParam(value = "applyStatus", defaultValue = "0") int applyStatus,
    		@RequestParam(value = "dataType", defaultValue = "1") int dataType, ModelMap map) {
        Developer developer = getDeveloper();
        int isLogin = YesNoEnum.NO.getValue();
        List<String> types = null;
        if (null != developer) {
            isLogin = YesNoEnum.YES.getValue();
            types = openApiApplyService.getTypes(ApplyStatusEnum.PASS_VERIFY.getValue(), developer.getTicketKey());
            map.put("developerId", developer.getId());
            map.put("ticketKey", developer.getTicketKey());
        }
        map.put("openApiApplys", getTypes(types,dataType));
        map.put("isLogin", isLogin);
        map.put("dataType", dataType);
        return "/openapi/dataManage/dataManage.ftl";
    }

    /**
     * 申请接口
     * @param dataType
     * @return
     */
    @RequestMapping("/applyInterface")
    public String applyInterface(String dataType, ModelMap map) {
    	Developer developer = getDeveloper();
    	if (null != developer) {
	    	  List<OpenApiInterface> openApiInterfaces = openApiInterfaceService.findByDataType(Integer.valueOf(dataType));
	    	  int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.UNPASS_VERIFY.getValue()};
	  		  List<String> types = openApiApplyService.findByTicketKeyAndStatusIn(developer.getTicketKey(),status);
	  		  boolean isNull = CollectionUtils.isEmpty(types);
	  		  Map<String, List<OpenApiInterface>> interfaceTypeMap = EntityUtils.getListMap(openApiInterfaces, OpenApiInterface::getType, Function.identity());
	  		  List<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
	  		  for (Entry<String, List<OpenApiInterface>> entry : interfaceTypeMap.entrySet()) {
	  			OpenApiInterface openApiInterface = entry.getValue().get(0);
	  			if(isNull || !types.contains(openApiInterface.getType())){
					 OpenInterfaceDto dto = new OpenInterfaceDto();
					 dto.setType(openApiInterface.getType());
		        	 dto.setInterfaceName(openApiInterface.getTypeName());
		        	 dto.setResultType(openApiInterface.getResultType());
		        	 dto.setId(openApiInterface.getId());
		        	 openInterfaceDtos.add(dto);
			    }
	  		  }
//	  		  List<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
//			  for (OpenApiInterface openApiInterface : openApiInterfaces) {
//				  if(isNull || !types.contains(openApiInterface.getType())){
//						 OpenInterfaceDto dto = new OpenInterfaceDto();
//						 dto.setType(openApiInterface.getType());
//			        	 dto.setInterfaceName(openApiInterface.getTypeName());
//			        	 dto.setResultType(openApiInterface.getResultType());
//			        	 dto.setId(openApiInterface.getId());
//			        	 openInterfaceDtos.add(dto);
//				  }
//			  }
    	      map.put("openInterfaceDtos", openInterfaceDtos);
    	}
    	return "/openapi/dataManage/interfaceApply.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/showEntityList")
    public String showEntityList(ModelMap map,String type) {
		List<OpenApiEntity> openApiEntities = openApiEntityService.findByType(type);
		JSONArray jsonArray = new JSONArray();
		for(OpenApiEntity entity:openApiEntities){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id",entity.getId());
			jsonObject.put("type", entity.getType());
			jsonObject.put("entityName", entity.getEntityName());
			jsonObject.put("displayName", entity.getDisplayName());
			jsonObject.put("mandatory", entity.getMandatory() == 0 ? "是" : "否");
			jsonArray.add(jsonObject);
		}
		return jsonArray.toJSONString();
    }
    
    /**
     * 查询接口
     * @author chicb
     * @param type
     * @param dataType
     * @return
     */
    @ResponseBody
    @RequestMapping("/interface")
    public String queryInterface(String type, String dataType) {
    	List<OpenApiInterface> interfaces;
    	if(StringUtils.isNotBlank(dataType)){
    		interfaces = openApiInterfaceService.findByTypAndDataType(type, Integer.valueOf(dataType));
    	}else{
    	    interfaces = openApiInterfaceService.getByType(type);
    	}
        return success(SUtils.s(interfaces));
    }
    
    /**
     * 查询接口详细信息
     * 
     * @author chicb
     * @param uri
     * @param applyStatus
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping("/param")
    public String queryParam(String interfaceId, int applyStatus,HttpSession httpSession) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 接口的传参
        List<OpenApiParameter> params = paramService.findByInterfaceId(interfaceId);
        List<String> mcodeIds = new ArrayList<String>();
        for (OpenApiParameter param : params) {
            if (null != param.getMcodeId()) {
                mcodeIds.add(param.getMcodeId());
            }
        }
        // 查字典
        if (CollectionUtils.isNotEmpty(mcodeIds)) {
//            Map<String, List<McodeDetail>> mcodeMap = new HashMap<String, List<McodeDetail>>();
//            for (String mcodeId : mcodeIds) {
//                List<McodeDetail> mcodes = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodeId),
//                        new TypeReference<List<McodeDetail>>() {
//                        });
//                mcodeMap.put(mcodeId, mcodes);
//            }
            map.put("mcodes", null);
        }
        
        //判断是否显示获取 按钮
        OpenApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
        if(OpenApiInterface.PUSH_DATE_TYPE == openApiInterface.getDataType() || OpenApiInterface.UPDATE_DATE_TYPE == openApiInterface.getDataType()){
        	map.put("isShowButton", Boolean.FALSE);
        }else{
        	map.put("isShowButton", Boolean.TRUE);
        }
        // 接口获得的参数
        List<OpenApiEntity> entitys = openApiEntityService.getEntityParams(applyStatus, openApiInterface.getType(), openApiInterface.getResultType(), 
                null == getDeveloper() ? null : getDeveloper().getTicketKey());
        map.put("entitys", entitys);
        map.put("params", params);
        return SUtils.s(map);
    }

    
    /**
     * 接口申请
     * @param entityIds
     * @return
     */
    @ResponseBody
    @RequestMapping("/apply")
    public String applyInte(String passInterfaceDtos) {
    	List<InterfaceDto> allInterfaceDtos = JSON.parseArray(passInterfaceDtos, InterfaceDto.class);
    	Map<String, List<EntityDto>> typeMap = EntityUtils.getMap(allInterfaceDtos, InterfaceDto::getType, InterfaceDto::getEntitys);
    	Set<String> types = EntityUtils.getSet(allInterfaceDtos, InterfaceDto::getType);
    	
    	Map<String, List<OpenApiEntity>> interfaceTypeMap = new HashMap<String, List<OpenApiEntity>>();
    	for (Entry<String, List<EntityDto>> entry : typeMap.entrySet()) {
    		List<EntityDto> dtos = entry.getValue();
    		Set<String> entitySet = EntityUtils.getSet(dtos, EntityDto::getEntityId);
    		List<OpenApiEntity> enList = openApiEntityService.findListByIdIn(entitySet.toArray(new String[entitySet.size()]));
    		interfaceTypeMap.put(entry.getKey(), enList);
    	}
    	
    	Developer developer = getDeveloper();
    	 // 获取所有申请接口
        List<OpenApiApply> openApplys = openApiApplyService.getApplys(developer.getTicketKey());
        types = getEndApplyType(types, developer);
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	for (OpenApiApply ap : openApplys) {
    		if (ap.getStatus() == ApplyStatusEnum.UNPASS_VERIFY.getValue()) {
    			continue;
    		}
    		map.put(ap.getType(), ap.getStatus());
    	}
    	Date date = new Date();
    	List<OpenApiApply> saveApiApplies = new ArrayList<OpenApiApply>();
    	for (String type : types) {
    		if (null == map.get(type)) {
    			OpenApiApply apply = new OpenApiApply();
    			apply.setTicketKey(developer.getTicketKey());
    			apply.setCreationTime(date);
    			apply.setId(UuidUtils.generateUuid());
    			apply.setStatus(ApplyStatusEnum.IN_VERIFY.getValue());
    			apply.setType(type);
    			apply.setMaxNumDay(OpenApiApply.DEFAULT_MAX_NUM_EVERYDAY);
    			apply.setLimitEveryTime(OpenApiApply.DEFAULT_LIMIT_EVERY_TIME);
    			saveApiApplies.add(apply);
    		}
    	}
    	entityTicketService.deleteByTypeInAndTicketKey(types.toArray(new String[types.size()]), developer.getTicketKey());
        if(CollectionUtils.isNotEmpty(saveApiApplies)){
        	openApiApplyService.saveAll(saveApiApplies);
        }
    	//保存申请的属性字段
    	doSaveEntityTicket(developer, interfaceTypeMap, types.toArray(new String[types.size()]));
    	return success("申请成功");
    }

    // ----------------------------------------------------私有方法区--------------------
    /**
     * 得到最后需要审核的类型
     * @param types
     * @param developer
     * @return
     */
	private Set<String> getEndApplyType(Set<String> types, Developer developer) {
		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.UNPASS_VERIFY.getValue()};
		List<String> typeList = openApiApplyService.findByTicketKeyAndStatusIn(developer.getId(),status);
        if(CollectionUtils.isNotEmpty(typeList)){
        	types.removeAll(typeList);
        }
		return types;
	}

    /**
     * 进行保存开发者申请的属性
     * @param developer
     * @param typeMap
     * @param type
     */
	private void doSaveEntityTicket(Developer developer,
			Map<String, List<OpenApiEntity>> typeMap, String... types) {
			List<OpenApiEntityTicket> ticketKeys = new ArrayList<>();
	    	for (String type : types) {
	    		List<OpenApiEntity> enList = typeMap.get(type);
	    		if(CollectionUtils.isNotEmpty(enList)){
	    			for (OpenApiEntity openApiEntity : enList) {
	    				OpenApiEntityTicket et = new OpenApiEntityTicket();
	    				et.setId(UuidUtils.generateUuid());
	    				et.setType(type);
	    				et.setTicketKey(developer.getTicketKey());
	    				et.setEntityId(openApiEntity.getId());
	    				ticketKeys.add(et);
	    			}
	    		}
			}
	        entityTicketService.saveAll(ticketKeys.toArray(new OpenApiEntityTicket[ticketKeys.size()]));
    }
}
