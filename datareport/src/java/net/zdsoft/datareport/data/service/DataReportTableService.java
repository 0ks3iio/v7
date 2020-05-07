package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.dto.AttFileDto;

public interface DataReportTableService {

	/**
	 * 创建Excel模板文件
	 * @param infoId
	 * @param unitId
	 * @param titleName 
	 * @throws Exception 
	 */
	void createExcel(String infoId, String unitId, Integer tableType) throws Exception;

	/**
	 * 生成Excel文件
	 * @param infoId
	 * @param taskId
	 * @param tableType 
	 * @param type 
	 * @throws Exception 
	 */
	void exportExcel(String infoId, String taskId, Integer tableType, Integer type) throws Exception;

	String saveAttFiles(List<AttFileDto> fileDtos, String taskId, String unitId) throws Exception;

	void saveNewStructure() throws Exception;

}
