package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopScoreRecord;

public interface StuDevelopScoreRecordDao extends BaseJpaRepositoryDao<StuDevelopScoreRecord,String>{
    @Query("from StuDevelopScoreRecord where acadyear = ?1 and semester = ?2 and studentId = ?3")
	public List<StuDevelopScoreRecord> stuDevelopScoreRecordList(
			String acadyear, String semester, String studentId);
    
    @Query("from StuDevelopScoreRecord where acadyear = ?1 and semester = ?2 and projectId in (?3)")
    public List<StuDevelopScoreRecord> findByProjectIds(String acadyear,
			String semester, String[] projectIds);
    
    @Query("from StuDevelopScoreRecord where acadyear = ?1 and semester = ?2 and projectId = ?3")
    public List<StuDevelopScoreRecord> findByProjectId(String acadyear,
			String semester, String projectId);
    @Modifying
    @Query("delete from StuDevelopScoreRecord where acadyear = ?1 and semester = ?2 and studentId in (?3)")
    public void deleteByStudentIds(String acadyear, String semester,
			String[] studentIds);
}
