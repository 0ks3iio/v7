package net.zdsoft.bigdata.dataimport.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.dataimport.dao.BgDaCustomTableColumnDao;
import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTableColumn;
import net.zdsoft.bigdata.dataimport.service.BgDaCustomTableColumnService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:zhujy
 * @since:2019/6/17 19:31
 */
@Service
public class BgDaCustomTableColumnServiceImpl extends BaseServiceImpl<BgDaCustomTableColumn, String> implements BgDaCustomTableColumnService{

    @Autowired
    private BgDaCustomTableColumnDao bgDaCustomTableColumnDao;

    @Override
    protected BaseJpaRepositoryDao<BgDaCustomTableColumn, String> getJpaDao() {
        return bgDaCustomTableColumnDao;
    }

    @Override
    protected Class<BgDaCustomTableColumn> getEntityClass() {
        return BgDaCustomTableColumn.class;
    }


    @Override
    public List<String> findColumnNameByTableId(String id) {
        return bgDaCustomTableColumnDao.findColumnNameByTableId(id);
    }
}
