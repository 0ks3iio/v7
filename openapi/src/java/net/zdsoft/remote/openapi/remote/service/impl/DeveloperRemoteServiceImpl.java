/* 
 * @(#)DeveloperRemoteServiceImpl.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.remote.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.dto.EntityDto;
import net.zdsoft.remote.openapi.dto.InterfaceDto;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.InterfaceDateTypeEnum;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.remote.service.DeveloperRemoteService;
import net.zdsoft.remote.openapi.service.DeveloperService;
import net.zdsoft.remote.openapi.service.EntityTicketService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceTypeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午02:37:46 $
 */
@Service("developerRemoteService")
public class DeveloperRemoteServiceImpl implements DeveloperRemoteService {
    @Resource
    private DeveloperService develoerService;
    @Resource
    private OpenApiApplyService openApiApplyService;
    @Resource
    private EntityTicketService entityTicketService;
    @Resource
    private OpenApiEntityService openApiEntityService;
    @Autowired
    private OpenApiInterfaceService interfaceService;
    @Autowired
    private OpenApiInterfaceTypeService openApiInterfaceTypeService;

    @Override
    public List<DeveloperDto> getDevelperSimpleInfos() {
        List<Developer> developers = develoerService.getAllOdereByCreationTime();
        if (CollectionUtils.isNotEmpty(developers)) {
            Set<String> list = EntityUtils.getSet(developers, Developer::getId);
            List<OpenApiApply> applys = openApiApplyService.getApplys(list.toArray(new String[list.size()]));
            Map<String,List<OpenApiApply>> developerIdMap = EntityUtils.getListMap(applys, OpenApiApply::getDeveloperId, Function.identity());
            Map<String, String> typeNameMap = getAllTypeNameMap();
            Map<String, List<String>> map = new HashMap<>();
            Set<String> check = new HashSet<>();
            for (Map.Entry<String,List<OpenApiApply>> entry : developerIdMap.entrySet()) {
            	String developerId = entry.getKey();
            	List<String> typeNames = new ArrayList<>();
            	List<OpenApiApply> openApiApplies = entry.getValue();
            	openApiApplies.forEach(e->{
            		if (ApplyStatusEnum.PASS_VERIFY.getValue() == e.getStatus()) {
            			typeNames.add(typeNameMap.get(e.getType()));
            		}else if (ApplyStatusEnum.IN_VERIFY.getValue() == e.getStatus()) {
            			check.add(developerId);
            		}
            	});
            	map.put(developerId, typeNames);
			}
            return getDeveloperDtos(developers, map, check);
        }
        return Collections.emptyList();          
    }

    @Override
    public DeveloperDto getDeveloper(String id) {
        return convertDeveloperDto(develoerService.findOne(id));
    }
    
    @Override
	public DeveloperDto findByTicketKey(String ticketKey) {
		return convertDeveloperDto(develoerService.findByTicketKey(ticketKey));
	}

    @Override
    public DeveloperDto getDeveloperDto(String id) {
        // 开发者基本信息
        Developer one = develoerService.findOne(id);
        // 申请的接口状态信息
        List<OpenApiApply> applys = openApiApplyService.getApplys(id);
        if (CollectionUtils.isEmpty(applys)) {
            return convertDeveloperDto(one);
        }
        // 接口状态和对应的名称
        Map<Integer, List<String>> typesMap = getTypesMap(applys);
        // 已订阅的接口名称
        List<String> passTypes = typesMap.get(ApplyStatusEnum.PASS_VERIFY.getValue());
        Map<String, Integer> sensitiveNum = null;
        Map<String, Integer> commonNum = null;
        if (CollectionUtils.isNotEmpty(passTypes)) {
            // 查询已订阅接口的敏感字段
            List<EntityTicket> entitys = entityTicketService.findByTicketKeyAndTypeIn(one.getTicketKey(),passTypes.toArray(new String[passTypes.size()]));
            List<EntityTicket> trueSensitives = entitys.stream().filter(t -> t.getIsSensitive() == EntityTicket.IS_SENSITIVE_TRUE)
                    .collect(Collectors.toList());
            List<EntityTicket> falseSensitives = entitys.stream().filter(t -> t.getIsSensitive() == EntityTicket.IS_SENSITIVE_FALSE)
                    .collect(Collectors.toList());
            // 已订阅接口的对应的敏感字段数
            sensitiveNum = getSensitiveNum(trueSensitives);
            // 已订阅接口的对应的普通字段数
            commonNum = getSensitiveNum(falseSensitives);
        }
        DeveloperDto dto = convertDeveloperDto(one);
        dto.setInterfaces(getInterfaceDtoMap(typesMap, sensitiveNum, commonNum, dto));
        return dto;
    }

