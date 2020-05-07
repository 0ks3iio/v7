/*
 * @(#)GroupService.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.Group;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午4:00:30 $
 */
public interface GroupService extends BaseService<Group, String> {
    /**
     * 单位公共
     * 
     * @param unitId
     * @param type
     * @return
     */
    List<Group> findByUnitIdAndType(String unitId, Integer[] type);

    /**
     * 个人
     *
     * @param createUserId
     * @param type
     * @return
     */
    List<Group> findByUserIdAndType(String createUserId, Integer[] type);
}
