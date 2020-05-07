
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

import java.util.Date;
import java.util.List;

public interface DataModelService extends BaseService<DataModel, String> {

    List<DataModel> findAll(String unitId);

    void saveDataModel(DataModel dataModel, String[] orderUnits, String[] orderUsers);

    void saveDataModel(DataModel dataModel) throws BigDataBusinessException;

    List<DataModel> getCurrentUserDataModel(String userId, String unitId);

    void deleteDataModel(String id, String code);

    long count(Date start, Date end);

    void deleteDataModelByIds(String[] modelIds);
}
