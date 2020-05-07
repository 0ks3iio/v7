package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;

public interface FlinkJobService {

	/**
	 * 保存shell任务  做日志时 要区分流计算和批处理 这里传一个type,1为批处理，2为流计算
	 * @param flinkJob
	 */
	public void saveFlinkJob(EtlJob flinkJob,Integer type);

	/**
	 * 执行shell的job
	 * @param flinkJob
	 */
	public boolean dealJob(EtlJob flinkJob, String params);
	
}
