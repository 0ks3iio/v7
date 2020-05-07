/* 
 * @(#)RolePermServiceImpl.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.user.RoleDao;
import net.zdsoft.system.dao.user.RolePermDao;
import net.zdsoft.system.dao.user.UserRoleDao;
import net.zdsoft.system.entity.user.RolePerm;
import net.zdsoft.system.service.user.RolePermService;
import net.zdsoft.system.service.user.UserRoleService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:55:54 $
 */
@Service
public class RolePermServiceImpl extends BaseServiceImpl<RolePerm, String> implements RolePermService {

    @Autowired
    private RolePermDao rolePermDao;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    protected BaseJpaRepositoryDao<RolePerm, String> getJpaDao() {
        return rolePermDao;
    }

    @Override
    protected Class<RolePerm> getEntityClass() {
        return RolePerm.class;
    }

    @Override
    public List<Integer> findModelIdsByRoleId(String roleId) {
        return rolePermDao.findModelIdsByRoleIds(new String[] { roleId });
    }

    @Override
    public List<Integer> findModelIdsByUserIdAndIsSystem(String userId, Integer isSystem) {
        // 查找用户所在用户组
        List<String> roleIds = userRoleService.findRoleIdsByUserId(userId);

        List<Integer> modelIds = new ArrayList<Integer>();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<String> newRoleIds = roleDao.findByIdsAndIsSystem(roleIds.toArray(new String[roleIds.size()]),
                    isSystem);
            if (CollectionUtils.isNotEmpty(newRoleIds)) {
                modelIds = rolePermDao.findModelIdsByRoleIds(newRoleIds.toArray(new String[newRoleIds.size()]));
            }
        }
        return modelIds;
    }

    @Override
    public List<String> getUserIdsByModelIds(Integer[] modelIds) {
        List<String> roleIds = rolePermDao.findRoleIdsByModelIds(modelIds);
        List<String> userIds = userRoleDao.findUserIdsByRoleIds(roleIds.toArray(new String[roleIds.size()]));
        return userIds;
    }
}
