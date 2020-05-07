package net.zdsoft.bigdata.dataimport.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTable;

/**
 * @author:zhujy
 * @since:2019/6/17 19:06
 */
public interface BgDaCustomTableService extends BaseService<BgDaCustomTable, String> {

    void deleteTableList(String id);

    String findIdByTableName(String tableName);

    void updateDataNumByTableName(String dataNum, String tableName);

    void updateDataNumToZeroByTableName(String dataNum, String tableName);
}
