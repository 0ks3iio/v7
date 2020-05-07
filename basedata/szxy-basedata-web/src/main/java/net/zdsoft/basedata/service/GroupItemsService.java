/*
 * @(#)GroupItemsService.java    Created on 2017年3月1日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.dto.GroupItemsDto;
import net.zdsoft.basedata.entity.GroupItems;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017年3月1日 下午5:02:07 $
 */
public interface GroupItemsService extends BaseService<GroupItems, String> {
    /**
     * 查询组成员id
     *
     * @param groupId
     * @return
     */
    List<String> findByGroupId(String groupId);

    /**
     * 查询某个类型组下的组成员id和名称
     *
     * @param groupId
     * @param type
     * @return
     */
    List<GroupItemsDto> findByGroupIdAndType(String groupId, Integer type);

}
