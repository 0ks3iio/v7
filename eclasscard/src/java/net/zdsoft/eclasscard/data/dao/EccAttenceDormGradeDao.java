package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccAttenceDormGrade;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccAttenceDormGradeDao extends BaseJpaRepositoryDao<EccAttenceDormGrade, String> {
	
	@Query("From EccAttenceDormGrade Where unitId = ?1 and type = ?2 and grade in (?3)")
	public List<EccAttenceDormGrade> findListByUnitIdInGrade(String unitId,Integer type,String[] gradeCodes);
	
	@Query("From EccAttenceDormGrade Where unitId = ?1 and type in (?2) ")
	public List<EccAttenceDormGrade> findListByUnitIdType(String unitId,Integer[] types);
	@Query("From EccAttenceDormGrade Where unitId = ?1 and type in (?2) and grade in (?3)")
	public List<EccAttenceDormGrade> findListByUnitIdTypeGrade(String unitId,Integer[] types,String[] gradeCodes);
	
//	@Query("From EccAttenceDormGrade Where unitId = ?1 and type = ?2 and grade = ?3 and beginTime <= ?4 and endTime >= ?4")
//	public EccAttenceDormGrade findByBettwenTime(String unitId,Integer type,String gradeCode,String time);

	@Modifying
    @Query("delete from EccAttenceDormGrade Where unitId = ?1 and periodId = ?2")
	public void deleteByUnitIdPeriodId(String unitId, String periodId);

}
