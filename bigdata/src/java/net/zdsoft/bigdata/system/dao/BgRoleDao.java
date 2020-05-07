package net.zdsoft.bigdata.system.dao;

import java.util.List;

import net.zdsoft.bigdata.system.entity.BgRole;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface BgRoleDao extends BaseJpaRepositoryDao<BgRole, String> {

	@Query("From BgRole where unitId =?1 order by orderId ")
	public List<BgRole> findRoleListListByUnitId(String unitId);
	
	@Query("From BgRole where unitId =?1 and name =?2 ")
	public BgRole findRoleByUnitIdAndName(String unitId,String name);

	 @Query("select max(orderId) from BgRole where unitId = ?1 ")
	 public Integer getMaxOrderIdByUnitId(String unitId);
}
