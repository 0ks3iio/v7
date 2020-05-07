package net.zdsoft.datareport.data.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.datareport.data.service.ReportTaskService;
import net.zdsoft.datareport.data.task.ReportTask;
import net.zdsoft.framework.delayQueue.DelayItem;
import net.zdsoft.framework.delayQueue.DelayQueueService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reportTaskService")
public class ReportTaskServiceImpl implements ReportTaskService{

	@Autowired
	private DelayQueueService delayQueueService;

	@Override
	public void addReportTask(String reportId, String startTime, String endTime,
			ReportTask reportTaskStart, ReportTask reportTaskEnd) {
		List<DelayItem<?>> itemList = new ArrayList<>();
		try {
			if (StringUtils.isNotEmpty(startTime)) {
				DelayItem<ReportTask> btStart = new DelayItem<>(reportId + "start",startTime, reportTaskStart);
				itemList.add(btStart);
			}
			DelayItem<ReportTask> btEnd = new DelayItem<>(reportId + "end",endTime, reportTaskEnd);
			itemList.add(btEnd);
			delayQueueService.addRecentlyTimeoutItems(itemList);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteReportTask(String reportId) {
		delayQueueService.addRemoveItem2Queue(reportId);
	}
	
	
}
