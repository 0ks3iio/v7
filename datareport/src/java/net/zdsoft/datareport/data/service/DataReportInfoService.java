package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datareport.data.dto.DataReportSaveDto;
import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.framework.entity.Pagination;

public interface DataReportInfoService extends BaseService<DataReportInfo,String>{

	/**
	 * 通过单位Id查找当前单位所发布的所有问卷任务
	 * @param title 
	 * @param unitId
	 * @param page
	 * @return
	 */
	List<DataReportInfo> findInfoList(String title, String unitId, Pagination page);
	
	/**
	 * 删除或撤销报表任务
	 * @param infoId
	 * @param unitId
	 * @param type 
	 * @throws Exception 
	 */
	void deleteOrRevokeInfo(String infoId, String unitId, Integer type) throws Exception;
	
	/**
	 * 新增填报任务
	 * @param columnDto 
	 * @param isSub 
	 */
	DataReportInfo saveOneInfo(DataReportSaveDto saveDto, boolean isSub);
	
	/**
	 * 修改任务单的状态
	 * @param state
	 * @param reportId
	 */
	void updateState(Integer state, String reportId);

	/**
	 * 添加问卷发布任务队列
	 */
	void addTaskQueue();

	/**
	 * 查找相同名称
	 * @param infoTitle
	 * @param unitId
	 * @return
	 */
	DataReportInfo findSameTitle(String infoTitle, String unitId);

	/**
	 * 保存复制问卷
	 * @param infoId
	 * @return
	 */
	void saveCopyInfo(String infoId);

	void updateTime(String infoId, String endTime, Integer state, String unitId) throws Exception;

}
