package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.DataModelFavoriteParam;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface DataModelFavoriteParamDao extends BaseJpaRepositoryDao<DataModelFavoriteParam, String> {

    void deleteByFavoriteId(String favoriteId);
}
