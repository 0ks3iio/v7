/*
 * @(#)GroupServiceImpl.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.GroupDao;
import net.zdsoft.basedata.entity.Group;
import net.zdsoft.basedata.service.GroupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午4:01:17 $
 */
@Service("groupService")
public class GroupServiceImpl extends BaseServiceImpl<Group, String> implements GroupService {
    @Autowired
    private GroupDao groupDao;

    @Override
    public List<Group> findByUnitIdAndType(String unitId, Integer[] type) {
        return groupDao.findByUnitIdAndType(unitId, type);
    }

    @Override
    public List<Group> findByUserIdAndType(String createUserId, Integer[] type) {
        return groupDao.findByUserIdAndType(createUserId, type);
    }

    @Override
    protected BaseJpaRepositoryDao<Group, String> getJpaDao() {
        return groupDao;
    }

    @Override
    protected Class<Group> getEntityClass() {
        return Group.class;
    }
}
