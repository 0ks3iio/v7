/* 
 * @(#)DeveloperRemoteService.java    Created on 2017-3-6
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.remote.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.remote.openapi.dto.DeveloperDto;
import net.zdsoft.remote.openapi.dto.EntityDto;
import net.zdsoft.remote.openapi.dto.InterfaceDto;
import net.zdsoft.remote.openapi.entity.Developer;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-6 下午02:38:01 $
 */
public interface DeveloperRemoteService {
    /**
     * 开发者简单信息,根据创建时间排序
     * 
     * @author chicb
     * @return（id;ticketKey;unitName;username;realName;mobilePhone;email;address;homepage;interfaceNames）
     */
    List<DeveloperDto> getDevelperSimpleInfos();

    /**
     * 根据id获得开发者详细信息
     * 
     * @author chicb
     * @param id
     * @return
     */
    DeveloperDto getDeveloperDto(String id);

    /**
     * 
     * @author chicb
     * @param id
     * @return
     */
    DeveloperDto getDeveloper(String id);

    /**
     * 根据ids获得开发者姓名
     * 
     * @author chicb
     * @param ids
     * @return Map<id,unitName>
     */
    Map<String, String> getDeveloper(List<String> ids);

    /**
     * 删除已订阅的接口
     * 
     * @author chicb
     * @param type
     * @param tickerKey
     * @param developerId
     */
    void delInterface(String[] type, String ticketKey, String developerId);

    /**
     * 查询对应接口的敏感字段，并标记出用户可用的敏感字段
     * 
     * @author chicb
     * @param type
     * @param ticketKey
     * @return
     */
    List<EntityDto> checkSensitive(String type, String ticketKey);

    /**
     * 修改用户的看见字段（注意：是先删后加的修改，请不要随意操作）
     * @author chicb
     * @param type
     * @param ticketKey
     * @param columnNames
     * @param isSensitive 是否敏感字段
     */
    void modifyEntityTicket(String type, String ticketKey, String[] columnNames,int isSensitive );

    /**
     * 查询审核中的接口即可或的敏感字段信息
     * 
     * @author chicb
     * @param developerId
     * @param type
     * @return
     */
    List<InterfaceDto> checkInVerify(String developerId,String... type);

    /**
     * 更改申请信息
     * 
     * @author chicb
     * @param types
     * @param developerId
     * @param ticketKey
     * @param status
     */
    public void modifyApplyInterface(String[] types, String developerId, String ticketKey, int status);

    /**
     * 修改开发者名称
     * 
     * @author chicb
     * @param developerId
     * @param name
     */
    public int modifyDeveloperUnitName(String developerId, String name);

    /**
     * 修改开发者白名单
     * 
     * @param developerId
     * @param ips
     * @return
     */
    public int modifyDeveloperIps(String developerId, String ips);

    /**
     * 接口审核通过（操作）
     * 
     * @author chicb
     * @param deserialize
     */
    public void passApplyInterface(DeveloperDto developerDto);

    /**
     * 重置密码
     * 
     * @author chicb
     * @param developerId
     * @return
     */
    int defaultPw(String developerId);

    DeveloperDto findByTicketKey(String ticketKey);

	String findEntityMapByTicketKeyAndTypeIn(String ticketKey, String[] array);

	List<EntityDto> checkEntity(String type, String ticketKey, int isSensitive);
	
}
