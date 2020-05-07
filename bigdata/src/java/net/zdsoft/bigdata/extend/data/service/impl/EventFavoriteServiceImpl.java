package net.zdsoft.bigdata.extend.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.dao.EventFavoriteDao;
import net.zdsoft.bigdata.extend.data.entity.EventFavorite;
import net.zdsoft.bigdata.extend.data.entity.EventFavoriteParam;
import net.zdsoft.bigdata.extend.data.entity.EventGroupFavorite;
import net.zdsoft.bigdata.extend.data.service.EventFavoriteParamService;
import net.zdsoft.bigdata.extend.data.service.EventFavoriteService;
import net.zdsoft.bigdata.extend.data.service.EventGroupFavoriteService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class EventFavoriteServiceImpl extends
		BaseServiceImpl<EventFavorite, String> implements EventFavoriteService {

	@Resource
	private EventFavoriteDao eventFavoriteDao;
	@Resource
	private EventGroupFavoriteService eventGroupFavoriteService;
	@Resource
	private EventFavoriteParamService eventFavoriteParamService;

	@Override
	protected BaseJpaRepositoryDao<EventFavorite, String> getJpaDao() {
		return eventFavoriteDao;
	}

	@Override
	protected Class<EventFavorite> getEntityClass() {
		return EventFavorite.class;
	}

	@Override
	public void saveEventFavorite(EventFavorite eventFavorite,
			List<EventFavoriteParam> eventFavoriteParams,
			List<String> eventGroupIds) throws BigDataBusinessException {

        if (StringUtils.isNotBlank(eventFavorite.getId())) {
            EventFavorite favorite = this.findOne(eventFavorite.getId());
            if (favorite == null) {
                eventFavorite.setId(null);
            }
        }
		eventFavorite.setModifyTime(new Date());
		if (StringUtils.isNotBlank(eventFavorite.getId())) {
			// 验证名称和code是否存在
			List<EventFavorite> listForName = this.findListBy(new String[] {
					"favoriteName", "userId" },
					new String[] { eventFavorite.getFavoriteName(),
							eventFavorite.getUserId() });
			if (listForName.size() > 0) {
				if (!listForName.get(0).getId().equals(eventFavorite.getId())) {
					throw new BigDataBusinessException("该名称已存在!");
				}
			}
			eventFavoriteDao.update(eventFavorite,
					new String[] { "eventId", "chartType", "favoriteName",
							"windowSize", "beginDate", "endDate", "autoFresh",
							"granularity", "orderId", "remark", "modifyTime"});
		} else {
			// 验证名称和code是否存在
			List<EventFavorite> listForName = this.findListBy(new String[] {
					"favoriteName", "userId" },
					new String[] { eventFavorite.getFavoriteName(),
							eventFavorite.getUserId() });
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			eventFavorite.setId(UuidUtils.generateUuid());
			eventFavorite.setCreationTime(new Date());
			eventFavoriteDao.save(eventFavorite);
		}

		// 保存参数
		eventFavoriteParamService.deleteByFavoriteId(eventFavorite.getId());
		eventFavoriteParams.forEach(e -> {
			e.setFavoriteId(eventFavorite.getId());
			e.setId(UuidUtils.generateUuid());
		});
		eventFavoriteParamService.saveAll(eventFavoriteParams
				.toArray(new EventFavoriteParam[0]));

		// 保存收藏级到组
		eventGroupFavoriteService.deleteByFavoriteId(eventFavorite.getId());

		if (eventGroupIds != null) {
			List<EventGroupFavorite> favoriteList = Lists.newArrayList();
			eventGroupIds.forEach(e -> {
				EventGroupFavorite groupFavorite = new EventGroupFavorite();
				groupFavorite.setFavoriteId(eventFavorite.getId());
				groupFavorite.setGroupId(e);
				groupFavorite.setId(UuidUtils.generateUuid());
				favoriteList.add(groupFavorite);
			});
			eventGroupFavoriteService.saveFavorite2Group(favoriteList);
		}
	}

	@Override
	public void updateFavoriteWindowSize(String id, int windowSize)
			throws BigDataBusinessException {
		EventFavorite favorite = findOne(id);
		favorite.setWindowSize(windowSize);
		update(favorite, id, new String[] { "windowSize" });
	}

	@Override
	public List<EventFavorite> findByUserIdAndFavoriteIds(String userId,
			List<String> favoriteIds) {
		return eventFavoriteDao.findAllByUserIdAndIdIn(userId, favoriteIds);
	}

	@Override
	public List<EventFavorite> findByUserId(String userId) {
		return eventFavoriteDao.findAllByUserIdOrderByModifyTimeDesc(userId);
	}

	@Override
	public List<EventFavorite> findByUserId(String userId, String favoriteName) {
		if (StringUtils.isBlank(favoriteName)) {
			return eventFavoriteDao.findAllByUserIdOrderByModifyTimeDesc(userId);
		}
		return eventFavoriteDao.findAllByUserIdAndFavoriteNameLikeOrderByModifyTimeDesc(userId, "%" + favoriteName + "%");
	}

	@Override
	public void deleteEventFavorite(String id) {
		this.delete(id);
		eventGroupFavoriteService.deleteByFavoriteId(id);
	}
}
