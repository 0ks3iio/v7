/* 
 * @(#)UserPermService.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.user;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.user.UserPerm;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:54:19 $
 */
public interface UserPermService extends BaseService<UserPerm, String> {

    /**
     * 修改用户权限信息
     * 
     * @author cuimq
     * @param userPermList
     * @param userId
     * @param allModelIds
     */
    void saveUserPerm(List<UserPerm> userPermList, String userId, Integer[] allModelIds);

    /**
     * 根据用户查找已授权模块
     * 
     * @author cuimq
     * @param userId
     * @return
     */
    List<Integer> findModelIdsByUserId(String userId);

    /**
     * 根据模块ID查找授权的用户ID
     * 
     * @param modelIds
     * @return
     */
    List<String> getUserIdsByModelIds(Integer[] modelIds);
}
