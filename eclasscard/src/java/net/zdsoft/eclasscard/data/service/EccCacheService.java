package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.eclasscard.data.dto.courseDto;
import net.zdsoft.eclasscard.data.dto.cache.ClassAttCacheDto;
import net.zdsoft.eclasscard.data.dto.cache.StudentCacheDto;
import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.eclasscard.data.entity.EccInfo;

/**
 * 缓存用service,缓存对象用dto精简，减小缓存数据
 * @author user
 *
 */
public interface EccCacheService {
	
	/**
	 * 10分钟获取一次
	 * @param schoolId
	 * @return
	 */
	public List<StudentCacheDto> getSchoolStuListCache(String schoolId);
	
	/**
	 * 班牌今日课表，缓存5小时
	 * @param schoolId
	 * @return
	 */
	public List<courseDto> getCardToDayCourseSchedule(EccInfo eccInfo,Semester semester,String scheduleId);
	
	/**
	 * 获取学生当前上课考勤信息
	 * @param studentId
	 * @return
	 */
	public ClassAttCacheDto getStuClassAttCacheDto(String studentId);
	/**
	 * 删除缓存的信息
	 * @param studentIds
	 */
	public void deleteStuClassAttCacheDto(String... studentIds);
	
	/**
	 * 放入当前学生上课考勤信息
	 * @param studentId
	 * @param dto
	 */
	public void saveStuClassAttCacheDto(String studentId,ClassAttCacheDto dto);
	
	/**
	 * 获取当前时间的上下学考勤时段Id
	 * @param schoolId
	 * @param gradeCode
	 * @return
	 */
	public String getInOutPeroidIdCache(String schoolId,String gradeCode);
	
	public List<EccAttenceGateGrade> getInOutCacheByPeroidId(String periodId,String schoolId);

}
