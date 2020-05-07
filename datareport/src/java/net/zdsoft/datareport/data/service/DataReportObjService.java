package net.zdsoft.datareport.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.datareport.data.entity.DataReportObj;

public interface DataReportObjService extends BaseService<DataReportObj,String>{

	/**
	 * 根据主表Ids获得所有发布对象的信息
	 * @param infoId
	 * @param needName  是否需要发布对象名称
	 * @return
	 */
	List<DataReportObj> findByReportIds(String[] infoIds, boolean needName);

	/**
	 * 根据填报对象ID查找所有的发布对象
	 * @param customerId
	 * @return
	 */
	List<DataReportObj> findByObjectId(String objectId);

	/**
	 * 根据填报表的Id删除所有对象
	 * @param reportId
	 */
	void deleteByReportId(String reportId);

	DataReportObj findById(String objId);

	List<DataReportObj> findByObjIds(String[] objIds);
}
