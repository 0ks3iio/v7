package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.FieldsDisplay;

public interface FieldsDisplayService extends BaseService<FieldsDisplay, String> {
    /**
     * 得到要设置的数据项列表，根据某校unitid、type、colsUse是否显示属性
     * 
     * @param unitId
     * @param type
     * @param colsUse
     * @return
     */
    public List<FieldsDisplay> findByColsDisplays(String unitId, String type, Integer colsUse);

    /**
     * 根据schGUID、类型,得到显示信息设置记录
     * 
     * @param unitId
     * @param type
     * @return
     */
    public List<FieldsDisplay> findByColsDisplays(String unitId, String type);

    /**
     * 取得下级显示
     * 
     * @param parentId
     * @param colsUse
     * @return
     */
    public List<FieldsDisplay> findByColsDisplays(String parentId, Integer colsUse);

}
