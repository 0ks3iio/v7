package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;

public interface StuEvaluateRecordDao extends BaseJpaRepositoryDao<StuEvaluateRecord,String>{

	@Query("From StuEvaluateRecord where acadyear = ?1 and semester = ?2 and studentId in (?3)")
	public List<StuEvaluateRecord> findListByCls(String acadyear, String semester,String[] array);

	@Query("From StuEvaluateRecord where studentId = ?1 and acadyear = ?2 and semester = ?3")
	public StuEvaluateRecord findById(String stuId, String acadyear,String semester);

	@Modifying
	@Query("delete From StuEvaluateRecord where acadyear = ?1 and semester = ?2 and studentId in (?3)")
	Integer deleteByStuIds(String acadyear, String semester,String[] studentIds);
}
