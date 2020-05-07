package net.zdsoft.system.remote.service;

public interface SystemIniRemoteService {

	/**
	 * 获取内部级参数值
	 * @param code
	 * @return
	 */
	public String findValue(String code);
	
	/**
	 * 更新value参数值
	 */
	public void updateNowvalue(String code, String nowValue);
	
	// 刷新缓存
    void doRefreshCache(String... iniid);
}
