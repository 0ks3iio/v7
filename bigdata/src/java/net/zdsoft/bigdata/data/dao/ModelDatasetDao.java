package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.ModelDataset;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface ModelDatasetDao extends BaseJpaRepositoryDao<ModelDataset, String> {

    List<ModelDataset> findAllByModelIdAndUnitIdOrderByOrderId(String modelId, String unitId);

    List<ModelDataset> findAllByIdInOrderByOrderId(String[] ids);
}
