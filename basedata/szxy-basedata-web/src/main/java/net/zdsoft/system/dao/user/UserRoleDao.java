/* 
 * @(#)UserRoleDao.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.user.UserRole;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:51:36 $
 */
public interface UserRoleDao extends BaseJpaRepositoryDao<UserRole, String> {

    @Query("From UserRole where userId in ?1")
    List<UserRole> findByUserIds(String[] userIds);

    @Query("From UserRole where roleId in ?1")
    List<UserRole> findByRoleIds(String[] roleIds);

    @Query("select userId from UserRole where roleId=?1")
    List<String> findUserIdsByRoleId(String roleId);

    @Modifying
    @Query("delete from UserRole where roleId=?1")
    void deleteByRoleId(String roleId);

    @Query("select roleId from UserRole where userId=?1")
    List<String> findRoleIdsByUserId(String userId);

    @Query("select userId from UserRole where roleId in ?1")
    List<String> findUserIdsByRoleIds(String[] roleIds);
    
    @Modifying 
	@Query("update  UserRole set  userId = ?1 where userId = ?2")
	void updateRemoteId(String id, String oldId);

    @Query("From UserRole where roleId = ?1 and userId in ?2")
	List<UserRole> findByRoleIdAndUserIdIn(String roleId, String[] ids);

    @Modifying
    @Query("delete from UserRole where roleId=?1 and userId in ?2")
	void deleteByRoleIdAndUserIdIn(String roleId, String[] ids);
    
    @Modifying
    @Query("delete from UserRole where userId in ?1")
    void deleteByUserIdIn(String[] ids);
}
