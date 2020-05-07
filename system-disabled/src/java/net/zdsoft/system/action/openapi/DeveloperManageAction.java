/* 
 * @(#)DeveloperManageAction.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.openapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.dto.EntityDto;
import net.zdsoft.remote.openapi.dto.InterfaceDto;
import net.zdsoft.remote.openapi.entity.DeveloperPower;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.remote.service.DeveloperPowerRemoteService;
import net.zdsoft.remote.openapi.remote.service.DeveloperRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityTicketRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceCountRemoteService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.service.server.ServerService;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午03:35:21 $
 */
@Controller
@RequestMapping("/system/developer/")
public class DeveloperManageAction extends BaseAction {
    @Autowired
    @Lazy
    private DeveloperRemoteService developerRemoteService;
    @Autowired
    private ServerService serverService;
    @Autowired
	private OpenApiInterfaceCountRemoteService openApiInterfaceCountRemoteService;
    @Autowired
    private DeveloperPowerRemoteService developerPowerRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private OpenApiEntityTicketRemoteService openApiEntityTicketRemoteService;

    @RequestMapping("/index")
    public String developerManageIndex(ModelMap map) {
        String url = "/system/developer/manage";
        map.put("url", url);
        return "/system/common/home.ftl";
    }

    
    /**
     * 开发者管理
     * 
     * @author chicb
     * @param map
     * @return
     */
    @RequestMapping("/manage")
    public String shwoManage(ModelMap map) {
        List<DeveloperDto> developerDtos = developerRemoteService.getDevelperSimpleInfos();
        if (CollectionUtils.isNotEmpty(developerDtos)) {
            List<String> devIds = EntityUtils.getList(developerDtos, DeveloperDto::getId);
            Map<String, List<String>> appsNumByDevIds = serverService.getAppNamesByDevIds(devIds
                    .toArray(new String[devIds.size()]));
            addServerNumToDeveloperDto(developerDtos, appsNumByDevIds);
            map.put("developerDtos", developerDtos);
        }
        return "/system/developer/developerManage.ftl";
    }

    /**
     * 重置密码
     * 
     * @author chicb
     * @return
     */
    @ResponseBody
    @RequestMapping("/defaultPw")
    public String defaultPw(String developerId) {
        if (YesNoEnum.YES.getValue() == developerRemoteService.defaultPw(developerId)) {
            return success("success");
        }
        return error("error");
    }

    @ResponseBody
    @RequestMapping("/modifyUnitName")
    public String modifyDeveloperUnitName(String developerId, String unitName) {
        if (!Validators.isName(unitName, 4, 60)) {
            return error("2-30个中文字符");
        }
        if (1 == developerRemoteService.modifyDeveloperUnitName(developerId, unitName)) {
            return returnSuccess();
        }
        return error("服务开小差，请刷新后重试...");
    }

