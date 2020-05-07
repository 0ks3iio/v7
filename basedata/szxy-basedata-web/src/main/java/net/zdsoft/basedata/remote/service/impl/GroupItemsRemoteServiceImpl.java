/*
 * @(#)GroupItemsRemoteServiceImpl.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.GroupItems;
import net.zdsoft.basedata.remote.service.GroupItemsRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.GroupItemsService;
import net.zdsoft.framework.utils.SUtils;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午5:30:03 $
 */
@Service("groupItemsRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class GroupItemsRemoteServiceImpl extends BaseRemoteServiceImpl<GroupItems,String> implements GroupItemsRemoteService {
    @Autowired
    private GroupItemsService groupItemsService;

    @Override
    protected BaseService<GroupItems, String> getBaseService() {
        return groupItemsService;
    }

    /**
     * 组成员的id，名称(人和单位)
     */
    @Override
    public String findByGroupIdAndType(String groupId, Integer type) {
        return SUtils.s(groupItemsService.findByGroupIdAndType(groupId, type));
    }
}
