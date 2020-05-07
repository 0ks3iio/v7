package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datareport.data.entity.DataReportColumn;


public interface DataReportColumnService extends BaseService<DataReportColumn,String>{

	/**
	 * 根据Id查找横纵列数据
	 * @param reportId
	 * @return
	 */
	List<DataReportColumn> findByReportId(String reportId);
	
	/**
	 * 根据Id和类型查找横纵列数据
	 * @param reportId
	 * @param type 
	 * @return
	 */
	List<DataReportColumn> findByIdAndType(String reportId, Integer type);

	/**
	 * 删除列名数据
	 * @param reportId
	 */
	void deleteByReportId(String reportId);

}
