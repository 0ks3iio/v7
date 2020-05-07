package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.user.UserRole;

/**
 * Created by shenke on 2017/2/28.
 */
public interface UserRoleRemoteService extends BaseRemoteService<UserRole, String> {

    /**
     * 根据UserId查找该用户所有的角色
     * 
     * @param userId
     * @return
     */
    String findByUserId(String userId);
    
    
	/**
	 * 更新权限的id,模块权限转移
	 * 
	 * @param id
	 * @param oldId
	 */
	public void updateRemoteId(String userId, String oldUserId);


	/**
	 * @param roleId
	 * @param ids
	 * @return
	 */
	String findByRoleIdAndUserIdIn(String roleId, String[] ids);


	/**
	 * @param roleId
	 * @param array
	 */
	void deleteByRoleIdAndUserIdIn(String roleId, String[] ids);
	
	public void deleteByUserIdIn( String[] ids);
}
