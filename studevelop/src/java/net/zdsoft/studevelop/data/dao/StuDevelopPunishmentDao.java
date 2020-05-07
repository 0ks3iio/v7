package net.zdsoft.studevelop.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface StuDevelopPunishmentDao extends BaseJpaRepositoryDao<StuDevelopPunishment, String>{
    @Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and schid = ?3")
	public List<StuDevelopPunishment> findListBySchId(String acadyear, String semester, String unitId, Pageable page);
    
    @Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and schid = ?3")
	public List<StuDevelopPunishment> findListBySchId(String acadyear, String semester, String unitId);
    
    @Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid in (?3)")
	public List<StuDevelopPunishment> findByStuIds(String acadyear, String semester, String[] stuids, Pageable page);
    
    @Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid in (?3)")
	public List<StuDevelopPunishment> findByStuIds(String acadyear, String semester, String[] stuids);
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid = ?3")
	public List<StuDevelopPunishment> findByStuId(String acadyear, String semester, String stuid, Pageable page); 
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid = ?3")
	public List<StuDevelopPunishment> findByStuId(String acadyear, String semester, String stuid); 
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and schid = ?3 and punishType = ?4")
	public List<StuDevelopPunishment> findByUnitIdAndLevel(String acadyear, String semester, String unitId, String punishtype, Pageable page);
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and schid = ?3 and punishType = ?4")
	public List<StuDevelopPunishment> findByUnitIdAndLevel(String acadyear, String semester, String unitId, String punishtype);
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and punishType = ?3 and stuid in (?4)")
	public List<StuDevelopPunishment> findByStuidsAndLevel(String acadyear, String semester, String punishtype, String[] stuids, Pageable page);
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and punishType = ?3 and stuid in (?4)")
	public List<StuDevelopPunishment> findByStuidsAndLevel(String acadyear, String semester, String punishtype, String[] stuids);
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid = ?3 and punishType = ?4")
	public List<StuDevelopPunishment> findByStuidAndLevel(String acadyear, String semester, String stuid, String punishtype, Pageable page);
	
	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid = ?3 and punishType = ?4")
	public List<StuDevelopPunishment> findByStuidAndLevel(String acadyear, String semester, String stuid, String punishtype);

	@Query("From StuDevelopPunishment where acadyear = ?1 and semester = ?2 and stuid = ?3")
	public List<StuDevelopPunishment> findByAcaAndSemAndStuId(String acadyear, String semester, String studentId);
}
