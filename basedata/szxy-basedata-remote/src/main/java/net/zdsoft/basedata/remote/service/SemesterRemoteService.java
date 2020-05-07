package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Semester;

import java.util.Date;

public interface SemesterRemoteService extends BaseRemoteService<Semester,String> {

	/**
	 * 学年学期接口类型， type值， 如果当前时间不在任何一个学期，则为空
	 */
	public static final int RET_NULL = 0;
	/**
	 * 学年学期接口类型， type值， 如果当前时间不在任何一个学期，则返回前一个学期
	 */
	public static final int RET_PRE = 1;
	/**
	 * 学年学期接口类型， type值， 如果当前时间不在任何一个学期，则返回后一个学期
	 */
	public static final int RET_NEXT = 2;

	/**
	 * 返回当前时间的学年学期 若当前时间不在学期时间内 则根据type返回
	 * 
	 * @param type
	 *            当前时间不在任何一个学期时处理方式： 0=返回空；1=返回前一个学期；2=返回后一个学期
	 * @return Semester
	 */
	public String getCurrentSemester(int type);

	/**
	 * 获取所有教育局设置的，未删除的学年
	 * 
	 * @return List&lt;Semester&gt;
	 */
	public String findAcadeyearList();

	/**
	 * @param currentDay  根据当前的时间，获得当前学年学期的信息,YYYY-mm-DD
	 * @return
	 */
	public String findByCurrentDay(String currentDay);

	/**
	 * @param currentDay
	 * @param i   根据当前的时间，获得当前学年学期的信息
	 * @return
	 */
	public String findByCurrentDay(String currentDay, int i);

	String findByAcadyearAndSemester(String acadyear,Integer semester,String unitId);
	
	/**
	 * @param acadyear
	 * @param semester
	 * @return    根据学年学期取得学期信息
	 */
	public String findByAcadYearAndSemester(String acadyear, Integer semester);

	/**
	 * @return
	 */
	public String findSemesters();

	/**
	 * @return
	 */
	public String findDistinctSemesters();

	/**
	 * @param date
	 * @return
	 */
	public String findSemestersByDate(Date date);
	
	
	/**
	 * 获取所有教育局设置的，未删除的学期，date以及以后的学期
	 * @param date
	 * @return List<Semester>;
	 */
	public String findListByDate(Date date);

	/**
	 * 先查找当前学校的学年学期，若不粗存在则取base_semester
	 * @param type 当前时间不在任何一个学期时处理方式： 0=返回空；1=返回前一个学期；2=返回后一个学期
	 * @param schoolId
	 * @return
	 */
	String getCurrentSemester(int type, String schoolId);
	

}
