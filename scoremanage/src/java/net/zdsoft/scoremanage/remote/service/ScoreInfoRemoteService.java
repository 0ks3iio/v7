package net.zdsoft.scoremanage.remote.service;

import java.util.List;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;

public interface ScoreInfoRemoteService extends BaseRemoteService<ScoreInfo,String>{
	
	/**
	 * 根据条件取状态为正常的成绩
	 * @param examId
	 * @param subjectIds
	 * @return List<ScoreInfo>
	 */
	public String findScoreInfoList(String examId,String[] subjectIds);
	
	public String findJsonScoreInfo(String schoolId,String examId, String gradeCode);
	
	/**
	 * 根据学年学期查询学生成绩
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param studentId
	 * @param examId
	 * @return
	 */
	public String findScoreByCondition(String unitId, String acadyear, String semester, String studentId, String examId);
	
	public String findBystudentIds(String unitId, String acadyear, String semester, String[] studentIds, String examId);
}
