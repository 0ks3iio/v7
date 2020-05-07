package net.zdsoft.officework.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.officework.dto.OfficeHealthInfoDto;
import net.zdsoft.officework.entity.OfficeHealthCount;
/**
 * office_health_count
 * @author 
 * 
 */
public interface OfficeHealthCountService{
	
	/**
	 * 新增或更新count数据
	 * @param list 
	 * @param nowtime TODO
	 * @param checkDate检查日期
	 */
	void saveOrUpdate(List<OfficeHealthInfoDto> list, Date nowtime);

	/**
	 * 新增office_health_count
	 * @param list
	 */
	void saveBatch(List<OfficeHealthCount> list);


	/**
	 * 批量更新
	 */
	void updateBatch(List<OfficeHealthCount> list);

	/**
	 * 查询学生排名
	 * @param student
	 * @param queryDate
	 * @return
	 */
	Integer getRankByStudentId(Student student,String dateType,String startTime,String endTime);

	/**
	 * 根据studentId 日/周/月 获取 Map office_health_count
	 * @param studentId
	 * @param dateType
	 * @param queryDate
	 * @return
	 */
	Map<Integer,OfficeHealthCount> getOfficeHealthCountByStudentId(String studentId,String dateType,String startDate,String endDate);

	List<OfficeHealthCount> findByLastDayData(String[] array);
	
	List<OfficeHealthCount> findByTypeDateStuIds(String[] checkDates, String[] studentIds,int type);

	List<OfficeHealthCount> findByDateStuIds(String[] checkDates, String[] studentIds);
}