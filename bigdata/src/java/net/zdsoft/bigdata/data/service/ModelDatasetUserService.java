
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.ModelDatasetUser;

public interface ModelDatasetUserService extends BaseService<ModelDatasetUser, String> {

    /**
     * 根据数据集删除
     * @param modelDatasetId
     * @param unitId 单位id可为空
     */
    void deleteByModelDatasetId(String modelDatasetId, String unitId);
}
