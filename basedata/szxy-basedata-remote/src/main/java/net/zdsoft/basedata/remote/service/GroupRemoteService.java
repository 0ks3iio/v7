/*
 * @(#)GroupRemoteService.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Group;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午3:51:33 $
 */
public interface GroupRemoteService extends BaseRemoteService<Group,String> {
    String findByUnitIdAndType(String unitId, Integer[] types, String userId);
}
