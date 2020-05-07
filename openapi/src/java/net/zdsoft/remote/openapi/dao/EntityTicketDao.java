/* 
 * @(#)EntityTicketDao.java    Created on 2017-3-1
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.EntityTicket;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-1 上午10:01:49 $
 */
public interface EntityTicketDao extends BaseJpaRepositoryDao<EntityTicket, String> {

    /**
     * 查询允许获得的参数
     * 
     * @author chicb
     * @param resultType
     * @param type
     * @return
     */
    @Query("select entityColumnName from EntityTicket where type=?1 and ticketKey=?2")
    List<String> findColumnNames(String type, String ticketKey);

    /**
     * 根据（ticketKey,isSensitive,types)查询
     * 
     * @author chicb
     * @param ticketKey
     * @param isSenitive
     * @param types
     * @return
     */
    List<EntityTicket> findByTicketKeyAndIsSensitiveAndTypeIn(String ticketKey, int isSenitive, String... types);

    /**
     * @author chicb
     * @param type
     * @param tickerKey
     */
    @Modifying
    @Query("delete from EntityTicket where type in ?1 and ticketKey =?2")
    void deleteEntityTicket(String[] type, String ticketKey);

    /**
     * @author chicb
     * @param ticketKey
     * @param isSenitive
     * @param type
     * @return
     */
    @Query("select entityColumnName from EntityTicket where ticketKey=?1 and isSensitive=?2 and type=?3")
    List<String> findColumnNames(String ticketKey, int isSenitive, String type);

    /**
     * 删除 指定类型的某个开发者的敏感字段
     * 
     * @author chicb
     * @param type
     * @param ticketKey
     * @param isSensitive
     */
    @Modifying
    @Query("delete from EntityTicket where type=?1 and ticketKey=?2 and isSensitive=?3")
    void delete(String type, String ticketKey, int isSensitive);

    @Modifying
    @Query("delete from EntityTicket where type=?1")
	void deleteByType(String type);

    
	List<EntityTicket> findByTicketKeyAndTypeIn(String ticketKey, String... types);

	
	void deleteByIdIn(String... array);
}
