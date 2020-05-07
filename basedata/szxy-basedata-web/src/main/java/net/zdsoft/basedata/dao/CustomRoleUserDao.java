/*
* Project: v7
* Author : shenke
* @(#) CustomRoleUser.java Created on 2016-10-13
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @description: 
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-13下午7:00:22
 */
public interface CustomRoleUserDao extends BaseJpaRepositoryDao<CustomRoleUser, String>{

	@Modifying
	@Query("delete from CustomRoleUser where id in (?1)")
	void deleteAllByIds(String... id);

	@Modifying
    void deleteByRoleId(String roleId);
}
