package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;

public interface DyDormCheckResultDao  extends BaseJpaRepositoryDao<DyDormCheckResult,String>{
	
	@Modifying
	@Query("DELETE FROM DyDormCheckResult WHERE id in (?1)")
	public void deletedByIds(String[] ids);
	@Query("FROM DyDormCheckResult WHERE  school_id= ?1 and building_id = ?2  and input_date = ?3 ")
	public List<DyDormCheckResult> findByCon(String schoolId,String buildingId,Date inputDate);
	
	@Query("FROM DyDormCheckResult WHERE  school_id= ?1   and input_date = ?2 and room_id in (?3)")
	public List<DyDormCheckResult> findByCon(String schoolId,Date inputDate,String[] roomIds);
	
	@Query("FROM DyDormCheckResult WHERE  school_id= ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and room_id in (?5)")
	public List<DyDormCheckResult> findByCon(String schoolId,String acadyear, String semeter, int week, String[] roomIds);
}
