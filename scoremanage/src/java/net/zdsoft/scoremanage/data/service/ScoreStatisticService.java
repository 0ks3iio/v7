package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ScoreStatistic;

public interface ScoreStatisticService extends BaseService<ScoreStatistic, String>{

	List<ScoreStatistic> findList(String examId, String classId, String courseId, String type);

	/**
	 * 
	 * @param unitId TODO
	 * @param classId TODO
	 * @param examId
	 * @param courseId
	 * @param ranking
	 * @param type
	 * @param rankType 排名类型 1科目 2总
	 * @return
	 */
	ScoreStatistic findOne(String unitId, String classId, String examId, String courseId, Integer ranking, String type, String rankType);

	void deleteByExamId(String examId);

	List<ScoreStatistic> saveAllEntitys(ScoreStatistic... scoreStatistic);
}
