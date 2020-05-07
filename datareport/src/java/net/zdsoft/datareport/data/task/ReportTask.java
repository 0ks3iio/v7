package net.zdsoft.datareport.data.task;

import net.zdsoft.datareport.data.service.DataReportTaskService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class ReportTask implements Runnable{

	private static final Logger logger = Logger.getLogger(ReportTask.class);
	
	private String reportId;
	private Boolean isEnd;
	
	public ReportTask(String reportId, Boolean isEnd) {
		this.reportId = reportId;
		this.isEnd = isEnd;
	}
	
	@Override
	public void run() {
		DataReportTaskService dataReportTaskService = Evn.getBean("dataReportTaskService");
		logger.info("发布填报任务开始，id="+reportId);
		dataReportTaskService.reportTaskRun(reportId,isEnd);
		logger.info("发布填报任务结束，id="+reportId);
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Boolean getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(Boolean isEnd) {
		this.isEnd = isEnd;
	}
	
}
