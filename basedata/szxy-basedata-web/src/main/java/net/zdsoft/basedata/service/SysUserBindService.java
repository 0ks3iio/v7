package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.SysUserBind;



public interface SysUserBindService {

	/**
	 * 保存信息sys_user_bind
	 * @param sysUserBind
	 */
	public void save(SysUserBind sysUserBind);

	/**
	 * 根据remoteUserIds数组删除sys_user_bind数据
	 * @param remoteUserIds
	 * @return
	 */
	public void deleteByRemoteUserIdIn(String[] remoteUserIds);
	
	/**
	 * 根据userids 数组删除 sys_user_bind数据
	 * @param userIds
	 */
	public void deleteByUserIdIn(String[] userids);

	/**
	 * 根据remoteUserId获取sys_user_bind
	 * @param remoteUserId
	 * @return
	 */
	public SysUserBind getSysUserBindById(String remoteUserId);
	
	/**
	 * 根据remoteUsername获取sys_user_bind
	 * @param remoteUsername
	 * @return
	 */
	public SysUserBind findByRemoteUsername(String remoteUsername);
	
	/**
	 * 根据userId获取sys_user_bind
	 * @param userId
	 * @return
	 */
	public SysUserBind findByUserId(String userId);
	
}
