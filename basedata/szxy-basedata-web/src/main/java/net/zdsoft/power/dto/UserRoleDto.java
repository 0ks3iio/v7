package net.zdsoft.power.dto;

import net.zdsoft.basedata.entity.User;

/**
 * @author yangsj  2018年6月12日下午2:12:39
 */
public class UserRoleDto {

	private String roleId;
	private User user;
	private String isAppoint; //是否已经委派
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getIsAppoint() {
		return isAppoint;
	}
	public void setIsAppoint(String isAppoint) {
		this.isAppoint = isAppoint;
	}
}
