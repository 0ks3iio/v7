package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsRole;

public interface ChartsRoleService {

	/**
	 * 获取用户图表角色启用的
	 * 缓存
	 * @param userId
	 * @return
	 */
	List<ChartsRole> findByUserId(String userId);

	/**
	 * 找所有启用的
	 * 数据库
	 * @return
	 */
	List<ChartsRole> findAll();

}
