package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datareport.data.dto.SaveTaskResultDto;
import net.zdsoft.datareport.data.entity.DataReportResults;


public interface DataReportResultsService extends BaseService<DataReportResults,String>{

	/**
	 * 查询填报对象的填报数据
	 * @param infoId
	 * @param taskId
	 * @return
	 */
	List<DataReportResults> findByTaskId(String taskId);

	/**
	 * 分析并保存上传excel文件中的数据
	 * @param path
	 * @param taskId
	 * @param reportId
	 * @param tableType
	 * @param unitId
	 * @param ownerId
	 * @param coverage
	 * @return
	 * @throws Exception 
	 */
	String saveDataResult(String path, String taskId, String reportId,
			Integer tableType, String unitId, String ownerId, boolean coverage) throws Exception;

	/**
	 * 查询普通数据
	 * @param infoId
	 * @param type
	 */
	List<DataReportResults> findByInfoIdAndType(String infoId, Integer type);

	/**
	 * 根据问卷Id删除所有数据
	 * @param infoId
	 */
	void deleteByReportId(String infoId);

	/**
	 * 保存在线数据
	 * @param results
	 * @param taskId
	 * @param reportId
	 * @param unitId
	 * @param ownerId
	 * @throws Exception 
	 */
	void saveTaskResults(SaveTaskResultDto saveResultDto) throws Exception;

}
