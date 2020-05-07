/* 
 * @(#)ServerAuthorizeJdbcDao.java    Created on 2017年3月17日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.server;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月17日 下午4:07:07 $
 */
public interface ServerAuthorizeJdbcDao {
    /**
     * 批量删除订阅信息
     * 
     * @author cuimq
     * @param serverId
     * @param region
     * @param unitClass
     */
    void deleteByServerIdAndUnitCondition(Integer serverId, String region, Integer[] unitClass);
}
