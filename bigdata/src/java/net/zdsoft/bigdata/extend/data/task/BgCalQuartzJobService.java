package net.zdsoft.bigdata.extend.data.task;

import org.quartz.SchedulerException;

public interface BgCalQuartzJobService {
	
	/**
	 * 初始化定时任务
	 */
	public void initCalJob() throws SchedulerException;
	
	/**
	 * @Description: 添加一个定时任务
	 * @param jobId
	 *            业务id
	 * @param cron
	 *            时间设置，参考quartz说明文档
	 */
	public void addCalJob(String jobId, String cron)
			throws SchedulerException;
	
	/**
	 * @Description: 修改一个定时任务
	 * @param jobId
	 *            业务id
	 * @param cron
	 *            时间设置，参考quartz说明文档
	 */
	public void modifyCalJob(String jobId, String cron)
			throws SchedulerException;

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobId
	 *            业务id
	 */
	public void deleteCalJob(String jobId) throws SchedulerException;
}
