package net.zdsoft.basedata.action;

import net.zdsoft.basedata.service.CustomRoleService;
import net.zdsoft.framework.action.BaseAction;

import org.springframework.beans.factory.annotation.Autowired;

public class RoleCommonAction extends BaseAction{
	
	@Autowired
	private CustomRoleService customRoleService;
	
	public static final String SUBSYSTEM_86="86";
	public static final String EDUCATION_CODE ="86_edu_admin";
	public static final String NO_ROLE_MSG ="你不是教务管理员，没有权限查看！";
	
	/**
	 * 判断用户是不是教务管理员
	 * @param userId
	 * @return
	 */
	public boolean isAdmin(String unitId,String userId) {
		return customRoleService.checkUserRole(unitId, SUBSYSTEM_86, EDUCATION_CODE, userId);
	}
}
