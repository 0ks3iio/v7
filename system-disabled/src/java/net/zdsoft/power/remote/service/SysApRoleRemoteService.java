package net.zdsoft.power.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.power.entity.SysApRole;

/**
 * @author yangsj  2018年6月21日上午9:31:11
 */
public interface SysApRoleRemoteService extends BaseRemoteService<SysApRole, String>{

	/**
	 * @param array
	 * @return
	 */
	String findByServerIdIn(Integer[] serverIds);

	
}
