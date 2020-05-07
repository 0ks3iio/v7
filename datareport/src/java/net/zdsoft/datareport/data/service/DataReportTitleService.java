package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datareport.data.entity.DataReportTitle;

public interface DataReportTitleService extends BaseService<DataReportTitle,String>{

	/**
	 * 获取模板的表头表尾备注数据
	 * @param infoId
	 * @return
	 */
	List<DataReportTitle> findByReportId(String infoId);

	/**
	 * 删除表头
	 * @param reportId
	 */
	void deleteByReportId(String reportId);

	/**
	 * 根据类型和id获取数据
	 * @param type
	 * @param reportId
	 * @return
	 */
	DataReportTitle findByTypeAndId(Integer type, String reportId);

}
