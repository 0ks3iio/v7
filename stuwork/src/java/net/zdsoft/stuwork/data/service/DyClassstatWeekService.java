package net.zdsoft.stuwork.data.service;


import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DyClassStatListDto;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;

public interface DyClassstatWeekService extends BaseService<DyClassstatWeek, String> {
	/**
	 * 获得某个班级某一周的考核汇总数据
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classId
	 * @param week
	 * @return
	 */
	public List<DyClassStatListDto> findClassTableDto(String unitId,
			String acadyear, String semester, String classId, int week);
	
	/**
	 * 获取班级上周的统计情况。
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param classId
	 * @param date 为null取当前是
	 * @return 若date所在的周次不存在或为第一周 则返回null
	 */
	public DyClassstatWeek findBySchoolIdAndAcadyearAndSemesterAndClassIdAndDate(
			String schoolId, String acadyear, String semester, String classId, Date date);
	
	public DyClassstatWeek findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(
			String schoolId, String acadyear, String semester, String classId, int week);
	/**
	 * 
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @return
	 */
	public List<DyClassstatWeek> findBySchoolIdAndAcadyearAndSemesterAndWeek(String schoolId, String acadyear, String semester, int week);
	/**
	 * 统计一个学校所有班级一周的考核汇总信息
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param week
	 */
	public void statByUnitId(String unitId, String acadyear, String semester,
			int week);

	/**
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @param classIds
	 * @return
	 */
	public List<DyClassstatWeek> findRankingList(String unitId, String acadyear, String semester, Integer[] array,
			String[] classIds);
	
}
