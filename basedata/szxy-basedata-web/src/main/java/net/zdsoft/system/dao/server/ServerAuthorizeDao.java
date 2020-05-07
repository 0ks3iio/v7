/* 
 * @(#)ServerAuthorizeDao.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.server;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.server.ServerAuthorize;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:10:20 $
 */
public interface ServerAuthorizeDao extends BaseJpaRepositoryDao<ServerAuthorize, String> {

    @Query("FROM ServerAuthorize where unitId in ?1")
    List<ServerAuthorize> findByUnitIds(String[] unitIds);

    @Query("select serverId from ServerAuthorize where unitId = ?1")
    List<Integer> findServerIds(String unitId);

    @Modifying
    @Query("delete ServerAuthorize where unitId = ?1")
    void deleteByUnitId(String unitId);

    @Modifying
    @Query("delete ServerAuthorize where serverId = ?1 and unitId in ?2")
    void deleteByServerIdAndUnitIds(Integer serverId, String[] unitIds);

    @Query("select unitId from ServerAuthorize where serverId = ?1")
    List<String> findUnitIds(Integer serverId);
}
