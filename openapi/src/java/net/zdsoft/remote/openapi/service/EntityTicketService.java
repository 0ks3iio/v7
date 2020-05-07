/* 
 * @(#)EntityTicketService.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.remote.openapi.entity.EntityTicket;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:08:35 $
 */
public interface EntityTicketService extends BaseService<EntityTicket, String>{
    /**
     * 查询用户可接口可获得的敏感字段
     * 
     * @author chicb
     * @param ticketKey
     * @param isSenitive
     * @param types
     * @return
     */
    List<EntityTicket> getEntitys(String ticketKey, int isSenitive, String... types);

    /**
     * 查询用户可以获得的敏感字段
     * 
     * @author chicb
     * @param ticketKey
     * @param isSenitive
     * @param type
     * @return
     */
    List<String> getColumnNames(String ticketKey, int isSenitive, String type);

    /**
     * 修改用户看见的字段（注意：是先删后加的修改，请不要随意操作）
     * 
     * @author chicb
     * @param type
     * @param ticketKey
     * @param columnNames
     * @param isSensitive 是否是敏感字段
     */
    void updateEntityTicket(String type, String ticketKey, String[] columnNames,int isSensitive);
    /**
     * 根据类型删除属性
     * @param type
     */
	void deleteByType(String type);

	List<EntityTicket> findByTicketKeyAndTypeIn(String ticketKey, String... types);

	void deleteByTypeInAndTicketKey(String[] type, String ticketKey);

}
