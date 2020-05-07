package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopMannerRecord;

public interface StuDevelopMannerRecordDao extends BaseJpaRepositoryDao<StuDevelopMannerRecord, String>{
	@Query("From StuDevelopMannerRecord where acadyear = ?1 and semester = ?2 and subjectId = ?3 and studentId in (?4)")
	public List<StuDevelopMannerRecord> findListByCls(String acadyear, String semester,String subjId, String[] stuIds);
	
	@Query("From StuDevelopMannerRecord where acadyear = ?1 and semester = ?2 and studentId = ?3")
	public List<StuDevelopMannerRecord> findListByStu(String acadyear, String semester,String stuId);
	
	@Query("From StuDevelopMannerRecord where acadyear = ?1 and semester = ?2 and studentId = ?3 and subjectId = ?4")
	public List<StuDevelopMannerRecord> findListByStuAndSub(String acadyear, String semester,String stuId, String subjectId);
}
