package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.TipsayDto;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.basedata.entity.TipsayEx;

public interface TipsayService extends BaseService<Tipsay, String> {
	/**
	 * 
	 * @param schoolId
	 *            不能为空
	 * @param acadyear
	 *            不能为空
	 * @param semester
	 *            不能为空
	 * @param weekOfWorkTime
	 * 
	 * @param teacherId (原老师或者原辅助老师，或者代课老师 三者包括一个)
	 * @return 根据创建时间降序
	 */
	public List<Tipsay> findTipsayListWithMaster(String schoolId,
			String acadyear, Integer semester, Integer weekOfWorkTime,String teacherId);

	public void saveAll(Tipsay tipsay, TipsayEx tipsayEx,
			CourseSchedule courseSchedule);

	public void deleteByTipsayId(String tipsayId);

	public void deleteTipsayOrSaveCourseSchedule(Tipsay tipsay,
			CourseSchedule courseSchedule);


	/**
	 * 组装数据
	 * @param dateByWeek
	 * @param tipsayList
	 * @param needHeadPic 是否需要头像
	 * @return
	 */
	public List<TipsayDto> tipsayToTipsayDto(Map<String, String> dateByWeek,
			List<Tipsay> tipsayList,boolean needHeadPic);
	
	/**
	 * 辅助：验证教师在courseSchedule所在的时间有没有其他课程
	 * 
	 * @param ttIds
	 *            教师id
	 * @param courseSchedule
	 * @return
	 */
	public String checkTimeByTeacher(List<String> ttIds,
			CourseSchedule courseSchedule);
	/**
	 * 辅助：查询班级名称
	 * @param classIds
	 * @return
	 */
	public Map<String, String> getClassNameMap(String[] classIds);
	/**
	 * 验证courseSchedule与代课的课程是否一致
	 * @param courseSchedule
	 * @param tipsay
	 * @param isContainTeacher 是否包括教师字段
	 * @return
	 */
	public boolean checkCourseSchedule(CourseSchedule courseSchedule,
			Tipsay tipsa,Boolean isContainTeacher) ;
	/**
	 * 验证能不能撤销
	 * @param tipsay
	 * @param courseSchedule
	 * @return
	 */
	public String checkCanDeleted(Tipsay tipsay, CourseSchedule courseSchedule);

	public String checkTimesByTeacher(String acadyear, String semester, int minWorkTime, int maxWorkTime,
			String newTeacherId, List<CourseSchedule> scheduleList);
	/**
	 * 
	 * @param tipsayList
	 * @param tipsayExList
	 * @param scheduleList
	 * @param newTeacherId
	 * @param oldTeacherId
	 * @param applyType
	 */
	public void saveAllApplyList(String unitId,List<Tipsay> tipsayList, List<TipsayEx> tipsayExList, 
			List<CourseSchedule> scheduleList,String newTeacherId,String oldTeacherId,String applyType);

	/**
	 * 根据条件查询代课记录
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param dates
	 * @param type
	 * @param teacherIds
	 * @return
	 */
	public List<Tipsay> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String type, String[] teacherIds);

	/**
	 * 申请教务安排
	 * @param array
	 */
	public void saveApplySelfList(String unitId,String applyTeacherId,List<Tipsay> tiplayList);
	
	/**
	 * 消息推送
	 * @param teacherIds
	 * @param userIds
	 * @param content
	 * @param title
	 */
	public void pushMessage(String[] teacherIds,String[] userIds,String content,String title) ;
	/**
	 * 找到调代管课管理员
	 * @param unitId
	 * @return userId
	 */
    public List<String> findUserRole(String unitId);

    /**
     * 删除老师后清空代课表中的teacherId
     * @param teacherId
     */
	public void updateByTeacherId(String teacherId);

	/**
	 * 删除班级后删除代课表里的相关数据
	 * @param classId
	 */
	public void deleteByClassId(String classId);

	/**
	 * 根据课程查询代课记录
	 * @param subjectId
	 * @return
	 */
	public List<Tipsay> findBySubjectId(String subjectId);

}
