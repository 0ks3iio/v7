/*
 * @(#)GroupRemoteServiceImpl.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Group;
import net.zdsoft.basedata.remote.service.GroupRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.GroupService;
import net.zdsoft.framework.utils.SUtils;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午3:52:17 $
 */
@Service("groupRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class GroupRemoteServiceImpl extends BaseRemoteServiceImpl<Group,String> implements GroupRemoteService {
    @Autowired
    private GroupService groupService;

    @Override
    public String findByUnitIdAndType(String unitId, Integer[] type, String userId) {
        List<Group> groupList = groupService.findByUnitIdAndType(unitId, type);
        groupList.addAll(groupService.findByUserIdAndType(userId, type));
        return SUtils.s(groupList);
    }

    @Override
    protected BaseService<Group, String> getBaseService() {
        return groupService;
    }

}
