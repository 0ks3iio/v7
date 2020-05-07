package net.zdsoft.evaluation.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.evaluation.data.dto.OptionDto;
import net.zdsoft.evaluation.data.dto.ResultStatDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;

public interface TeachEvaluateResultService extends BaseService<TeachEvaluateResult, String> {

	public List<TeachEvaluateResult> findBySubmitAndInProjectId(String[] projectIds);

	/**
	 * 评教调查列表页面   
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<TeachEvaluateProject> findResultByUnitId(String unitId, String studentId,String acadyear, String semester);
//	/**
//	 * 评教调查列表页面   
//	 * @param unitId
//	 * @param acadyear
//	 * @param semester
//	 * @return
//	 */
//	public List<TeachEvaluateResult> findResultByItemId(String unitId, String itemId);
	/**
	 * 调查问卷
	 * @param unitId
	 * @param studentId
	 * @param projectId evaluateType
	 * @param teacher TODO
	 * @param clsId TODO
	 * @return
	 */
	public Map<String,List<TeachEvaluateItem>> findItemByUnitId(Map<String,String> resultOfTeaIdMap,String unitId, String studentId,String projectId,
			String evaluateType,String subjectId,String status, String teacher, String clsId);
	/**
	 * 获取subjectId 以及对应的teacherId
	 * @param unitId
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	//public Map<String,String> getSubjectIdOfTeaId(String unitId,String studentId,String acadyear, String semester);
	/**
	 * 获取班主任
	 * @param studentId
	 * @return
	 */
	public Teacher getClassTeacher(String studentId);
	/**
	 * 获取学生的选修课
	 * @param unitId
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	//public Map<String,String[]> getElectiveMap(String unitId,String studentId, String acadyear, String semester);
	/**
	 * 获取导师
	 * @param studentId
	 * @return
	 */
	public Teacher getTutorTeacher(String studentId,String unitId);
	/**
	 * 保存结果
	 * @param studentId unitId dto
	 * @return
	 */
	public String saveResult(String unitId,String studentId,OptionDto dto);

	public void deleteByProjectId(String projectId);
	/**
	 * 获得提交调查的学生
	 */
	public Set<String> getResultStudents(String projectId,String classId,String subId,String teacherId);
	/**
	 * @param projectId
	 * @return clsId+","+subId+","+teaId
	 */
	public Set<String> getResultClsSubTeaIds(String projectId);
	
	public List<TeachEvaluateResult> findByProjectIdAndClsIdAndSubId(String projectId,
			String clsId, String subId);

	public List<TeachEvaluateResult> findByProjectIdAndSubIdAndTeacherId(
			String projectId, String subId, String teaId);
	/**获得由提交数据的班级
	 * @param projectId
	 * @return gradeId+","+classId
	 */
	public Set<String> getResultGradeClsId(String projectId);

	public List<TeachEvaluateResult> findByProjectIdAndClsId(
			String projectId, String clsId);

	public Set<String> getResultGradeTeaId(String projectId);
	
	public List<TeachEvaluateResult> findByProjectIdAndTeaId(
			String projectId, String teaId);

	public List<TeachEvaluateResult> findByProjectIdAndGradeId(String projectId,
			String gradeId);

	public Set<String> getResultSubIds(String projectId);

//	public List<TeachEvaluateResult> findByProjectIdAndSubId(String projectId,
//			String subId);

	public List<ResultStatDto> getResultTxtDto(String projectId,
			String subjectId, String classId,
			String teaId);

	public Set<String> getStuIdByProjectId(String projectId,
			String classId, String subId, String teacherId);

	public Map<String, Integer> getCountMapByProjectIds(String[] projectIds);
	/**
	 * 获取必修课或选修课的科目id对应班级id  以及科目id对应老师id
	 * @param subjectIdOFTeaIdMap
	 * @param subjectIdOFClassIdMap
	 * @param unitId
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @param projectId
	 */
	public void setCourseMap(Set<String> subIdTeaIdClassIdSet
			,String unitId,String studentId,String acadyear, String semester,String projectId);

	public Map<String, Float> findTeaRankBy(String projectId, String evaluateType);
	/**
	 * @param projectId
	 * @param evaluateType
	 * @return key:teacherId or teaId_classId
	 */
	public Map<String, List<Map<String,String>>> getResultTxtDto(String projectId, String evaluateType);

}
