/*
 * Project: v7
 * Author : shenke
 * @(#) CustomRoleUserServiceImpl.java Created on 2016-10-13
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.CustomRoleUserDao;
import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.CustomRoleService;
import net.zdsoft.basedata.service.CustomRoleUserService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @description:
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-13下午7:05:17
 */
@Service("customRoleUserService")
public class CustomRoleUserServiceImpl extends BaseServiceImpl<CustomRoleUser, String> implements CustomRoleUserService {

    @Autowired
    private CustomRoleUserDao customRoleUserDao;
    @Autowired
    private CustomRoleService customRoleService;
    @Autowired
    private UserService userService;

    @Override
    protected BaseJpaRepositoryDao<CustomRoleUser, String> getJpaDao() {
        return customRoleUserDao;
    }

    @Override
    protected Class<CustomRoleUser> getEntityClass() {
        return CustomRoleUser.class;
    }

    @Override
    public boolean containRole(String userId, String roleCode) {

        List<CustomRoleUser> roleUsers = findListByIn("userId", ArrayUtils.<String> toArray(userId));
        if (CollectionUtils.isEmpty(roleUsers)) {
            return false;
        }

        List<String> roleUserIds = EntityUtils.getList(roleUsers, "roleId");
        List<CustomRole> roles = customRoleService.findListByIdIn(roleUserIds.toArray(new String[0]));
        Map<String, CustomRole> roleMap = EntityUtils.getMap(roles, "id", StringUtils.EMPTY);

        for (CustomRoleUser roleUser : roleUsers) {
            CustomRole role = roleMap.get(roleUser.getRoleId());
            if (role != null && role.getRoleCode().equals(roleCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveCustomRoleUsers(String unitId,String[] userIds, String customRoleId) {
        if (StringUtils.isEmpty(customRoleId)) {
            return;
        }
        
        List<CustomRoleUser> roleUsers = findListBy("roleId", customRoleId);
        if (CollectionUtils.isNotEmpty(roleUsers)) {
        	List<String> oldIds = EntityUtils.getList(roleUsers, e->e.getId());
        	deleteAllByIds(oldIds.toArray(new String[0]));
        }
        if(ArrayUtils.isNotEmpty(userIds)) {
        	List<CustomRoleUser> customRoleUsers = Lists.newArrayList();
            for (String userId : userIds) {
                CustomRoleUser customRoleUser = new CustomRoleUser();
                customRoleUser.setId(UuidUtils.generateUuid());
                customRoleUser.setUserId(userId);
                customRoleUser.setRoleId(customRoleId);
                customRoleUsers.add(customRoleUser);
            }
            saveAllEntitys(customRoleUsers.toArray(new CustomRoleUser[0]));

        }

    }

    @Override
    public void saveAllEntitys(CustomRoleUser... customRoleUser) {
        customRoleUserDao.saveAll(checkSave(customRoleUser));
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0)
            customRoleUserDao.deleteAllByIds(id);
    }

    @Override
    public void deleteByRoleId(String roleId) {
        customRoleUserDao.deleteByRoleId(roleId);
    }
}
