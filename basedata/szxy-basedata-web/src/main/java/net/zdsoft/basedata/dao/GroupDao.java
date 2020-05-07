/*
 * @(#)GroupDao.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Group;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午4:05:40 $
 */
public interface GroupDao extends BaseJpaRepositoryDao<Group, String> {
    @Query("From Group Where unitId = ?1 and type in  (?2) and opened=1")
    List<Group> findByUnitIdAndType(String unitId, Integer[] type);

    @Query("From Group Where createUserId = ?1 and type in  (?2) and opened=0")
    List<Group> findByUserIdAndType(String userId, Integer[] type);
}
