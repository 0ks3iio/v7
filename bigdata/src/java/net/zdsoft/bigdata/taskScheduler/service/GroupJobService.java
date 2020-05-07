package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;

public interface GroupJobService {

	/**
	 * 保存group任务
	 * @param groupJob
	 * @param  jobIds
	 */
	public void saveGroupJob(EtlJob groupJob,String jobIds);

	/**
	 * 执行group的job
	 * @param groupJob
	 */
	public boolean dealJob(EtlJob groupJob, String params);
	
}
