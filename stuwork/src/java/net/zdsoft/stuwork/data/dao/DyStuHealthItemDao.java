package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuHealthItem;

public interface DyStuHealthItemDao extends BaseJpaRepositoryDao<DyStuHealthItem, String>{

	@Query("From DyStuHealthItem where unitId = ?1 order by orderId")
	public List<DyStuHealthItem> findByUnitId(String unitId);
	
	@Query("From DyStuHealthItem where unitId = ?1 and itemName = ?2")
	public List<DyStuHealthItem> findbyItemName(String unitId,String itemName);
}
