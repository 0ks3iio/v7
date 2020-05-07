/*
 * @(#)GroupItemsRemoteService.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.GroupItems;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午5:25:04 $
 */
public interface GroupItemsRemoteService extends BaseRemoteService<GroupItems,String> {
    String findByGroupIdAndType(String groupId, Integer type);
}
