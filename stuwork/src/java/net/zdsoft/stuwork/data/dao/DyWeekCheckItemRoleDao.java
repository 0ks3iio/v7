package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;

public interface DyWeekCheckItemRoleDao extends BaseJpaRepositoryDao<DyWeekCheckItemRole, String>{
	@Query("From DyWeekCheckItemRole where itemId in ?1 order by roleType")
	public List<DyWeekCheckItemRole> findByItems(String[] itemIds);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckItemRole WHERE itemId=?1")
	public void deleteByItemId(String itemId);
}
