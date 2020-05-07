/*
* Project: v7
* Author : shenke
* @(#) CustomRoleUser.java Created on 2016-10-13
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  
 * @author: shenke
 * @version: 1.0
 * 2016-10-13下午6:58:09
 */
@Entity
@Table(name = "sys_custom_role_user")
public class CustomRoleUser extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleId;
	private String userId;
	
	
	/**
	 * role_id 
	 */
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	/**
	 * role_id 
	 */
	public String getRoleId(){
		return this.roleId;
	}
	
	/**
	 * user_id 
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
	/**
	 * user_id 
	 */
	public String getUserId(){
		return this.userId;
	}
	@Override
	public String fetchCacheEntitName() {
		return this.getClass().getSimpleName();
	}
}
