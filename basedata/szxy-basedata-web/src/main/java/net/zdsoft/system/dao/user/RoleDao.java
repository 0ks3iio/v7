/* 
 * @(#)RoleDao.java    Created on 2017年2月27日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.user.Role;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年2月27日 上午11:49:38 $
 */
public interface RoleDao extends BaseJpaRepositoryDao<Role, String> {

    @Query("From Role where id in ?1")
    List<Role> findByIds(String[] ids);

    @Query("select id from Role where id in ?1 and isSystem=?2")
    List<String> findByIdsAndIsSystem(String[] ids, Integer isSystem);

    @Query("From Role where unitId = ?1 and roleType = ?2")
	List<Role> findByUnitIdAndRoleType(String unitId, int roleTypeOper);
 
    @Query("From Role where name = ?1 and unitId = ?2")
	Role findByNameAndUnitId(String roleName, String unitId);
}
