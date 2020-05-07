package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;

public interface PythonJobService {

	/**
	 * 保存shell任务
	 * @param pythonJob
	 */
	public void savePythonJob(EtlJob pythonJob);

	/**
	 * 执行shell的job
	 * @param pythonJob
	 */
	public boolean dealJob(EtlJob pythonJob, String params);
	
}
