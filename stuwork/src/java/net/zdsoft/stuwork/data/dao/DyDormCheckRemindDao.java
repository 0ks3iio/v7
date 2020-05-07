package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormCheckRemind;
import net.zdsoft.stuwork.data.entity.DyDormCheckResult;

public interface DyDormCheckRemindDao extends BaseJpaRepositoryDao<DyDormCheckRemind, String>{
	
	@Modifying
	@Query("DELETE FROM DyDormCheckRemind WHERE id in (?1)")
	public void deletedByIds(String[] ids);
	
	@Query("FROM DyDormCheckRemind WHERE  school_id= ?1 and building_id = ?2  and check_date = ?3 ")
	public List<DyDormCheckRemind> findByCon(String schoolId,String buildingId,Date checkDate);

	@Query("FROM DyDormCheckRemind WHERE  school_id= ?1  and check_date = ?2 and room_id in (?3)")
	public List<DyDormCheckRemind> getRemindByCon(String schoolId,Date checkDate,String[] roomIds);
	
	@Query("FROM DyDormCheckRemind WHERE  school_id= ?1   and acadyear = ?2 and semester = ?3 and week=?4 and room_id in (?5)")
	public List<DyDormCheckRemind> findByCon(String schoolId,String acadyear, String semeter, int week, String[] roomIds);
}
