package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsUser;

public interface ChartsUserService {

	/**
	 * 获取
	 * 数据库
	 * @param userId
	 * @return
	 */
	List<ChartsUser> findByUserId(String userId);

}
