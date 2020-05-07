package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuStudyingFarming;

public interface DyStuStudyingFarmingDao extends BaseJpaRepositoryDao<DyStuStudyingFarming, String>{
	
	public List<DyStuStudyingFarming> findByUnitIdAndGradeIdIn(String unitId, String[] gradeIds);

	public List<DyStuStudyingFarming> findByUnitIdAndStudentIdIn(String unitId, String[] studentIds);

	public List<DyStuStudyingFarming> findByStudentIdIn(String[] studentIds);

	public void deleteByUnitIdAndStudentIdIn(String unitId, String[] studentIds);
	
	@Query("from DyStuStudyingFarming where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId in (?4)")
	public List<DyStuStudyingFarming> findByUnitIdAndStuIdIn(String unitId,String acadyear,String semester, String[] studentIds);
	@Query("from DyStuStudyingFarming where unitId = ?1 and acadyear = ?2 and semester = ?3 ")
	public List<DyStuStudyingFarming> findByUnitIdAnd(String unitId,String acadyear,String semester);
}
