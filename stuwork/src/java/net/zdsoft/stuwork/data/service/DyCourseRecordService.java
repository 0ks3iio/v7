package net.zdsoft.stuwork.data.service;

/**
 * @author wangmq
 */
import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;

public interface DyCourseRecordService extends
		BaseService<DyCourseRecord, String> {

	/**
	 * 日志保存
	 * 
	 * @param dyCourseRecord
	 * @param stuIds
	 */
	public void save(DyCourseRecord dyCourseRecord,
			List<DyCourseStudentRecord> dyCourseStudentRecordList);
	
	/**
	 * 晚自习日志保存
	 * @param dyCourseRecord
	 * @param dyCourseStudentRecordList
	 */
	public void saveNightCourseRecord(List<DyCourseRecord> dyCourseRecordList, List<DyCourseStudentRecord> dyCourseStudentRecordList, String acadyear, String semester, int week, int day, String type, String[] classIds);

	/**
	 * 晚自习日志删除
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @param day
	 * @param classIds
	 */
	public void deleteBy(String acadyear, String semester, int week, int day, String type, String[] classIds);
	
	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	public DyCourseRecord findById(String id);

	/**
	 * 根据教师课表查询
	 * 
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param type
	 * @param teacherId
	 * @param week
	 * @param day
	 * @param period
	 * @return
	 */
	public DyCourseRecord findByAll(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day,
			int period);
	
	/**
	 * 个人晚自习查找
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param type
	 * @param teacherId
	 * @param week
	 * @param day
	 * @return
	 */
	public DyCourseRecord findBy(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day);
	
	/**
	 * 教师那一天上课list
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param type
	 * @param teacherId
	 * @param week
	 * @param day
	 * @return
	 */
	public List<DyCourseRecord> findListBy(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day);
	
	public List<DyCourseRecord> findListByPeriod(String schoolId, String acadyear,
			String semester, String type, int week, int day, int period);
	
	/**
	 * 根据日期查找晚自习日志
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param type
	 * @param recordDate
	 * @return
	 */
	public List<DyCourseRecord> findListByRecordDate(String schoolId, String type, Date recordDate);
	
	public List<DyCourseRecord> findListByDateClsIds(String schoolId, Date recordDate, String[] clsIds);

	
	public List<DyCourseRecord> findListByRecordClassId(String schoolId, String acadyear, String semester, int week, int day, String type, String classId);
	
	public List<DyCourseRecord> findListByRecordClassIds(String schoolId, String acadyear, String semester, int week, String[] classIds);

}
