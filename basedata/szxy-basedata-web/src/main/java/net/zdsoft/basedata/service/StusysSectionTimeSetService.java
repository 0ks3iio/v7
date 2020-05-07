package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.StusysSectionTimeSet;

public interface StusysSectionTimeSetService extends BaseService<StusysSectionTimeSet, String> {
	/**
	 * 
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param section 学段数组
	 * @param upToFind  向上查找，业务调用方传true(如没按照学段设置，去找按单位设置的)
	 * @return
	 */
	public List<StusysSectionTimeSet> findByAcadyearAndSemesterAndUnitId(String acadyear, Integer semester, String unitId,String[] section,boolean upToFind);

	public List<StusysSectionTimeSet> findByUnitIdIn(String[] unitIds);
	
}
