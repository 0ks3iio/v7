/* 
 * @(#)RolePermService.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.user;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.user.RolePerm;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:53:50 $
 */
public interface RolePermService extends BaseService<RolePerm, String> {

    /**
     * 查询用户组授权模块
     * 
     * @author cuimq
     * @param roleId
     * @return
     */
    List<Integer> findModelIdsByRoleId(String roleId);

    /**
     * 查找用户所在用户组授权模块
     * 
     * @author cuimq
     * @param userId
     * @return
     */
    List<Integer> findModelIdsByUserIdAndIsSystem(String userId, Integer isSystem);

    List<String> getUserIdsByModelIds(Integer[] modelIds);

}
