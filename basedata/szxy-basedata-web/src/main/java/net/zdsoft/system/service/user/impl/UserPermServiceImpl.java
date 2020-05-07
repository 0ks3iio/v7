/* 
 * @(#)UserPermServiceImpl.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.user.UserPermDao;
import net.zdsoft.system.entity.user.UserPerm;
import net.zdsoft.system.service.user.UserPermService;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 下午1:30:35 $
 */
@Service
public class UserPermServiceImpl extends BaseServiceImpl<UserPerm, String> implements UserPermService {

    @Autowired
    private UserPermDao userPermDao;

    @Override
    protected BaseJpaRepositoryDao<UserPerm, String> getJpaDao() {
        return userPermDao;
    }

    @Override
    protected Class<UserPerm> getEntityClass() {
        return UserPerm.class;
    }

    @Override
    public void saveUserPerm(List<UserPerm> userPermList, String userId, Integer[] allModelIds) {
        userPermDao.deleteByUserIdAndModelIds(userId, allModelIds);
        if (CollectionUtils.isNotEmpty(userPermList)) {
            userPermDao.saveAll(userPermList);
        }
    }

    @Override
    public List<Integer> findModelIdsByUserId(String userId) {
        return userPermDao.findModelIdsByUserId(userId);
    }

    @Override
    public List<String> getUserIdsByModelIds(Integer[] modelIds) {
        return userPermDao.findUserIdsByModelIds(modelIds);
    }
}
