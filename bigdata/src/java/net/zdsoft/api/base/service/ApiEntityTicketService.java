/* 
 * @(#)OpenapiEntityTicketService.java    Created on 2017-3-7
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.api.base.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.basedata.service.BaseService;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-7 上午10:08:35 $
 */
public interface ApiEntityTicketService extends BaseService<ApiEntityTicket, String>{
    /**
     * 查询用户可接口可获得的敏感字段
     * 
     * @author chicb
     * @param ticketKey
     * @param isSenitive
     * @param types
     * @return
     */
    List<ApiEntityTicket> getEntitys(String ticketKey, int isSenitive, String... types);

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
    void updateOpenapiEntityTicket(String type, String ticketKey, String[] columnNames,int isSensitive);
    /**
     * 根据类型删除属性
     * @param type
     */
	void deleteByType(String type);

	List<ApiEntityTicket> findByTicketKeyAndTypeIn(String ticketKey, String... types);

	void deleteByTypeInAndTicketKey(String[] type, String ticketKey);

	
	List<ApiEntityTicket> fingByTicketKeyAndInterfaceIdIn(String ticketKey,String[] interfaceIds);

}
