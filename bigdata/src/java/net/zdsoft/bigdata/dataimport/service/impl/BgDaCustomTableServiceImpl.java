package net.zdsoft.bigdata.dataimport.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.dataimport.dao.BgDaCustomTableColumnDao;
import net.zdsoft.bigdata.dataimport.dao.BgDaCustomTableDao;
import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTable;
import net.zdsoft.bigdata.dataimport.service.BgDaCustomTableService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:zhujy
 * @since:2019/6/17 19:06
 */
@Service("bgDaCustomTableService")
public class BgDaCustomTableServiceImpl extends BaseServiceImpl<BgDaCustomTable, String> implements BgDaCustomTableService{

    @Autowired
    private BgDaCustomTableDao bgDaCustomTableDao;

    @Autowired
    private BgDaCustomTableColumnDao bgDaCustomTableColumnDao;

    @Autowired
    private MysqlClientService mysqlClientService;


    @Override
    protected BaseJpaRepositoryDao getJpaDao() {
        return bgDaCustomTableDao;
    }

    @Override
    protected Class getEntityClass() {
        return BgDaCustomTable.class;
    }

    @Override
    public void deleteTableList(String id) {
        String tableName=bgDaCustomTableDao.findTableNameById(id);
        if (tableName==null){
            throw  new  RuntimeException("表已经被删除了，请刷新");
        }
        String sql="drop table "+tableName;
        mysqlClientService.execSql(null,null,sql,null);
        bgDaCustomTableColumnDao.deleteByTableId(id);
        bgDaCustomTableDao.deleteById(id);
    }

    @Override
    public String findIdByTableName(String tableName) {
        return bgDaCustomTableDao.findIdByTableName(tableName);
    }

    @Override
    public void updateDataNumByTableName(String dataNum, String tableName) {
       bgDaCustomTableDao.updateDataNumByTableName(dataNum,tableName);
    }

    @Override
    public void updateDataNumToZeroByTableName(String dataNum, String tableName) {
        bgDaCustomTableDao.updateDataNumToZeroByTableName(dataNum,tableName);
    }


}
