package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuPunishment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DyStuPunishmentDao extends BaseJpaRepositoryDao<DyStuPunishment, String>{
	@Query("from DyStuPunishment where studentId = ?1")
	public List<DyStuPunishment> getScoreByStudentId(String studentId);
	@Query("from DyStuPunishment where unitId = ?1  and punishDate >=?2 and punishDate <=?3")
	public List<DyStuPunishment> getALL(String unitId,Date startTime,Date endTime);
	
	public List<DyStuPunishment> findByUnitIdAndPunishTypeIdIn(String unitId, String[] punishTypeIds);

	@Query("from DyStuPunishment where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId in (?4)")
	public List<DyStuPunishment> findByUnitIdAndStuIdIn(String unitId, String acadyear, String semester,String[] studentIds);
	@Query("from DyStuPunishment where unitId = ?1 and acadyear = ?2 and semester = ?3 ")
	public List<DyStuPunishment> findByUnitIdAnd(String unitId, String acadyear, String semester);
	@Modifying
	@Query("delete from DyStuPunishment where unitId = ?1 and acadyear = ?2 and semester = ?3")
	public void deleteByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);
	@Query("from DyStuPunishment where studentId in (?1)")
	public List<DyStuPunishment> getListByStudentIdIn(String[] studentId);
	
}