    @Override
    public Map<String, String> getDeveloper(List<String> ids) {
        List<Developer> developers = develoerService.findListByIdIn(ids.toArray(new String[ids.size()]));
        Map<String, String> idAndNames = new HashMap<String, String>();
        for (Developer d : developers) {
            idAndNames.put(d.getId(), d.getUnitName());
        }
        return idAndNames;
    }

    @Override
    public void delInterface(String[] type, String ticketKey, String developerId) {
        openApiApplyService.removeInterface(type, ticketKey, developerId);
    }

    @Override
    public List<EntityDto> checkSensitive(String type, String ticketKey) {
        return checkEntity(type, ticketKey, YesNoEnum.YES.getValue());
    }

	@Override
	public List<EntityDto> checkEntity(String type, String ticketKey,
			int isSensitive) {
		 // 用户可以看见的字段
        List<String> columnNames = entityTicketService.getColumnNames(ticketKey, isSensitive, type);
        // 所有字段
        List<OpenApiEntity> entitys = openApiEntityService.getEntitys(type, isSensitive);
        return toOpenApiEntitys(columnNames, entitys);
	}

    @Override
    public List<InterfaceDto> checkInVerify(String developerId,String... type) {
    	List<String> types = new ArrayList<>();
    	if(type != null) {
    		types.addAll(Arrays.asList(type));
    	}else {
    		types = openApiApplyService.getTypes(ApplyStatusEnum.IN_VERIFY.getValue(), developerId);
    	}
    	// 开发者基本信息
        Developer developer = develoerService.findOne(developerId);
        List<OpenApiEntity> entitys = getEntityTicketList(types, developer.getTicketKey());
        return convertInterfaceDtos(types, entitys);
    }

    @Override
    public void modifyApplyInterface(String[] types, String developerId, String ticketKey, int status) {
        openApiApplyService.updateApplyInterface(types, developerId, ticketKey, status);
    }

    @Override
    public int modifyDeveloperUnitName(String developerId, String name) {
        return develoerService.updateUnitName(developerId, name);
    }

    @Override
    public int modifyDeveloperIps(String developerId, String ips) {
        return develoerService.updateIps(developerId, ips);
    }

    @Override
    public int defaultPw(String developerId) {
        return develoerService.updatePwd(OpenApiConstants.DEFAULT_PASSWORD, developerId);
    }
    
    @Override
    public void modifyEntityTicket(String type, String ticketKey, String[] columnNames,int isSensitive) {
        entityTicketService.updateEntityTicket(type, ticketKey, columnNames, isSensitive);
    }

