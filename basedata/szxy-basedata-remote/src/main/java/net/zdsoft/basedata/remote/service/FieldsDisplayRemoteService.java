package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.FieldsDisplay;

public interface FieldsDisplayRemoteService extends BaseRemoteService<FieldsDisplay,String>{
	/**
	 * 根据schGUID、类型,得到显示信息设置记录
	 * @param unitId
	 * @param type
	 * @return
	 */
	public String findByColsDisplays(String unitId, String type);
	
	/**
	 * 得到要设置的数据项列表，根据某校unitid、type、colsUse是否显示属性
	 * @param unitId
	 * @param type
	 * @param colsUse
	 * @return
	 */
	public String findByColsDisplays(String unitId, String type, Integer colsUse);
	
	/**
	 * 取得下级显示
	 * @param parentId
	 * @param colsUse
	 * @return
	 */
	public String findByColsDisplays(String parentId, Integer colsUse);

}
