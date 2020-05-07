package net.zdsoft.scoremanage.data.dao;

import java.util.List;
import java.util.Map;

import net.zdsoft.scoremanage.data.entity.ScoreInfo;

public interface ScoreInfoJdbcDao {
	
	public List<ScoreInfo> findScoreInfoListRanking(String examId, String scoreStatus, String... studentIds);
	
	public List<ScoreInfo> findListByExamId(String examId, String inputType, String scoreStatus, String... studentIds);
	
	/**
	 * 班级各科目的学生成绩数
	 * @param examId
	 * @param clsIds
	 * @return
	 */
	public Map<String, Integer> findNumByExamIdClsIds(String examId, String... clsIds);
	
	/**
	 * 教学班级各科目的学生成绩数
	 * @param examId
	 * @param clsIds
	 * @return
	 */
	public Map<String, Integer> findNumByExamIdTeaClsIds(String examId, String... teaClsIds);
}