    @Override
    public void passApplyInterface(DeveloperDto developerDto) {
        List<InterfaceDto> inters = developerDto.getPassInterfaceDtos();
        Map<String, InterfaceDto> typeMap = EntityUtils.getMap(inters, InterfaceDto::getType);
        Map<String, List<String>> passColumnMap = new HashMap<String, List<String>>();
        List<String> types = new ArrayList<>();
        for (InterfaceDto dto : inters) {
            String type = dto.getType();
            types.add(type);
            List<EntityDto> entitys = dto.getEntitys();
            List<String> ents = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(entitys)) {
                for (EntityDto en : entitys) {
                    ents.add(en.getColumnName());
                }
            }
            passColumnMap.put(type, ents);
        }
        List<OpenApiApply> openApiApplies = openApiApplyService.findByDeveloperIdAndTypeIn(developerDto.getId(), types.toArray(new String[0]));
        openApiApplies.forEach(o->{
        	InterfaceDto dto = typeMap.get(o.getType());
        	o.setStatus(ApplyStatusEnum.PASS_VERIFY.getValue());
        	o.setModifyTime(new Date());
        	o.setMaxNumDay(dto.getMaxNumDay());
        	o.setLimitEveryTime(dto.getLimitEveryTime());
        });
        openApiApplyService.saveAll(openApiApplies);
        openApiApplyService.updatePassApply(developerDto.getId(), developerDto.getTicketKey(), passColumnMap );
    }

    /** -------------------------私有方法区---------------------------------- **/
    
    /**
     * @author yangsj
     * @param types
     * @param ticketKey
     * @return
     */
    private List<OpenApiEntity> getEntityTicketList(List<String> types, String ticketKey) {
		List<OpenApiEntity> entitys = new ArrayList<>();
		List<EntityTicket> entityTickets =  entityTicketService.findByTicketKeyAndTypeIn(ticketKey, types.toArray(new String[types.size()]));
		Map<String, EntityTicket> typeColumnMap = new HashMap<String, EntityTicket>();
		for (EntityTicket entityTicket : entityTickets) {
			String key = entityTicket.getType() + entityTicket.getEntityColumnName();
			typeColumnMap.put(key, entityTicket);
		}
        if(CollectionUtils.isNotEmpty(entityTickets)){
        	Set<String> columnNuames = EntityUtils.getSet(entityTickets, EntityTicket::getEntityColumnName);
        	// 查询允许获得的参数
        	if (!CollectionUtils.isEmpty(columnNuames)) {
//        		entitys = openApiEntityService.findByIsUsingAndEntityColumnNameIn (YesNoEnum.YES.getValue(),
//        				columnNuames.toArray(new String[columnNuames.size()]));
        		List<OpenApiEntity> entityList = openApiEntityService.findByIsUsingAndEntityColumnNameInAndTypeIn (YesNoEnum.YES.getValue(),
        				columnNuames.toArray(new String[columnNuames.size()]), 
        				types.toArray(new String[types.size()])
        				);
        		if(CollectionUtils.isNotEmpty(entityList)){
        			for (OpenApiEntity openApiEntity : entityList) {
        				String key = openApiEntity.getType() + openApiEntity.getEntityColumnName();
        				if(typeColumnMap.get(key) != null){
        					entitys.add(openApiEntity);
        				}
        			}
        		}
        	}
        }
		return entitys;
	}
    /**
     * @author chicb
     * @param types
     * @param entitys
     * @return
     */
    private List<InterfaceDto> convertInterfaceDtos(List<String> types, List<OpenApiEntity> entitys) {
    	 List<InterfaceDto> dtos = new ArrayList<InterfaceDto>();
         if (CollectionUtils.isEmpty(entitys)) {
             return convertInterfaceDtos(types);
         }
         Map<String, List<OpenApiEntity>> entityMap = getOpenApiEntityMap(entitys);
         Map<String, String> typeNameMap = getAllTypeNameMap();
         for (String t : types) {
             InterfaceDto dto = new InterfaceDto();
             dto.setType(t);
             dto.setTypeName(typeNameMap.get(t));
             List<OpenApiEntity> list = entityMap.get(t);
             dto.setEntitys(convertOpenApiEntitys(list));
             dto.setMaxNumDay(OpenApiApply.DEFAULT_MAX_NUM_EVERYDAY);
             dto.setLimitEveryTime(OpenApiApply.DEFAULT_LIMIT_EVERY_TIME);
             dtos.add(dto);
         }
         return dtos;
    }

	/**
     * @author chicb
     * @param entitys
     * @return
     */
    private Map<String, List<OpenApiEntity>> getOpenApiEntityMap(List<OpenApiEntity> entitys) {
        Map<String, List<OpenApiEntity>> map = new HashMap<String, List<OpenApiEntity>>();
        for (OpenApiEntity e : entitys) {
            String type = e.getType();
            List<OpenApiEntity> list = map.get(type);
            if (CollectionUtils.isEmpty(list)) {
                list = new ArrayList<OpenApiEntity>();
            }
            list.add(e);
            map.put(type, list);
        }
        return map;
    }

    /**
     * @author chicb
     * @param types
     * @return
     */
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

    /**
     * @author chicb
     * @param columnNames
     * @param entitys
     * @return
     */
    private List<EntityDto> toOpenApiEntitys(List<String> columnNames, List<OpenApiEntity> entitys) {
        if (CollectionUtils.isEmpty(entitys)) {
            return null;
        }
        List<EntityDto> dtos = new ArrayList<EntityDto>();
        for (OpenApiEntity entity : entitys) {
        	if (columnNames.contains(entity.getEntityColumnName())) {
                EntityDto dto = convertOpenApiEntity(entity);
                dto.setIsAutho(YesNoEnum.YES.getValue());
                dtos.add(dto);
            }
        }
        return dtos;
    }

    /**
     * @author chicb
     * @param entitys
     * @return
     */
    private List<EntityDto> convertOpenApiEntitys(List<OpenApiEntity> entitys) {
        if (CollectionUtils.isEmpty(entitys)) {
            return null;
        }
        List<EntityDto> dtos = new ArrayList<EntityDto>();
        for (OpenApiEntity entity : entitys) {
            dtos.add(convertOpenApiEntity(entity));
        }
        return dtos;
    }

    /**
     * @author chicb
     * @param entity
     */
    private EntityDto convertOpenApiEntity(OpenApiEntity entity) {
        EntityDto dto = new EntityDto();
        dto.setColumnName(entity.getEntityColumnName());
        dto.setDisplayName(entity.getDisplayName());
        dto.setIsSensitive(entity.getIsSensitive());
        return dto;
    }

    /**
     * @author chicb
     * @param typesMap
     * @param sensitiveNum
     * @param dto
     * @return
     */
    private Map<String, List<InterfaceDto>> getInterfaceDtoMap(Map<Integer, List<String>> typesMap,
            Map<String, Integer> sensitiveNum,Map<String, Integer> commonNum, DeveloperDto dto) {
        Map<String, List<InterfaceDto>> dtoMap = new HashMap<String, List<InterfaceDto>>();
        boolean notEmpty = MapUtils.isNotEmpty(sensitiveNum);
        for (Map.Entry<Integer, List<String>> entry : typesMap.entrySet()) {
            Integer key = entry.getKey();
            List<String> value = entry.getValue();
            if (ApplyStatusEnum.PASS_VERIFY.getValue() == key) {
                dto.setPassVerifyNum(value.size());
            }
            else if (ApplyStatusEnum.UNPASS_VERIFY.getValue() == key) {
                dto.setUnpassVerifyNum(value.size());
            }
            else if (ApplyStatusEnum.IN_VERIFY.getValue() == key) {
                dto.setInVerifyNum(value.size());
            }
            List<InterfaceDto> dtos = new ArrayList<>();
//            TODO
            Map<String, String> typeNameMap = getAllTypeNameMap();
            for (String v : value) {
                InterfaceDto inter = new InterfaceDto();
                if (notEmpty && ApplyStatusEnum.PASS_VERIFY.getValue() == key) {
                    Integer num = sensitiveNum.get(v);
                    inter.setSensitiveNum(null == num ? 0 : num);
                }
                if(MapUtils.isNotEmpty(commonNum) && ApplyStatusEnum.PASS_VERIFY.getValue() == key){
                	Integer common = commonNum.get(v);
                    inter.setCommonNum(null == common ? 0 : common);
                }
                inter.setType(v);
                inter.setTypeName(typeNameMap.get(v));
                dtos.add(inter);
            }
            dtoMap.put(key.toString(), dtos);
        }
        return dtoMap;
    }

    private Map<String, Integer> getSensitiveNum(List<EntityTicket> entitys) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (CollectionUtils.isNotEmpty(entitys)) {
            for (EntityTicket e : entitys) {
                Integer i = map.get(e.getType());
                if (null == i) {
                    map.put(e.getType(), 1);
                }
                else {
                    map.put(e.getType(), i + 1);
                }
            }
        }
        return map;
    }

    /**
     * @author chicb
     * @param applys
     * @return
     */
    private Map<Integer, List<String>> getTypesMap(List<OpenApiApply> applys) {
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        for (OpenApiApply a : applys) {
            int status = a.getStatus();
            List<String> list = map.get(status);
            if (CollectionUtils.isEmpty(list)) {
                list = new ArrayList<String>();
            }
            list.add(a.getType());
            map.put(status, list);
        }
        return map;
    }

    private List<DeveloperDto> getDeveloperDtos(List<Developer> developers, Map<String, List<String>> interfaces,
            Set<String> check) {
        boolean ismap = MapUtils.isNotEmpty(interfaces);
        boolean isset = CollectionUtils.isNotEmpty(check);
        List<DeveloperDto> dtos = new ArrayList<DeveloperDto>();
        for (Developer d : developers) {
            DeveloperDto dto = convertDeveloperDto(d);
            dto.setInterfaceNames(ismap ? interfaces.get(d.getId()) : null);
            if (isset) {
                dto.setIsCheck(check.contains(d.getId()) ? YesNoEnum.YES.getValue() : YesNoEnum.NO.getValue());
            }
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * @author chicb
     * @param d
     * @return
     */
    private DeveloperDto convertDeveloperDto(Developer d) {
        DeveloperDto dto = new DeveloperDto();
        dto.setAddress(d.getAddress());
        dto.setCreationTime(d.getCreationTime());
        dto.setTicketKey(d.getTicketKey());
        dto.setEmail(d.getEmail());
        dto.setHomepage(d.getHomepage());
        dto.setId(d.getId());
        dto.setMobilePhone(d.getMobilePhone());
        dto.setRealName(d.getRealName());
        dto.setUnitName(d.getUnitName());
        dto.setUsername(d.getUsername());
        dto.setIps(d.getIps());
        return dto;
    }
    
    /**
	 * 得到所有的接口类型名称 根据 type-->key typeName -->value
	 * @return
	 */
	private Map<String, String> getAllTypeNameMap() {
		Integer[] types = {OpenApiInterfaceType.RESULT_TYPE,OpenApiInterfaceType.PUBLIC_TYPE};
		List<OpenApiInterfaceType>  interfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
		return EntityUtils.getMap(interfaceTypes, OpenApiInterfaceType::getType, OpenApiInterfaceType::getTypeName);
	}

	@Override
	public String findEntityMapByTicketKeyAndTypeIn(String ticketKey,
			String[] types) {
		Map<String, List<EntityDto>> entitydtoMap = new HashMap<>();
		List<OpenApiEntity>  openApiEntities = getEntityTicketList(Arrays.asList(types),ticketKey);
		Map<String, List<OpenApiEntity>> entityMap = getOpenApiEntityMap(openApiEntities);
        for (String t : types) {
            List<OpenApiEntity> list = entityMap.get(t);
            entitydtoMap.put(t, convertOpenApiEntitys(list));
        }
		return SUtils.s(entitydtoMap);
	}
}
