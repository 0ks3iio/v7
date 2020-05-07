package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.EventFavoriteParam;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface EventFavoriteParamDao extends BaseJpaRepositoryDao<EventFavoriteParam, String> {

    void deleteByFavoriteId(String favoriteId);

    List<EventFavoriteParam> findAllByFavoriteId(String favoriteId);
}
