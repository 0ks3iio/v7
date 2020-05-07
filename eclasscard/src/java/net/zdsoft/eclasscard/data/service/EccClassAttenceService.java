package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;
import net.zdsoft.framework.entity.Pagination;


public interface EccClassAttenceService extends BaseService<EccClassAttence, String>{

	public List<EccClassAttence> findListByPeriodNotOver(String unitId,String periodInterval,int period,String date);
	/**
	 * 定时器加入上课考勤时间到delayqueue
	 */
	public void addClassAttenceQueue(boolean isMorn);
	/**
	 * 加入到delayqueue的上课task运行
	 * @param unitId
	 * @param sectionNumber
	 * @param isEnd
	 */
	public void classTaskRun(String unitId, Integer sectionNumber,String section, boolean isEnd);

	public List<EccClassAttence> findListNotOver();
	/**
	 * 行政班根据班级找没结束考勤信息
	 * @param classId
	 * @param sectionNumber
	 * @param date
	 * @param unitId
	 * @return
	 */
	public EccClassAttence findListByClassIdNotOver(String classId,int sectionNumber,String date,String unitId);
	/**
	 * 教学班根据场地找没结束考勤信息
	 * @param placeId
	 * @param sectionNumber
	 * @param date
	 * @param unitId
	 * @return
	 */
	public EccClassAttence findListByPlaceIdNotOver(String placeId,int sectionNumber,String date,String unitId);
	
	public List<EccClassAttence> findListByStuAtt(String teacherId,Date date,String unitId,int section,String classId,String type,Pagination page);
	
	public List<EccClassAttence> findListByStuAtt(String teacherId,Date date,String unitId);
	public EccClassAttence findByIdFillName(String id);
	
	public List<EccClassAttence>  findByStudentIdSum(String studentId,String beginDate,String endDate,Pagination page);
	public Map<String, String> findSectionNumMap(String unitId);
	
	public List<EccClassAttence> findByIdsIsOver(String[] ids);
	/**
	 * 取传入时间该单位老师考勤情况
	 * @param date
	 * @param unitId
	 * @return
	 */
	public List<EccClassAttence> findListByTeaAtt(Date date,
			String unitId);
	/**
	 * 传入日期第几节课，教师考勤情况
	 * @param date
	 * @param unitId
	 * @param sectionNumber
	 * @return
	 */
	public List<EccTeaclzAttence> findListByTeaAttDetail(Date date,
			String unitId, Integer sectionNumber);
	/**
	 * 该教师在时间段类打卡情况
	 * @param teacherId
	 * @param bDate
	 * @param eDate
	 * @param page
	 * @return
	 */
	public List<EccClassAttence> findByTeacherIdSum(String teacherId,
			String bDate, String eDate, Pagination page);
	public void classRingTaskRun(String unitId, Set<String> attIds, boolean isEnd);
	public List<EccClassAttence> findByUnitIdThisDate(String date,String unitId);
	/**
	 * 根据课程中班级或学生获取课程属于的学段
	 * @param schedules
	 * @return
	 */
	public Map<String,List<CourseSchedule>> findSectionCourseMap(List<CourseSchedule> schedules);
	
	public EccClassAttence findbyPlaceIdSecNumToDay(String unitId,String placeId, Integer sectionNumber,String studentId);
	public EccClassAttence findbyPlaceIdSecNumAndDay(String unitId,
			String placeId, Integer sectionNumber, String toDay);
	public EccClassAttence findbyClassIdSecNumAndDay(String unitId,
			String classId, Integer sectionNumber, String toDay);
	public boolean saveStuAttSate(String unitId,EccClassAttence attence,String studentId,Date clockTime);
}