    @ResponseBody
    @RequestMapping("/modifyIps")
    public String modifyDeveloperIps(String developerId, String ips) {
        if (1 == developerRemoteService.modifyDeveloperIps(developerId, ips)) {
            return returnSuccess();
        }
        return error("服务开小差，请刷新后重试...");
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
    @RequestMapping("/info")
    public String manageInfo(ModelMap map, String developerId) {
        DeveloperDto developer = developerRemoteService.getDeveloperDto(developerId);
        getInterfaceCount(developer);
        List<Server> apps = serverService.getAppsByDevId(developerId);
        map.put("apps", apps);
        map.put("developerDto", developer);
        return "/system/developer/developerInfo.ftl";
    }


	/**
	 * @param developer
	 */
	private void getInterfaceCount(DeveloperDto developer) {
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
        	List<OpenApiInterfaceCount> counts = SUtils.dt(openApiInterfaceCountRemoteService.findByTicketKeyAndTypeIn(
        			developer.getTicketKey(),passTypes.toArray(new String[passTypes.size()])), OpenApiInterfaceCount.class);
        	Map<String,List<OpenApiInterfaceCount>> typeCountMap = counts.stream().collect(Collectors.groupingBy(OpenApiInterfaceCount::getType));
            for (InterfaceDto interfaceDto : passList) {
            	String type = interfaceDto.getType();
				if(typeCountMap == null || typeCountMap.isEmpty() || typeCountMap.get(type) == null) {
					interfaceDto.setCount(0);
				}else {
					interfaceDto.setCount(typeCountMap.get(type).size());
				}
			}
        }
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
    	getInterfaceCount(developer);
    	Map<String, List<InterfaceDto>> interfaces = developer.getInterfaces();
        map.put("activeId", activeId);
        List<InterfaceDto> interFaceList = new ArrayList<>();
        if(interfaces != null) {
        	interFaceList= interfaces.get(type);
        }
        map.put("interFaceList", interFaceList);
        return "/system/developer/developerInterfaceList.ftl";
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
    @ResponseBody
    @RequestMapping("/checkInVerify")
    public String checkInVerify(String developerId,String type) {
    	List<InterfaceDto> dtos = new ArrayList<>();
    	if(StringUtils.isNotBlank(type)) {
    		dtos = developerRemoteService.checkInVerify(developerId,type.split(","));
    	}else {
    		dtos = developerRemoteService.checkInVerify(developerId,StringUtils.EMPTY);
    	}
        if (CollectionUtils.isEmpty(dtos)) {
            return returnSuccess();
        }
        return SUtils.s(dtos);
    }

    /**
     * 接口申请通过审核
     */
    @ResponseBody
    @RequestMapping("/passApply")
    public String passApply(String developerDto) {
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
//        entityTicketDao.deleteEntityTicket(type, ticketKey);
        return returnSuccess();
    }

    /**
     * 查看敏感字段
     * 
     * @author chicb
     * @param type
     * @param tickerKey
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkSensitive")
    public String checkSensitive(String type, String ticketKey) {
        List<EntityDto> entitys = developerRemoteService.checkSensitive(type, ticketKey);
        if (CollectionUtils.isEmpty(entitys)) {
            return success("无敏感字段需要维护");
        }
        return SUtils.s(entitys);
    }

    /**
     * 修改敏感字段
     * 
     * @author chicb
     * @param type
     * @param ticketKey
     * @param columnNames
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifySensitive")
    public String modifySensitive(String type, String ticketKey, @RequestParam("columnNames[]") String[] columnNames) {
        developerRemoteService.modifySensitive(type, ticketKey, columnNames);
        return returnSuccess();
    }

    /**
     * @author chicb
     * @param developerDtos
     * @param appsNumByDevIds
     */
    private void addServerNumToDeveloperDto(List<DeveloperDto> developerDtos, Map<String, List<String>> appsNumByDevIds) {
        if (MapUtils.isEmpty(appsNumByDevIds)) {
            return;
        }
        for (DeveloperDto d : developerDtos) {
            List<String> list = appsNumByDevIds.get(d.getId());
            d.setServerNum(0);
            if (CollectionUtils.isNotEmpty(list)) {
                d.setServerNum(list.size());
                d.setServerNames(StringUtils.join(list, "<br>"));
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
    
    @ResponseBody
	@RequestMapping("/empower/addUnit")
	@ControllerInfo("单位授权")
	public String empowerAdd(String unitIds,String developerId){
		try {
			List<String> addUids = new ArrayList<>();
			if(StringUtils.isNotBlank(unitIds) && StringUtils.isNotBlank(developerId)) {
				List<DeveloperPower> developerPowers = SUtils.dt(developerPowerRemoteService.findByDeveloperIdAndUnitIdIn(developerId,unitIds.split(",")),
						DeveloperPower.class);
				List<String> uids = EntityUtils.getList(developerPowers, DeveloperPower::getUnitId);
				String[] aStrings = unitIds.split(",");
				for (String uid : aStrings) {
					addUids.add(uid);
				}
				addUids.removeAll(uids);
				if(CollectionUtils.isNotEmpty(addUids)) {
					List<DeveloperPower> addDP = new ArrayList<>();
					addUids.forEach(uid->{
						DeveloperPower developerPower = new DeveloperPower();
						developerPower.setId(UuidUtils.generateUuid());
						developerPower.setUnitId(uid);
						developerPower.setDeveloperId(developerId);
						addDP.add(developerPower);
					});
					developerPowerRemoteService.saveAll(addDP.toArray(new DeveloperPower[0]));
				}
			}
		} catch (Exception e) {
			return error("添加失败");
		}
		return success("添加成功");
	}
    
    @ResponseBody
	@RequestMapping("/empower/deleteUnit")
	@ControllerInfo("删除单位")
	public String empowerDelete(@RequestBody String param,String developerId){
		try {
			JSONObject jsonObject = SUtils.dc(param, JSONObject.class);
    		List<String>  unitIdList = JSONObject.parseArray(jsonObject.getString("unitIds"), String.class);
    		developerPowerRemoteService.deleteByDeveloperIdAndUnitIdIn(developerId,unitIdList.toArray(new String[unitIdList.size()]));
		} catch (Exception e) {
			return error("删除失败");
		}
		return success("删除成功");
	}
    
	@RequestMapping("/empower/findUnit")
	@ControllerInfo("查找单位")
	public String empowerDelete(ModelMap map,String developerId){
			//查找已经授权的单位列表
        List<DeveloperPower> developerPowers = SUtils.dt(developerPowerRemoteService.findByDeveloperId(developerId), 
        		DeveloperPower.class);
        if(CollectionUtils.isNotEmpty(developerPowers)) {
        	Set<String> uids = EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
        	List<Unit> allUnits = SUtils.dt(unitRemoteService.findListByIds(uids.toArray(new String[uids.size()]))
        			, Unit.class);
        	map.put("empowerUnits", allUnits);
        }
        map.put("developerId", developerId);
		return "/system/developer/developerPower.ftl";
	}
    
}
