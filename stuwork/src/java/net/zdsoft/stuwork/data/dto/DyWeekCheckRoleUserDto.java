package net.zdsoft.stuwork.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;

public class DyWeekCheckRoleUserDto {
	
	private List<DyWeekCheckRoleUser> roles = new ArrayList<DyWeekCheckRoleUser>();
	
	public List<DyWeekCheckRoleUser> getRoles() {
		return roles;
	}

	public void setRoles(List<DyWeekCheckRoleUser> roles) {
		this.roles = roles;
	}
	
	
}
