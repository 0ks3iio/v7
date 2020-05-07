/* 
 * @(#)Role.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.user.RolePerm;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:50:14 $
 */
public interface RolePermDao extends BaseJpaRepositoryDao<RolePerm, String> {

    @Query("select modelId from RolePerm where roleId in ?1")
    List<Integer> findModelIdsByRoleIds(String[] roleId);

    @Modifying
    @Query("delete from RolePerm where roleId=?1 and modelId in ?2")
    void deleteByRoleIdAndModelIds(String roleId, Integer[] modelIds);

    @Query("select roleId from RolePerm where modelId in ?1")
    List<String> findRoleIdsByModelIds(Integer[] modelIds);
}
