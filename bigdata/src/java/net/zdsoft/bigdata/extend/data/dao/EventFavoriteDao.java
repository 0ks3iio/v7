package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.EventFavorite;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface EventFavoriteDao extends BaseJpaRepositoryDao<EventFavorite, String> {

    List<EventFavorite> findAllByUserIdAndIdIn(String userId, List<String> ids);

    List<EventFavorite> findAllByUserIdOrderByModifyTimeDesc(String userId);

    List<EventFavorite> findAllByUserIdAndFavoriteNameLikeOrderByModifyTimeDesc(String userId, String favoriteName);
}
