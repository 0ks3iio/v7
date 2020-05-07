package net.zdsoft.basedata.dingding.service;

public interface DingDingSyncDataService {

	/**
	 * 定时执行
	 */
	public void dealData2DingDingByUpdateTime();
	
	
	/**
	 * 手工执行
	 */
	public void dealData2DingDing();
	
	/**
	 * 从钉钉获取部门数据
	 */
	public void getDepts();
	
	/**
	 * 从钉钉获取用户数据
	 */
	public void getUsers();
}
