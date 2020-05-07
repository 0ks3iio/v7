package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;

import org.springframework.data.jpa.repository.Query;

public interface DyDormBuildingDao extends BaseJpaRepositoryDao<DyDormBuilding,String>{
	@Query("From DyDormBuilding where unit_id = ?1 order by name")
	public List<DyDormBuilding> findByUnitId(String unitId);
	
	@Query("From DyDormBuilding where unit_id = ?1 and name = ?2")
	public List<DyDormBuilding> findByName(String unitId,String name);
}
