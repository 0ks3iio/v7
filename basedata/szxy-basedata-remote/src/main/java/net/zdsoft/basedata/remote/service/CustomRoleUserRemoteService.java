package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.CustomRoleUser;

public interface CustomRoleUserRemoteService extends BaseRemoteService<CustomRoleUser,String> {
	/**
	 * 此用户是否属于指定角色
	 * @param userId
	 * @param roleCode
	 * @return boolean
	 */
	boolean containRole(String userId, String roleCode);

	/**
	 * <p>更新user-角色
	 * <p>userIds为空customRoleId不为空则删除
	 * <p>userIds为空customRoleId为空不做任何操作
	 * 
	 * @param userIds 可以为空。如果为空时，且customRoleId 非空，则做删除；customRoleId为空，则不做任何操作
	 *            array
	 * @param customRoleId 可以为空。如果为空，且userIds为空，则不做任何操作
	 */
//	void saveCustomRoleUsers(String[] userIds, String customRoleId);
	/**
	 * 
	 * @param customRoleIds
	 * @return List<String> userId
	 */
	public String findUserIdsByCustomRoleIdIn(String[] customRoleIds);

    void deleteByRoleId(String roleId);
}
