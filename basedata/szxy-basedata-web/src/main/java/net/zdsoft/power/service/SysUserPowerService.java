package net.zdsoft.power.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.power.entity.SysUserPower;

/**
 * @author yangsj  2018年6月7日下午2:21:52
 */
public interface SysUserPowerService extends BaseService<SysUserPower, String> {

	/**
	 * @param userId
	 * @param typeUserValue
	 * @return
	 */
	List<SysUserPower> findByTargetIdAndType(String targetId, int type);

	/**
	 * @param targetId
	 * @param pids
	 * @param type
	 */
	void deleteByTargetIdAndPowerIdInAndType(String targetId, String[] pids, int type);

	/**
	 * @param type
	 * @param targetIds
	 * @return
	 */
	List<SysUserPower> findByTypeAndTargetIdIn(int type, String[] targetIds);

	/**
	 * @param targetId
	 * @param typeRoleValue
	 */
	void deleteByTargetIdAndType(String targetId, int type);

}
