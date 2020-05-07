package net.zdsoft.basedata.service;


import java.util.List;

import net.zdsoft.basedata.dto.CourseScheduleDto;

import net.zdsoft.basedata.entity.ClassHour;

public interface ClassHourService extends BaseService<ClassHour, String>{

	/**
	 * 
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param gradeId 可为null
	 * @return
	 */
	public List<ClassHour> findListByUnitId(String acadyear, String semester, String unitId,String gradeId,boolean isMakeTime);
	
	public String saveChange(CourseScheduleDto searchDto, String acadyear, String semester, String hourId,String time1, String time2);
	/**
	 * 
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param gradeId
	 * @param subjectIds 可为null
	 * @return
	 */
	List<ClassHour> findBySubjectIds(String acadyear, String semester,String unitId, String gradeId,  String[] subjectIds);

	public String deleteBySubjectIdTime(CourseScheduleDto searchDto, String acadyear, String semester, String hourId,
			String time1);
	
	
	public void deleteByIds(String[] ids);

	
	public List<ClassHour> makeClassNames(List<ClassHour> hourList);

}
