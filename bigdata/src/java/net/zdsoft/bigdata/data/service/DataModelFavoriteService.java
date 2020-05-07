
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.DataModelFavorite;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface DataModelFavoriteService extends BaseService<DataModelFavorite, String> {

    List<DataModelFavorite> findAll(String unitId);

    List<DataModelFavorite> getUserReport(String unitId, String userId, String name, String []tagIds, Pagination page, boolean isEdit);

    List<DataModelFavorite> getUserReport(String userId, String name, String []tagIds);

    void saveDataModelFavorite(DataModelFavorite dataModelFavorite) throws BigDataBusinessException;

    void deleteDataModelFavorite(String favoriteId);

    void deleteDataModelFavoriteByIds(String[] ids);

    void updateOrderType(DataModelFavorite dataModelFavorite);

    void updateDataModelFavorite(DataModelFavorite dataModelFavorite);
}
