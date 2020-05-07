package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopHonorRecord;

public interface StuDevelopHonorRecordDao extends BaseJpaRepositoryDao<StuDevelopHonorRecord,String>{

	@Query("From StuDevelopHonorRecord where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId in (?4) ORDER BY giveDate desc")
	public List<StuDevelopHonorRecord> findListByCls(String unitId,String acadyear, String semester,String[] array);
	
	@Query("From StuDevelopHonorRecord where unitId = ?1 and honorType = ?2 and acadyear = ?3 and semester = ?4 and studentId in (?5) ORDER BY giveDate desc")
	public List<StuDevelopHonorRecord> findByfindBytypeAndclass(String unitId,
			String honortype, String acadyear, String semester, String[] array);

	@Query("From StuDevelopHonorRecord where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId = ?4")
	public List<StuDevelopHonorRecord> getHonorList(String unitId,String acadyear,String semester, String stuId);

	@Query("From StuDevelopHonorRecord where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId = ?4 and honorType = ?5 and honorLevel = ?6")
	public List<StuDevelopHonorRecord> getStudocHonorRecordList(String unitId,
			String acadyear, String semester, String studentId,
			String honorType, String honorLevel);

	@Query("select count(distinct honorLevel) from StuDevelopHonorRecord where acadyear = ?1 and semester = ?2 and studentId = ?3 and honorType = ?4")
	public int getDistinctCount(String acadyear, String semester,String studentId, String honorType);

	@Query("From StuDevelopHonorRecord where acadyear = ?1 and semester = ?2 and unitId = ?3 ORDER BY giveDate desc")
	public List<StuDevelopHonorRecord> findAllhonor(String acadyear,String semester, String unitId);

	@Query("From StuDevelopHonorRecord where honorType = ?1 and acadyear = ?2 and semester = ?3 and unitId = ?4 ORDER BY giveDate desc")
	public List<StuDevelopHonorRecord> findByHonortype(String honortype,String acadyear, String semester, String unitId);

}
