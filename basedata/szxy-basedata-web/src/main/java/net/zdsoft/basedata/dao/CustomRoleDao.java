/*
* Project: v7
* Author : shenke
* @(#) CustomRoleDao.java Created on 2016-10-13
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-13下午6:59:47
 */
public interface CustomRoleDao extends BaseJpaRepositoryDao<CustomRole, String>{

	@Query("from CustomRole where unitId =?1 and subsystems = ?2")
	List<CustomRole> findByUnitIdAndSubsystem(String unitId, String subsystem);

	@Query("from CustomRole where unitId =?1 and subsystems = ?2 and roleCode=?3 ")
	List<CustomRole> findByUnitIdAndSubsystemAndRoleCode(String unitId, String subsystem,String roleCode);
	

}
