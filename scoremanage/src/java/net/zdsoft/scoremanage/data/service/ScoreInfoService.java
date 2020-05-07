package net.zdsoft.scoremanage.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;
import org.springframework.ui.ModelMap;

public interface ScoreInfoService extends BaseService<ScoreInfo, String>{

	/**
	 * 学生某次考试所有成绩
	 * @param examId
	 * @param unitId
	 * @param classIdSearch
	 * @param classType
	 * @param courseIds 科目id 如果不为空 那么查询条件加上这一条
	 * @param stuIds与courseIds 得到学生id与科目id
	 * @return <studentId_courseId,scoreInfo>
	 */
	public Map<String, ScoreInfo> findByExamIdAndUnitIdAndClassId(String examId,
			String unitId, String classIdSearch,String classType, Set<String> stuIds, Set<String> courseIds);

	public List<ScoreInfo> findByExamId(String examId);

	public List<ScoreInfo> findByExamIdCourseId(String schoolId, String examId,
			String courseId, String classIdSearch, String classType);

	/**
	 * 查询某次考试某个学生某个科目的成绩
	 * @param examId
	 * @param courseId
	 * @param stuId
	 * @return
	 */
	public ScoreInfo findByStudent(String examId, String courseId, String stuId);

	/**
	 * 统计用
	 * @param examId
	 * @param isStuScoreType TODO 缺考
	 * @param studentIds TODO
	 * @return
	 */
	public List<ScoreInfo> findScoreInfoListRanking(String examId, String isStuScoreType, String... studentIds);

	/**
	 * 统计用
	 * @param examId
	 * @param scoreType TODO
	 * @param isStuScoreType TODO 缺考
	 * @param studentIds TODO
	 * @return
	 */
	public List<ScoreInfo> findListByExamId(String examId, String scoreType, String isStuScoreType, String... studentIds);

	public List<ScoreInfo> saveAllEntitys(ScoreInfo... info);
	
	/**
	 * 学生某次考试所有成绩
	 * @param examId
	 * @param unitId
	 * @param stuIds
	 * @param courseIds 得到已有成绩科目id
	 * @return <studentId_courseId,scoreInfo>
	 */
	public Map<String, ScoreInfo> findByExamIdAndUnitIdAndStudentIds(
			String examId, String unitId, String[] stuIds,Set<String> courseIds);

	/**
	 * 根据条件取成绩
	 * @param examId
	 * @param subjectIds
	 * @return List<ScoreInfo>
	 */
	public List<ScoreInfo> findScoreInfoList(String examId, String[] subjectIds);

	/**
	 * 通过考试ids查成绩
	 * @param unitId
	 * @param examIds
	 * @param studentIds
	 * @return
	 */
	public List<ScoreInfo> findScoreByExamIds(String unitId,String[] examIds,String inputType);
	
	/**
	 * 根据学年、学期、单位、科目、教学班级来查找选修课的成绩信息
	 * @param unitId
	 * @param zeroGuid
	 * @param subjectId
	 * @param teachClassId
	 * @return
	 */
	public List<ScoreInfo> findOptionalCourseScore(String unitId,String subjectId,
			String teachClassId);
	/**
	 * 老师在某次考试 某门课 中，是否有录分权限
	 * @param examId 必修课填写，选修课为空(null)
	 * @param classIdSearch 行政班或者教学班班级ID
	 * @param courseId 课程id
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public int getEditRole(String examId, String classIdSearch,
			String courseId, String unitId, String acadyear, String semester, String teacherId);

	/**
	 * (杭外定制)
	 * @param unitId
	 * @param teachClassIds
	 * @return
	 */
	public List<ScoreInfo> findOptionalCourseScore(String unitId, String[] teachClassIds);

	/**
	 * 根据学年学期查询学生成绩
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param studentId
	 * @param examId
	 * @return
	 */
	public List<ScoreInfo> findByCondition(String unitId, String acadyear, String semester, String studentId, String examId);

	public List<ScoreInfo> findBystudentIds(String unitId, String acadyear, String semester, String[] studentIds, String examId);
	public String findJsonScoreInfo(String unitId, String examId, String gradeCode, ModelMap map);

	public List<ScoreInfo> findByExamIdAndSubIdsAndStudentIds(String unitId, String[] examIds, String[] subjectIds, String[] studentIds);

	/**
	 * 获取班级各科目的成绩数
	 * @param examId
	 * @param clsType 班级类型
	 * @param clsIds
	 * @return
	 */
	public Map<String, Integer> findNumByExamIdClsIds(String examId, String clsType, String... clsIds);
	
	public List<ScoreInfo> findByClsIds(String examId, String inputType, String... clsIds);
	
	public List<ScoreInfo> findListByClsIds(String unitId, String acadyear, String semester, String examId, String subjectId, String inputType, String... clsIds);
	public List<ScoreInfo> findListByClsIds(String unitId, String acadyear, String semester, String examId, String inputType, String... clsIds);
}
