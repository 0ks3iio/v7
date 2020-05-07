package net.zdsoft.power.service;

import java.util.Collection;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.power.entity.SysApRole;

/**
 * @author yangsj  2018年6月7日下午2:20:04
 */
public interface SysApRoleService extends BaseService<SysApRole, String> {

	/**
	 * 根据roleId来删除数据
	 * @param roleId
	 */
	void deleteByRoleId(String roleId);

	/**
	 * 根据serverId得到ap对应的角色
	 * @param serverId
	 * @return List<SysApRole>
	 */
	List<SysApRole> findByServerId(Integer serverId);

	/**
	 * 根据serverIds得到ap对应的角色
	 * @param serverIds
	 * @return
	 */
	List<SysApRole> findByServerIdIn(Integer[] serverIds);

	/**
	 * 根据roleId得到数据
	 * @param roleId
	 */
	SysApRole findByRoleId(String roleId);

	/**
	 * @param serverId
	 * @param unitId
	 * @return
	 */
	List<SysApRole> findByServerIdAndUnitId(Integer serverId, String unitId);

	/**
	 * @param serverIds
	 * @param unitId
	 * @return
	 */
	List<SysApRole> findByServerIdInAndUnitId(Integer[] serverIds, String unitId);

}
