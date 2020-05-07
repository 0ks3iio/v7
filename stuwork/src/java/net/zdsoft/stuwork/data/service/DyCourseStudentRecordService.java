package net.zdsoft.stuwork.data.service;
/**
 * @author wangmq
 */
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;

public interface DyCourseStudentRecordService extends BaseService<DyCourseStudentRecord, String>{
   
	/**
	 * 根据日志id查询list
	 * @param recordId
	 * @return
	 */
    public List<DyCourseStudentRecord> findListByRecordId(String recordId);
    
    public List<DyCourseStudentRecord> findListByRecordIds(String[] recordIds);
    
    /**
     * 根据班级id查找List
     * @param schoolId
     * @param acadyear
     * @param semester
     * @param week
     * @param day
     * @param classId
     * @return
     */
    public List<DyCourseStudentRecord> findListByClassId(String schoolId, String acadyear, String semester, int week, int day, String classId);
    
    
    public List<DyCourseStudentRecord> findListByDate(String schoolId, String classId, Date queryDate, String period, String teacherId);
    
    public void deleteByRecordId(String recordId);
    
    public void deleteBy(String acadyear, String semester, int week, int day, String type, String[] classIds);
    
    public List<DyCourseStudentRecord> findByWeekAndInClassId(String unitId,
			String acadyear, String semester, int week, String[] classIds);
    /**
     * 获得某个班级一周的扣分情况
     * @param unitId
     * @param acadyear
     * @param semester
     * @param classId
     * @param week
     * @return key:classId,type,day value:score
     */
	public Map<String, Float> findByClassIdAndWeek(String unitId,
			String acadyear, String semester, String[] classIds, int week);
	
	public Map<String, String> findByClassIdAndWeekRemark(String unitId,
			String acadyear, String semester, String[] classIds, int week);
    
}
