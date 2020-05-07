
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.ModelDataset;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

import java.util.List;

public interface ModelDatasetService extends BaseService<ModelDataset, String> {


    List<ModelDataset> getByModelId(String modelId, String unitId);

    void saveModelDataset(ModelDataset modelDataset) throws BigDataBusinessException;

    void authorization(String modelId, String modelDatasetId, String[] orderUserIdArray, String unitId);

    void deleteDataModel(String id);

    List<ModelDataset> findAllByIdIn(String[] ids);
}
