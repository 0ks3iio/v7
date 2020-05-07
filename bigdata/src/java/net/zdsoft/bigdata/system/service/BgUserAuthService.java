package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgUserAuth;

public interface BgUserAuthService extends BaseService<BgUserAuth, String> {

	/**
	 * 所有所有授权用户（后台）
	 * @return
	 */
	public List<BgUserAuth> findAllAuthUserList();
	
	/**
	 * 搜索用户
	 * @param unitId
	 * @param username
	 * @param realname
	 * @return
	 */
	public List<BgUserAuth> findUserList(String unitId,String username, String realname);

	/**
	 * 保存用户授权
	 * 
	 * @param userId
	 */
	public void saveUserAuth(String userId);
	
	/**
	 * 是否后台正常用户
	 * 
	 * @param userId
	 */
	public boolean isBackgroundUser(String userId,int userType);

	/**
	 * 是否超级用户
	 * @param userId
	 * @return
	 */
	public boolean isSuperUser(String userId);

	/**
	 * 更新用户状态
	 * @param id
	 * @param status
	 */
	public void updateStatusById(String id, Integer status);

	/**
	 * 更新是否超级用户 1是 0不是
	 * @param id
	 * @param isSuperUser
	 */
	public void updateSuperUserById(String id, Integer isSuperUser);

}
