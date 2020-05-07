package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.MonitorUrl;

public interface MonitorUrlService {
	/**
	 * 获取所有监控url
	 * 
	 * @return
	 */
	public List<MonitorUrl> getMonitorList();
}
