/*
* Project: v7
* Author : shenke
* @(#) CustomRoleUserDto.java Created on 2016-10-18
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dto;

import java.util.Map;

import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.User;

/**
 * 頁面人員權限設置
 * @author: shenke
 * @version: 1.0
 * 2016-10-18下午4:30:49
 */
public class CustomRoleDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CustomRole customRole;
	private Map<String,User> roleUserMap;
	
	public CustomRole getCustomRole() {
		return customRole;
	}

	public void setCustomRole(CustomRole customRole) {
		this.customRole = customRole;
	}

	public Map<String, User> getRoleUserMap() {
		return roleUserMap;
	}

	public void setRoleUserMap(Map<String, User> roleUserMap) {
		this.roleUserMap = roleUserMap;
	}
	
}
