package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemDay;

public interface DyWeekCheckItemDayDao extends BaseJpaRepositoryDao<DyWeekCheckItemDay, String>{
	@Query("From DyWeekCheckItemDay where itemId in ?1 order by day")
	public List<DyWeekCheckItemDay> findByItemIds(String[] itemIds);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckItemDay WHERE itemId=?1")
	public void deleteByItemId(String itemId);
}
