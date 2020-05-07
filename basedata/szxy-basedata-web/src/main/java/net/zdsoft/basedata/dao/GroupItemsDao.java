/*
 * @(#)GroupItemsDao.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.GroupItems;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午4:56:16 $
 */
public interface GroupItemsDao extends BaseJpaRepositoryDao<GroupItems, String> {

    @Query("select itemId From GroupItems Where groupId = ?1")
    List<String> findItemIdsByGroupId(String groupId);
}
