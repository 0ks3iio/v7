package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.UserDept;

public interface UserDeptService extends BaseService<UserDept, String>{

	/**
	 * 根据用户ids获取相关部门
	 * @param userIds
	 * @return
	 */
	List<UserDept> findByUserIds(String... userIds);
	
	/**
	 * 根据用户ids获取相关部门Map
	 * @param userIds
	 * @return
	 */
	Map<String,List<UserDept>> findMapByUserIds(String... userIds);
}
