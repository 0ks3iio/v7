package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.ModelDatasetUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ModelDatasetUserDao extends BaseJpaRepositoryDao<ModelDatasetUser, String> {

    void deleteByDsIdAndUnitId(String dsId, String unitId);

    void deleteByDsId(String dsId);
}
