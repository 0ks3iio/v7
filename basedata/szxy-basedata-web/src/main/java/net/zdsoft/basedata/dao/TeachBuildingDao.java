package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachBuildingDao extends BaseJpaRepositoryDao<TeachBuilding, String>{

	@Query("From TeachBuilding where unitId=?1 and isDeleted=?2")
	public List<TeachBuilding> findByUnitIdAndIsDeleted(String unitId,int isDeleted);

	@Query("From TeachBuilding where unitId in ?1 and isDeleted = 0 ")
	public List<TeachBuilding> findByUnitIdIn(String[] uidList);
	
	@Query("select id,buildingName From TeachBuilding Where id in (?1)")
    List<Object[]> findPartTeaBuiByIds(String[] ids); 
}
