package net.zdsoft.scoremanage.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ResitScore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResitScoreDao extends BaseJpaRepositoryDao<ResitScore, String>{
    @Modifying
    @Query("delete from ResitScore where unitId=?1 and examId=?2 and gradeId=?3 and subjectId=?4")
	public void deleteResitScoreBy(String unitId, String examId,
			String gradeId, String subjectId);
    
    @Modifying
    @Query("delete from ResitScore where unitId=?1 and examId=?2 and gradeId=?3")
	public void deleteResitScoreByGradeId(String unitId, String examId,String gradeId);
    
    @Query("from ResitScore where unitId=?1 and examId=?2 and subjectId=?3")
    public List<ResitScore> listResitScoreBy(String unitId, String examId,
			String subjectId);
    
    @Query("from ResitScore where unitId=?1 and examId=?2")
    public List<ResitScore> listResitScoreBy(String unitId, String examId);
    
    @Query("from ResitScore where unitId=?1 and examId=?2 and gradeId=?3")
    public List<ResitScore> listResitScoreByGradeId(String unitId, String examId,String gradeId);

    List<ResitScore> findByUnitIdAndExamIdIn(String unitId, String[] examIds);
}
