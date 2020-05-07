package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuMilitaryTraining;

public interface DyStuMilitaryTrainingDao extends BaseJpaRepositoryDao<DyStuMilitaryTraining, String>{
	@Modifying
	@Query("delete from DyStuMilitaryTraining where studentId in (?1)")
	public void deleteByStudentIds(String[] studentIds);
	@Query("from DyStuMilitaryTraining where studentId in (?1)")
	public List<DyStuMilitaryTraining> findByStudentIds(String[] studentIds);
	
	public List<DyStuMilitaryTraining> findByUnitIdAndGradeIdIn(String unitId, String[] gradeIds);
	
	public List<DyStuMilitaryTraining> findByUnitIdAndAcadyearAndSemester(String unitId,String acadyear,String semester);
	
	@Query("from DyStuMilitaryTraining where unitId = ?1 and acadyear = ?2 and semester =?3  and studentId in (?4)")
	public List<DyStuMilitaryTraining> findByAllStudentIds(String unitId,String acadyear,String semester,String[] studentIds);
	
	public void deleteByUnitIdAndStudentIdIn(String unitId, String[] studentIds);

}
