/*
* Project: v7
* Author : shenke
* @(#) CustomRoleUserDto.java Created on 2016-10-19
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.CustomRoleUser;

/**
 * 
 * @author: shenke
 * @version: 1.0
 * 2016-10-19上午8:43:28
 */
public class CustomRoleUserDto extends BaseDto{
	private static final long serialVersionUID = 1L;
	
	private CustomRoleUser customRoleUser;
	private String userName;
	public CustomRoleUser getCustomRoleUser() {
		return customRoleUser;
	}
	public void setCustomRoleUser(CustomRoleUser customRoleUser) {
		this.customRoleUser = customRoleUser;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
