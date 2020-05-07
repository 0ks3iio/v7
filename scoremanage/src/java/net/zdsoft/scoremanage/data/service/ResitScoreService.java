package net.zdsoft.scoremanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ResitScore;

import java.util.List;

public interface ResitScoreService extends BaseService<ResitScore, String>{

	void deleteResitScoreBy(String unitId, String examId, String gradeId,String subjectId);
	List<ResitScore> listResitScoreBy(String unitId, String examId, String subjectId);
	List<ResitScore> listResitScoreBy(String unitId, String examId);
	String saveAllResitScore(String unitId,String acadyear, String semester, String examId, String gradeId, List<String[]> datas);
	
	void saveResitScoreBy(String unitId, String examId, String gradeId,String subjectId,List<ResitScore> resitScoreList);

	/**
	 * 根据考试查询补考成绩
	 * @param unitId
	 * @param examIds
	 * @return
	 */
    List<ResitScore> findListByExamIds(String unitId, String[] examIds);
}
