package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.EventGroupFavorite;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventGroupFavoriteDao extends
		BaseJpaRepositoryDao<EventGroupFavorite, String> {

	@Query("From EventGroupFavorite where groupId = ?1 order by orderId ")
	public List<EventGroupFavorite> findFavoriteListByGroupId(String groupId);

	@Query("From EventGroupFavorite where favoriteId = ?1 ")
	public List<EventGroupFavorite> findGroupListByFavoriteId(String favoriteId);

	@Query(value = "delete from bg_event_group_favorite where group_id= ?1 ", nativeQuery = true)
	@Modifying
	public void deleteByGroupId(String groupId);

	@Query(value = "delete from bg_event_group_favorite where favorite_id= ?1 ", nativeQuery = true)
	@Modifying
	public void deleteByFavoriteId(String favoriteId);

	@Query(value = "delete from bg_event_group_favorite where group_id= ?1 and favorite_id = ?2", nativeQuery = true)
	@Modifying
	public void deleteByGroupAndFarvoriteId(String groupId, String favoriteId);

	@Query("select max(orderId) from EventGroupFavorite where groupId = ?1")
	public Integer getMaxOrderIdByGroupId(String groupId);
}
