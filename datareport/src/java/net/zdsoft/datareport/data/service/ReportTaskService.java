package net.zdsoft.datareport.data.service;

import net.zdsoft.datareport.data.task.ReportTask;

public interface ReportTaskService {

	/**
	 * 添加开始发布填报任务
	 * @param reportId
	 * @param startTime
	 * @param endTime 
	 * @param reportTaskStart
	 * @param reportTaskEnd 
	 */
	void addReportTask(String reportId, String startTime, String endTime, ReportTask reportTaskStart, ReportTask reportTaskEnd);

	/**
	 * 删除开始发布填报任务
	 * @param reportId
	 */
	void deleteReportTask(String reportId);
}
