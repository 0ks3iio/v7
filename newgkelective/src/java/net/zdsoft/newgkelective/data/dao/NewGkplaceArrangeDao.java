package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkplaceArrange;

public interface NewGkplaceArrangeDao extends BaseJpaRepositoryDao<NewGkplaceArrange, String>{

	public void deleteByArrayItemId(String arrayItemId);
	@Query("From NewGkplaceArrange where arrayItemId = ?1 order by orderId")
	public List<NewGkplaceArrange> findByArrayItemId(String arrayItemId);
	@Modifying
	@Query("delete from NewGkplaceArrange where arrayItemId = ?1 and placeId = ?2")
	public void deleteByItemIdAndPlaceId(String arrayItemId, String placeId);
	@Query("From NewGkplaceArrange where arrayItemId in (?1)")
	public List<NewGkplaceArrange> findByArrayItemIds(String[] arrayItemIds);
}
