package net.zdsoft.power.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.power.entity.SysUserPower;

/**
 * @author yangsj  2018年6月20日上午10:36:47
 */
public interface SysUserPowerRemoteService extends BaseRemoteService<SysUserPower, String>{

	/**
	 * @param userName
	 */
	String findPowerByUserName(String userName);

}
