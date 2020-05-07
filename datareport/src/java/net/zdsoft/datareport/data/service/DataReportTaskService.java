package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datareport.data.entity.DataReportTask;
import net.zdsoft.framework.entity.Pagination;

public interface DataReportTaskService extends BaseService<DataReportTask,String>{

	/**
	 * 获取任务填报List
	 * @param title
	 * @param objectId 
	 * @param state 
	 * @param page
	 * @return
	 */
	List<DataReportTask> findTaskInfoList(String objectId, Integer state, Pagination page);

	/**
	 * 查看任务填报详情
	 * @param taskId
	 * @return
	 */
	DataReportTask findByTaskInfo(String taskId);
	
	/**
	 * 修改填报任务状态
	 * @param state
	 * @param taskId 
	 */
	void updateState(Integer state, String taskId);

	/**
	 * 获取非定时任务单详情
	 * @param infoId
	 * @param page 
	 * @return
	 */
	List<DataReportTask> findByReportId(String infoId, Pagination page);

	/**
	 * 定时开始填报任务
	 * @param reportId
	 */
	void reportTaskRun(String reportId, Boolean isEnd);

	/**
	 * 删除任务
	 * @param infoId
	 */
	void deleteByReportId(String infoId);


}
