/*
 * Project: v7
 * Author : shenke
 * @(#) CustomRoleUserService.java Created on 2016-10-13
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.CustomRoleUser;

/**
 * @description:
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-13下午7:04:39
 */
public interface CustomRoleUserService extends BaseService<CustomRoleUser, String> {

    /**
     * TODO 应当做缓存（）
     * 
     * @param userId
     * @param roleCode
     * @return
     */
    boolean containRole(String userId, String roleCode);

    /**
     * 更新user-角色  
     * customRoleId为空不做任何操作
     * 
     * @param userIds
     *            array
     * @param customRoleId
     */
    void saveCustomRoleUsers(String unitId,String[] userIds, String customRoleId);

    public void saveAllEntitys(CustomRoleUser... customRoleUser);

    public void deleteAllByIds(String... id);

    void deleteByRoleId(String roleId);
}
