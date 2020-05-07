package net.zdsoft.studevelop.data.service;

/**
 * 
 * @author weixh
 * 2019年6月18日	
 */
public interface StudevelopTemplateDataService {
	/**
	 * 同学期数据，从旧表拷贝到新表
	 * @param unitId
	 * @param acadyear
	 */
	public void saveTempData(String unitId, String acadyear, String semester);
	
	/**
	 * 同学年数据，从第1学期拷贝到第2学期
	 * @param unitId
	 * @param acadyear
	 * @param force 是否强制覆盖数据
	 */
	public void saveCopyTempData(String unitId, String acadyear, boolean force);

}
