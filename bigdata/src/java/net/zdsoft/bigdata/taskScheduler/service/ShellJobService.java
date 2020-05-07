package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;

public interface ShellJobService {

	/**
	 * 保存shell任务
	 * @param shellJob
	 */
	public void saveShellJob(EtlJob shellJob);
	
	/**
	 * 执行shell的job
	 * @param shellJob
	 */
	public boolean dealJob(EtlJob shellJob,String params);
	
}
