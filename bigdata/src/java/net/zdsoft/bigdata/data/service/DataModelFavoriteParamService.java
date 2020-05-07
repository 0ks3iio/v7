
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.DataModelFavoriteParam;

public interface DataModelFavoriteParamService extends BaseService<DataModelFavoriteParam, String> {

    void deleteByFavoriteId(String favoriteId);
}
