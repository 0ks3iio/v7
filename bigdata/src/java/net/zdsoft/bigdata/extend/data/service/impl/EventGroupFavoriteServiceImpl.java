package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.EventGroupFavoriteDao;
import net.zdsoft.bigdata.extend.data.entity.EventGroupFavorite;
import net.zdsoft.bigdata.extend.data.service.EventGroupFavoriteService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件预览组和收藏关系接口实现
 * 
 * @author jiangf
 *
 */
@Service("eventGroupFavoriteService")
public class EventGroupFavoriteServiceImpl extends
		BaseServiceImpl<EventGroupFavorite, String> implements
		EventGroupFavoriteService {

	@Autowired
	private EventGroupFavoriteDao eventGroupFavoriteDao;

	@Override
	public List<EventGroupFavorite> findFavoriteListByGroupId(String groupId) {
		return eventGroupFavoriteDao.findFavoriteListByGroupId(groupId);
	}

	@Override
	public List<EventGroupFavorite> findGroupListByFavoriteId(String favoriteId) {
		return eventGroupFavoriteDao.findGroupListByFavoriteId(favoriteId);
	}

	@Override
	public void saveFavorite2Group(List<EventGroupFavorite> favoriteList) {
		for (EventGroupFavorite favorite : favoriteList) {
			Integer maxOrderId = getMaxOrderIdByGroupId(favorite.getGroupId());
			if (maxOrderId == null)
				maxOrderId = 0;
			favorite.setOrderId(maxOrderId + 1);
		}
		saveAll(favoriteList.toArray(new EventGroupFavorite[0]));
	}

	@Override
	public void deleteByFavoriteId(String favoriteId) {
		eventGroupFavoriteDao.deleteByFavoriteId(favoriteId);
	}

	@Override
	public void deleteByGroupId(String groupId) {
		eventGroupFavoriteDao.deleteByGroupId(groupId);
	}

	@Override
	public void deleteByGroupAndFarvoriteId(String groupId, String favoriteId) {
		eventGroupFavoriteDao.deleteByGroupAndFarvoriteId(groupId, favoriteId);
	}

	@Override
	public Integer getMaxOrderIdByGroupId(String groupId) {
		return eventGroupFavoriteDao.getMaxOrderIdByGroupId(groupId);
	}

	@Override
	protected BaseJpaRepositoryDao<EventGroupFavorite, String> getJpaDao() {
		return eventGroupFavoriteDao;
	}

	@Override
	protected Class<EventGroupFavorite> getEntityClass() {
		return EventGroupFavorite.class;
	}
}
