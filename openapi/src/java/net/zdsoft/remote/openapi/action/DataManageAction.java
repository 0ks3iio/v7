/* 
 * @(#)DataManageAction.java    Created on 2017-2-23
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.entity.Parameter;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.service.EntityTicketService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceService;
import net.zdsoft.remote.openapi.service.ParamService;
import net.zdsoft.system.dto.interfaceManage.OpenInterfaceDto;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-23 下午03:29:06 $
 */
@Controller
@RequestMapping("/data/manage")
public class DataManageAction extends OpenApiBaseAction {
    @Resource
    private OpenApiInterfaceService openApiInterfaceService;
    @Resource
    private ParamService paramService;
    @Resource
    private McodeRemoteService mcodeRemoteService;
    @Resource
    private OpenApiApplyService openApiApplyService;
    @Autowired
    private OpenApiEntityService openApiEntityService;
    @Autowired
    private EntityTicketService entityTicketService;

    /**
     * 进入数据管理模块
     * 
     * @author chicb
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
            types = openApiApplyService.getTypes(ApplyStatusEnum.PASS_VERIFY.getValue(), developer.getId());
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
    		List<String[]> interfaceTypes = getResultTypes(Integer.valueOf(dataType));
    		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.UNPASS_VERIFY.getValue()};
    		List<String> types = openApiApplyService.findByDeveloperIdAndStatusIn(developer.getId(),status);
    		boolean isNull = CollectionUtils.isEmpty(types);
    		List<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
    		for(Object[] type : interfaceTypes) {
    			if(isNull || !types.contains(String.valueOf(type[0]))){
    				 OpenInterfaceDto dto = new OpenInterfaceDto();
    				 dto.setType(String.valueOf(type[0]));
                	 dto.setInterfaceName(String.valueOf(type[1]));
                	 openInterfaceDtos.add(dto);
    			}
           }
    	   if(OpenApiConstants.INTERFACE_DATA_TYPE_3 == Integer.valueOf(dataType) ){
    		   map.put("isPushInte", Boolean.TRUE);  
    	   }
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
    		interfaces = openApiInterfaceService.findByResultTypAndDataType(type, Integer.valueOf(dataType));
    	}else{
    	    interfaces = openApiInterfaceService.getByResultType(type);
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
    public String queryParam(String uri, int applyStatus, String type, HttpSession httpSession) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 接口的传参
        List<Parameter> params = paramService.getParams(uri);
        List<String> mcodeIds = new ArrayList<String>();
        for (Parameter param : params) {
            if (null != param.getMcodeId()) {
                mcodeIds.add(param.getMcodeId());
            }
        }
        // 查字典
        if (CollectionUtils.isNotEmpty(mcodeIds)) {
            Map<String, List<McodeDetail>> mcodeMap = new HashMap<String, List<McodeDetail>>();
            for (String mcodeId : mcodeIds) {
                List<McodeDetail> mcodes = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodeId),
                        new TypeReference<List<McodeDetail>>() {
                        });
                mcodeMap.put(mcodeId, mcodes);
            }
            map.put("mcodes", mcodeMap);
        }
        
        //判断是否显示获取 按钮
        OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
        if(OpenApiInterface.PUSH_DATE_TYPE == oi.getDataType() || OpenApiInterface.UPDATE_DATE_TYPE == oi.getDataType()){
        	map.put("isShowButton", Boolean.FALSE);
        }else{
        	map.put("isShowButton", Boolean.TRUE);
        }
        // 接口获得的参数
        List<OpenApiEntity> entitys = openApiEntityService.getEntityParams(applyStatus, type,
                null == getDeveloper() ? null : getDeveloper().getTicketKey());
        map.put("params", params);
        map.put("entitys", entitys);
        return SUtils.s(map);
    }

    
    /**
     * 接口申请
     * @param entityIds
     * @return
     */
    @ResponseBody
    @RequestMapping("/apply")
    public String applyInte(@RequestParam(value = "entityIds[]",defaultValue = "new String[]") String[] entityIds) {
    	Developer developer = getDeveloper();
    	List<OpenApiEntity> enList = openApiEntityService.findListByIdIn(entityIds);
    	Map<String, List<OpenApiEntity>> typeMap  = EntityUtils.getListMap(enList, OpenApiEntity::getType, Function.identity());
    	Set<String> types1 = EntityUtils.getSet(enList, OpenApiEntity::getType);
    	String[] types =  types1.toArray(new String[types1.size()]);
    	 // 获取所有申请接口
        List<OpenApiApply> openApplys = openApiApplyService.getApplys(developer.getId());
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
    			apply.setDeveloperId(developer.getId());
    			apply.setCreationTime(date);
    			apply.setId(UuidUtils.generateUuid());
    			apply.setStatus(ApplyStatusEnum.IN_VERIFY.getValue());
    			apply.setType(type);
    			apply.setMaxNumDay(OpenApiApply.DEFAULT_MAX_NUM_EVERYDAY);
    			apply.setLimitEveryTime(OpenApiApply.DEFAULT_LIMIT_EVERY_TIME);
    			saveApiApplies.add(apply);
    		}
    	}
    	entityTicketService.deleteByTypeInAndTicketKey(types, developer.getTicketKey());
        if(CollectionUtils.isNotEmpty(saveApiApplies)){
        	openApiApplyService.saveAll(saveApiApplies);
        }
    	//保存申请的属性字段
    	doSaveEntityTicket(developer, typeMap, types);
    	return success("申请成功");
    }

    // ----------------------------------------------------私有方法区--------------------
    /**
     * 得到最后需要审核的类型
     * @param types
     * @param developer
     * @return
     */
	private String[] getEndApplyType(String[] types, Developer developer) {
		int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.UNPASS_VERIFY.getValue()};
		List<String> typeList = openApiApplyService.findByDeveloperIdAndStatusIn(developer.getId(),status);
        if(CollectionUtils.isNotEmpty(typeList)){
        	List<String> tyList = Arrays.asList(types);
        	tyList.removeAll(typeList);
//        	if(CollectionUtils.isEmpty(tyList)){
//        		return error("接口已经审核，不能再次申请！");
//        	}
        	types = tyList.toArray(new String[tyList.size()]);
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
			List<EntityTicket> ticketKeys = new ArrayList<>();
	    	for (String type : types) {
	    		List<OpenApiEntity> enList = typeMap.get(type);
	    		if(CollectionUtils.isNotEmpty(enList)){
	    			for (OpenApiEntity openApiEntity : enList) {
	    				EntityTicket et = new EntityTicket();
	    				et.setId(UuidUtils.generateUuid());
	    				et.setType(type);
	    				et.setTicketKey(developer.getTicketKey());
	    				et.setEntityColumnName(openApiEntity.getEntityColumnName());
	    				et.setIsSensitive(openApiEntity.getIsSensitive());
	    				ticketKeys.add(et);
	    			}
	    		}
			}
	        entityTicketService.saveAll(ticketKeys.toArray(new EntityTicket[ticketKeys.size()]));
    }
}
