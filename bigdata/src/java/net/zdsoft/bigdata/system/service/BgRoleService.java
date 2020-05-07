package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgRole;

public interface BgRoleService extends BaseService<BgRole, String> {

	/**
	 * 根据单位id获取角色
	 * 
	 * @param unitId
	 * @return
	 */
	public List<BgRole> findRoleListListByUnitId(String unitId);

	/**
	 * 根据单位id和名称获取角色
	 * 
	 * @param unitId
	 * @param name
	 * @return
	 */
	public BgRole findRoleByUnitIdAndName(String unitId, String name);

	/**
	 * 获取单位最大排序号
	 * 
	 * @param unitId
	 * @return
	 */
	public Integer getMaxOrderIdByUnitId(String unitId);

	/**
	 * 
	 * 删除角色及所有相关信息
	 * 
	 * @param id
	 */
	public void deleteRole4All(String id);
}
