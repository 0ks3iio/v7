package net.zdsoft.stuwork.remote.service;

import net.zdsoft.stuwork.data.dto.DyBusinessOptionDto;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface StuworkRemoteService {
	
	/**
	 * 查找寝室楼
	 * @param unitId
	 */
	public String getBuildingSbyUnitId(String unitId);
	
	/**
	 * 获取那一天上课日志分数
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param queryDate
	 * @param classId
	 * @return
	 */
	public String getCourseRecordData(String unitId, String acadyear, String semester, Date queryDate, String classId);
	
	public String getStudentLeaveDate(String studentId);
	/**
	 * 找到行政班一天内各个考核项的扣分情况,只会返回被扣分的数据
	 * 1 值周卫生 2值周纪律
	 * @param unitId
	 * @param classId
	 * @param dutyDate
	 * @return List<String[]{type，score，remark}>
	 */
	public String findWeekCheckByClassId(String unitId,String acadyear,String semester,String classId,Date dutyDate);
	/**
	 * 找到班级下各个学生所在的寝室楼
	 * @param unitId
	 * @param acadyear TODO
	 * @param semester TODO
	 * @param classIds
	 * @return List<String[]{studentId,classId,buildingId}>
	 */
	public String findStuRoomByClassIds(String unitId,String acadyear, String semester, String[] classIds);
	/**
	 * 找到班级下各个学生所在的寝室号名称
	 * @param unitId
	 * @param acadyear TODO
	 * @param semester TODO
	 * @param classIds
	 * @return List<String[]{studentId,classId,buildingId}>
	 */
	public String findStuRoomNameByClassIds(String unitId, String acadyear, String semester, String[] classIds);
	/**
	 * 找到对应寝室楼下的房间数和人数
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param buildingId
	 * @return 
	 */
	public String findRoomNumAndStuNumByBuildingId(String unitId,String acadyear,String semester,String buildingId);
	/**
	 * 根据studentId等找到对应的住宿信息
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param studentId
	 */
	public String findbuildRoomByStudentId(String studentId,String unitId,String acadyear,String semester);
	/**
	 * 根据studentId判断是否住校生
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param studentId
	 */
	public String findIsInResidence(String studentId,String unitId,String acadyear,String semester);
	/**
	 * 根据寝室楼id 获取参数roomName remark  寝室提醒
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param studentId
	 */
	public String findRemindByBuildingId(String buildingId,Date checkDate,String unitId);
	/**
	 * 根据寝室楼id 获取参数roomName score remark 寝室扣分
	 * @param unitId
	 * @param buildingId
	 * @param  
	 * @param inputDate
	 */
	public String findResultByBuildingId(String buildingId,Date inputDate,String unitId);
	/**
	 * 根据classId 获取参数name score remark  班级扣分
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param inputDate
	 */
	public String findClassResult(String classId,Date inputDate,String unitId,String acadyear,String semester);
	/**
	 * 根据classId 获取参数 name remark  班级提醒
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param checkDate
	 */
	public String findClassRemind(String classId,Date checkDate,String unitId,String acadyear,String semesterStr);
	/**
	 * 根据unitId   获取studentId 对应的roomName map
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param buildingId
	 */
	public String findStuIdsByBuiId(String unitId,String buildingId,String acadyear,String semesterStr);
	/**
	 * 根据unitId  buildingId获取学生ids
	 * @param unitId
	 * @param acadyear 
	 * @param semester 
	 * @param buildingId
	 */
	public String findStuIdRnameMapByBuiId(String unitId,String buildingId,String acadyear,String semesterStr);
	/**
	 * 根据学年学期单位id获取寝室号
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public String findDyDormBedByUnitId(String unitId, String acadyear, String semester, String[] studentIds);
	
	public String findDyDormByIds(String[] dormIds);
	
	/**
	 * 德育报表使用的
	 */
	public String findStuworkCountByStudentId(Map<String,Integer> maxValueMap,String studentId,Integer maxValuePer, boolean isShow);
	
	public String findStuworkCountByStudentIds(Map<String,Integer> maxValueMap,String classId, String[] studentIds,Integer maxValuePer, boolean isShow);
	
	/**
	 * 德育报表使用的
	 */
	public String findStuworkCountByUnitId(Map<String,Integer> maxValueMap,String unitId,Integer maxValuePer,Map<String, Boolean> showMap);
	/**
	 * 修复学处理
	 */
	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear);
	
	/**
	 * 查询该用户的班级权限(班主任有班级权限，年级组长有年级下所有班级权限)
	 * @param userId
	 * @return  班级id的set
	 */
	public Set<String> findClassSetByUserId(String userId);
	/**
	 * 获取德育汇总的结果  
	 * @param unitId
	 * @param type
	 * @param acadyear
	 * @param semester
	 * @param studentIds
	 * @return
	 */
	public String findCollectList2(String unitId,String acadyear,String semester,String[] studentIds);
	/**
	 * 获取节日汇总的结果  
	 * @param unitId
	 * @param type
	 * @param acadyear
	 * @param semester
	 * @param studentIds
	 * @return
	 */
	public String findCollectList4(String unitId,String acadyear,String semester,String[] studentIds);
	/**
	 * 查询该用户的班级权限(班主任有班级权限，年级组长有年级下所有班级权限)
	 * @param userId
	 * @return  班级id的set
	 */
	public Set<String> findClassSetByUserIdClaType(String userId,String classsType,String permissionType);

	/**
	 * 查询list
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public String findStuEvaluationListByUidAndDto(String unitId,DyBusinessOptionDto dto);
	/**
	 * 查询DyStuEvaluation
	 * @param unitId
	 * @param dto
	 * @return
	 */
	public String findStuEvaluationOneByUidAndDto(String unitId,DyBusinessOptionDto dto);

	/**
	 * 查询DyStuEvaluation
	 * @param unitId
	 * @return
	 */
	public String findStuEvaluationListByUnitIdAndStudentIds(String unitId, String acadyear, String semester, String[] studentIds);
	
	public String findStuHealthResultOneByStudnetId(String unitId, String acadyear, String semester, String studentId);
	public String findStuHealthResultListByStudentIds(String unitId, String acadyear, String semester, String[] studentIds);

	public String findXkjsByStudentIds(String unitId, String acadyear, String semester, String[] studentIds);
	
	
}
