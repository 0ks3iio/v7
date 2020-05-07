package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;

public interface NewGkPlaceItemDao extends BaseJpaRepositoryDao<NewGkPlaceItem, String>{

	@Modifying
	@Query("delete from NewGkPlaceItem where arrayItemId=?1 ")
	public void deleteByArrayItemId(String arrayItemId);
    
   
    public List<NewGkPlaceItem> findByArrayItemId(String arrayItemId);

	public List<NewGkPlaceItem> findByArrayItemIdAndTypeIn(String arrayItemId,
			String[] types);

	public void deleteByArrayItemIdAndType(String arrayItemId, String type);

	@Query("select arrayItemId,count(*) from NewGkPlaceItem "
			+ "where arrayItemId in (?1) group by arrayItemId ")
	public List<Object[]> findGroupInfo(String[] arrayItemIds);

	
	@Modifying
	@Query("delete from NewGkPlaceItem where arrayItemId=?1 and placeId not in (?2)")
	public void deleteNotInPlaceIds(String arrayItemId, String[] placeIds);
}
