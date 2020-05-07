package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsRoleUser;

public interface ChartsRoleUserService {

	/**
	 * 获取用户图表角色
	 * 数据库
	 * @param userId
	 * @return
	 */
	List<ChartsRoleUser> findByUserId(String userId);

}
