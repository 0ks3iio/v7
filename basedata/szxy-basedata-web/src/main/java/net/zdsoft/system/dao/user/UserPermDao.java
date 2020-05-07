/* 
 * @(#)UserPerm.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.user.UserPerm;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:50:57 $
 */
public interface UserPermDao extends BaseJpaRepositoryDao<UserPerm, String> {

    @Modifying
    @Query("delete from UserPerm where userId=?1 and modelId in ?2")
    void deleteByUserIdAndModelIds(String userId, Integer[] modelIds);

    @Query("select modelId from UserPerm where userId=?1")
    List<Integer> findModelIdsByUserId(String userId);

    @Query("select userId from UserPerm where modelId in ?1")
    List<String> findUserIdsByModelIds(Integer[] modelIds);
}
