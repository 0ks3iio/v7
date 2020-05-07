package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgUserRole;

public interface BgUserRoleService extends BaseService<BgUserRole, String> {

	/**
	 * 根据用户获取用户权限list
	 * 
	 * @param userId
	 * @return
	 */
	public List<BgUserRole> findUserRoleListByUserId(String userId);

	/**
	 * 根据角色获取用户权限list
	 * 
	 * @param userId
	 * @return
	 */
	public List<BgUserRole> findUserRoleListByRoleId(String roleId);
	
	/**
	 * 根据模块获取用户权限list
	 * @param moduleId
	 * @return
	 */
	public List<BgUserRole> findUserRoleListByModuleId(String moduleId);

	/**
	 * 搜索用户role
	 * @param roleId
	 * @param username
	 * @param realname
	 * @return
	 */
	public List<BgUserRole> findUserListWithRole(String roleId,
			String username, String realname);
	
	/**
	 * 搜索用户module
	 * @param roleId
	 * @param username
	 * @param realname
	 * @return
	 */
	public List<BgUserRole> findUserListWithModule(String moduleId,
			String username, String realname) ;
	
	/**
	 * 保存用户权限
	 * 
	 * @param roleId
	 * @param userId
	 */
	public void saveUserRole(String roleId,String userId);
	
	/**
	 * 保存用户和模块的权限
	 * @param moduleId
	 * @param userId
	 */
	public void saveUserModuleRole(String moduleId,String userId);

	/**
	 * 根据角色id删除用户权限
	 * 
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);

	/**
	 * 根据用户id删除用户权限
	 * 
	 * @param userId
	 */
	public void deleteByUserId(String userId);
	
	/**
	 * 根据角色和用户删除
	 * @param roleId
	 * @param userId
	 */
	public void deleteByRoleIdAndUserId(String roleId,String userId);
	
	/**
	 * 根据moduleId和用户删除
	 * @param moduleId
	 * @param userId
	 */
	public void deleteByModuleIdAndUserId(String moduleId,String userId);
}
