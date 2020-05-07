package net.zdsoft.basedata.remote.service;

import java.util.Date;

import net.zdsoft.basedata.entity.DateInfo;

public interface DateInfoRemoteService extends BaseRemoteService<DateInfo, String> {
	/**
	 * 获得当前日期信息
	 */
	public String findByDate(String schId, String acadyear, Integer semester, Date date);
	
	/**
	 * 获得学年学期下所有日期信息
	 */
	public String findByAcadyearAndSemester(String schId, String acadyear, Integer semester);
	/**
	 * 获得学年学期下一周的数据
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @return
	 */
	public String findByWeek(String unitId, String acadyear,
			Integer semester, Integer week);
	
	/**
	 * 获得一段时间内的节假日信息
	 */
	public String findByDates(String schId, Date startDate, Date endDate);

	public String getDate(String unitId, String acadyear, Integer semester, Date date);
}
