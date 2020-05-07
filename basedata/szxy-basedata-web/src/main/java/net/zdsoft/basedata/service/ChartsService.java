package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.Charts;

public interface ChartsService {
	
	//刷新缓存
	void doRefreshCacheAll();
	//删除用户图表缓存
	void deleteCacheUser(String[] userIds);
	
	/**
	 * 获取图表列表
	 * 缓存
	 * @param modelType	1为sys_model模块,2为sys_platform_model模块。默认为1
	 * @param modelId	
	 * @param userId	
	 * @param tagType 区分同个页面下，可为null
	 * @return
	 */
	List<Charts> findChartsByModelIdAndUserId(String modelType,Integer modelId,String userId, String tagType);
	
	/**
	 * 根据角色获取启用的图表
	 * 缓存
	 * @param roleIds
	 * @return
	 */
	List<Charts> findChartsByRoleId(Integer... roleIds);
	
	/**
	 * 获取用户的图表
	 * 缓存
	 * @param userId
	 * @return
	 */
	List<Charts> findChartsByUserId(String userId); 
	
	/**
	 * 获取模块的图表
	 * 缓存
	 * @param userId
	 * @return
	 */
	List<Charts> findChartsByModelId(String modelType,Integer modelId, String tagType);
	
	/**
	 * 获取启用的图表  orderid升序
	 * 数据库
	 * @param userId
	 * @return
	 */
	List<Charts> findAll(Integer[] intIds); 
	
}
