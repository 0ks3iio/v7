package net.zdsoft.power.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.power.entity.SysApPower;

/**
 * @author yangsj  2018年6月7日下午2:18:59
 */
public interface SysApPowerService extends BaseService<SysApPower, String> {

	/**
	 * @param powerId
	 * @param serverId
	 */
	void deleteBypowerIdAndServerId(String powerId, Integer serverId);

	/**
	 * @param serverId
	 * @return
	 */
	List<SysApPower> findByServerId(Integer serverId);

	/**
	 * @param serverIds
	 * @return
	 */
	List<SysApPower> findByServerIdIn(Integer[] serverIds);

	/**
	 * @param powerId
	 * @return
	 */
	SysApPower findByPowerId(String powerId);

	/**
	 * @param array
	 * @param unitId
	 * @return
	 */
	List<SysApPower> findByServerIdInAndUnitId(Integer[] serverIds, String unitId);

	/**
	 * @param valueOf
	 * @param unitId
	 * @return
	 */
	List<SysApPower> findByServerIdAndUnitId(Integer serverId, String unitId);

}
