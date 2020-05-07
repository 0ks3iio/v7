
package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.DataModelParam;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;

import java.util.List;

public interface DataModelParamService extends BaseService<DataModelParam, String> {

    /**
     * 根据code和type查询数据模型参数
     * @param code
     * @param type
     * @return
     */
    List<DataModelParam> findByCodeAndType(String code, String type);

    /**
     * 根据code和type查询数据模型参数 只查询最高级模型
     * @param code
     * @param type
     * @return
     */
    List<DataModelParam> findTopByCodeAndType(String code, String type);

    void deleteByCode(String code);

    void saveDataModelParam(DataModelParam dataModelParam) throws BigDataBusinessException;

    /**
     * 更新参数是否显示
     * @param id
     * @param isFilter
     */
    void updateDataModelParam(String id, Integer isFilter);

    /**
     * 根据id与useTable查询非直属下级维度
     * @param useTable
     * @param id
     * @return
     */
    List<DataModelParam>findNonDirectDimensionByIdAndUseTable(String useTable,String id);


    /**
     * 查询子维度param
     * @param id
     * @return
     */
    List<DataModelParam>findChildDimensionById(String id);
}
