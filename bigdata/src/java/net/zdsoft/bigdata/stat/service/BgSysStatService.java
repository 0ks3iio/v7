package net.zdsoft.bigdata.stat.service;

public interface BgSysStatService {

	/**
	 * 概览统计
	 */
	public void summaryStat();

	/**
	 * 用量统计（主要是dhfs 数据库）
	 */
	public void usageStat();
	
	/**
	 * 模块使用情况统计
	 */
	public void moduleStat();
	
	/**
	 * 任务统计
	 */
	public void jobStat();
	
	/**
	 * 日志统计
	 */
	public void logStat();

	/**
	 * 节点状态监控
	 */
	public void nodeMonitoring();
}
