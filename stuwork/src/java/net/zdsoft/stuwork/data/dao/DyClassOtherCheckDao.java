package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyClassOtherCheck;

import org.springframework.data.jpa.repository.Query;

public interface DyClassOtherCheckDao extends BaseJpaRepositoryDao<DyClassOtherCheck,String>{

	@Query("From DyClassOtherCheck where unitId = ?1 and acadyear = ?2 and semester = ?3")
	public List<DyClassOtherCheck> findByUnitId(String unitId, String acadyear,Integer semester);

	@Query("From DyClassOtherCheck where classId = ?1 and acadyear = ?2 and semester = ?3")
	public List<DyClassOtherCheck> findByClassId(String classId, String acadyear,Integer semester);
	
	@Query("From DyClassOtherCheck where classId = ?1  and week =?2 and acadyear = ?3 and semester = ?4")
	public List<DyClassOtherCheck> findByClassIdAndWeek(String classId,int week,String acadyear, Integer semester);
	
	@Query("From DyClassOtherCheck where week =?1 and acadyear = ?2 and semester = ?3")
	public List<DyClassOtherCheck> findByClassIdAndWeek(int week,String acadyear, Integer semester);

}
