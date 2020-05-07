package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;

public interface KettleService {

	/**
	 * 保存kettle任务
	 * @param kettleJob
	 */
	public void saveKettleJob(EtlJob kettleJob);
	
	/**
	 * 执行kettle的job
	 * @param kettleJob
	 */
	public boolean dealJob(EtlJob kettleJob,String params);
	
	/**
	 * 执行kettle的trans
	 * @param kettleJob
	 */
	public boolean dealTrans(EtlJob kettleJob,String params);
}
