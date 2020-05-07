package net.zdsoft.bigdata.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.system.entity.BgRolePerm;

public interface BgRolePermService extends BaseService<BgRolePerm, String>{

	/**
	 * 根据角色id获取模块列表
	 * @param roleId
	 * @return
	 */
	public List<BgRolePerm> findRolePermListByRoleId(String roleId);
	
	/**
	 * 根据roleids获取数据
	 * @param roleIds
	 * @return
	 */
	public List<BgRolePerm> findRolePermListByRoleIds(String[] roleIds);
	
	/**
	 * 建立用户组和模块的关系
	 * @param roleId
	 * @param moduleIds
	 */
	public void saveRolePerm(String roleId,String moduleIds);
	
	/**
	 * 根据角色id删除模块列表
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);
}
