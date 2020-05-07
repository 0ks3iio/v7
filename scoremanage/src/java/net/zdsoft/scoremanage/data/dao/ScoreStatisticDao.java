package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ScoreStatistic;

public interface ScoreStatisticDao extends BaseJpaRepositoryDao<ScoreStatistic, String>{

	@Modifying
	@Query("delete From ScoreStatistic where examId = ?1")
	void deleteByExamId(String examId);
	@Query("From ScoreStatistic where examId = ?1 and classId = ?2 and subjectId = ?3 and type = ?4 order by all_score desc")
	List<ScoreStatistic> findList(String examId, String classId, String subjectId, String type);
//	@Query("From ScoreStatistic where examId = ?1 and subjectId = ?2 and courseRanking = ?3 and type = ?4")
//	ScoreStatistic findOneCourseRanking(String examId, String subjectId, Integer ranking, String type);
//	@Query("From ScoreStatistic where examId = ?1 and subjectId = ?2 and allRanking = ?3 and type = ?4")
//	ScoreStatistic findOneAllRanking(String examId, String subjectId, Integer ranking, String type);

}
