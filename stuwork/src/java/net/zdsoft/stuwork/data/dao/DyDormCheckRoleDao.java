package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormCheckRole;

public interface DyDormCheckRoleDao extends BaseJpaRepositoryDao<DyDormCheckRole, String>{
	@Query("FROM DyDormCheckRole where school_id=?1 and acadyear=?2 and semester=?3")
	public List<DyDormCheckRole> getCheckRolesBy(String schoolId,String acadyear,String semester);
	
	@Modifying
	@Query("DELETE FROM DyDormCheckRole WHERE school_id=?1 and building_id=?2 and acadyear=?3 and semester=?4")
	public void deleteBy(String unitId,String buildingId,String acadyear,String semester);
}
