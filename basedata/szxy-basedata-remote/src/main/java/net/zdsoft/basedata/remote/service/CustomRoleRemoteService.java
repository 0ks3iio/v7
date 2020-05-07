package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.CustomRole;

public interface CustomRoleRemoteService extends BaseRemoteService<CustomRole,String> {

	
//	void deleteByUnitId(String unitId);
	/**
	 * 查修某个系统下人员权限角色
	 * @param unitId
	 * @param subsystem
	 * @return
	 */
	public String findListByUnitAndSubsystem(String unitId,String subsystem);
	
	/**
	 * 判断userId是不是roleCode角色
	 * @param unitId
	 * @param subsystem
	 * @param roleCode
	 * @param userId
	 * @return boolean
	 */
	public boolean checkUserRole(String unitId,String subsystem,String roleCode,String userId);

	/**
	 * 根据 id 删除 role组
	 */
	void deleteById(String id);
}
