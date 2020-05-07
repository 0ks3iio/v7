package net.zdsoft.bigdata.dataimport.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTableColumn;

import java.util.List;

/**
 * @author:zhujy
 * @since:2019/6/17 19:30
 */
public interface BgDaCustomTableColumnService extends BaseService<BgDaCustomTableColumn, String> {


    List<String> findColumnNameByTableId(String id);
}
