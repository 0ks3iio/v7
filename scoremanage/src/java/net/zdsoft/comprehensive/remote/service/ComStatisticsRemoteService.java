package net.zdsoft.comprehensive.remote.service;

import java.util.List;
import java.util.Map;

public interface ComStatisticsRemoteService {
	/**
	 * 某个高中学生综合素质 （初三下到高三上的数据）
	 * 不是高中学生返回null
	 * @param studentId
	 * @param maxValueMap 总分最大值限制
	 * @return key:
	 * "XNXQ";//学年学期
	 * "XKCJ";//学科成绩
	 * "YYBS";//英语笔试
	 * "YYKS";//英语口试
	 * "TYCJ";//体育成绩
	 */
	public Map<String,String[]> findStatisticsByStudentId(String studentId,Map<String,Integer> maxValueMap);

	/**
	 * 修复学处理
	 */
	public void setRecoverStuScore(String[] studentIds,Map<String,Integer> stuYearMap,String acadyear);
	/**
	 * 所有高中学生综合素质 （初三下到高三上的数据）
	 * @param unitId
	 * @return key:
	 * "XNXQ";//学年学期
	 * "XKCJ";//学科成绩
	 * "YYBS";//英语笔试
	 * "YYKS";//英语口试
	 * "TYCJ";//体育成绩
	 */
	public Map<String,String> findStatisticsByUnitId(String unitId,Map<String,Integer> maxValueMap, Map<String, Boolean> showMap);
	
	/**
	 * 某个学生学考成绩
	 * @param studentId
	 * @return Map<String,String> key:subject_id value 成绩  当subject_Id 32个0代表总分
	 */
	public Map<String,String> findXKStatisticsByStudentId(String studentId);
	/**
	 * 学考成绩
	 * @param unitId
	 * @return Map<String,String> key:student_id 学考折算总分
	 */
	public Map<String,String> findXKStatisticsByUnitId(String unitId);

	public Map<String, Map<String, String[]>> findStatisticsByStudentIds(String classId, String[] studentIds, Map<String, Integer> maxValueMap);

	public Map<String, Map<String, String>> findXKStatisticsByStudentIds(String unitId, String[] studentIds);

	/**
	 * 学习汇总
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param studentIds
	 * @return
	 */
	public Map<String, Map<String, String[]>> findXxhzByStudentIds(String unitId, String acadyear, String semester, String gradeId, String[] studentIds);

	/**
	 * 体育汇总
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param studentIds
	 * @return
	 */
	public Map<String, String[]> findTyhzByStudentIds(String unitId, String acadyear, String semester, String gradeId, String[] studentIds);

	/**
	 * 所有高中学生英语笔试（初三下到高三上的数据）
	 * @param gradeStudentMap
	 * @param maxValue
	 * @return
	 */
	Map<String, String> findYybsByGradeMap(Map<String, List<String>> gradeStudentMap, int maxValue);
}
