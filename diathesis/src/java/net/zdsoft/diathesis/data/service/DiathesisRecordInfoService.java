package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisRecordInfo;

import java.util.List;

/**
 * 
 * @author niuchao
 * @since  2019年4月1日
 */
public interface DiathesisRecordInfoService extends BaseService<DiathesisRecordInfo, String> {

	/**
	 * 查询自定义字段集合
	 * @param unitId
	 * @param recordIds
	 * @return
	 */
	List<DiathesisRecordInfo> findListByUnitIdAndRecordIds(String unitId, String[] recordIds);

	/**
	 * 根据主表id删除
	 * @param unitId
	 * @param recordIds
	 */
	void deleteByRecordIds(String unitId, String[] recordIds);

	/**
	 * 项目删除的时候,删除所有记录
	 * @param recordIds
	 */
	void deleteAllByRecordIds(List<String> recordIds);
}
