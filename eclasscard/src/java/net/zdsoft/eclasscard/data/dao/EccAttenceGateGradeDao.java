package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccAttenceGateGrade;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EccAttenceGateGradeDao extends BaseJpaRepositoryDao<EccAttenceGateGrade, String> {

	@Query("From EccAttenceGateGrade Where unitId = ?1 and classify = ?2 and type = ?3 and grade in (?4)")
	public List<EccAttenceGateGrade> findByUnitIdAndTypeInCodes(String unitId,Integer classify,Integer type,String[] gradeCodes);

	@Modifying
    @Query("delete from EccAttenceGateGrade Where unitId = ?1 and periodId = ?2")
	public void deleteByUnitIdPeriodId(String unitId, String periodId);

	@Query("From EccAttenceGateGrade Where classify = 2 and unitId = ?1 and grade = ?2")
	public List<EccAttenceGateGrade> findByInOutAndCode(String unitId,
			String gradeCode);

	@Query("From EccAttenceGateGrade Where classify = 2")
	public List<EccAttenceGateGrade> findInOutByAll();
	
	@Query("From EccAttenceGateGrade Where classify = ?1 and periodId = ?2")
	public List<EccAttenceGateGrade> findByClassifyAndPeriodId(
			Integer classify, String periodId);

	public List<EccAttenceGateGrade> findByUnitIdAndClassifyAndGradeIn(String unitId, Integer classify, String[] grades);

	@Query("From EccAttenceGateGrade Where unitId = ?1 and classify = ?2")
	public List<EccAttenceGateGrade> findByInOutAndClassify(String unitId,
			Integer classify);
}
