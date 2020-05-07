package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.StusysSectionTimeSet;

public interface StusysSectionTimeSetRemoteService extends BaseRemoteService<StusysSectionTimeSet,String> {
	/**
	 * 
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param section 学段数组
	 * @param upToFind  向上查找，业务调用方传true(如没按照学段设置，去找按单位设置的)
	 * @return
	 */
	public String findByAcadyearAndSemesterAndUnitId(String acadyear, Integer semester, String unitId,String[] section,boolean upToFind);

	public String findByUnitIdIn(String[] unitIds);
	
	
	
}
