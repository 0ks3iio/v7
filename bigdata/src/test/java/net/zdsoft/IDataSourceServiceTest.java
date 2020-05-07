package net.zdsoft;

import net.zdsoft.bg.ExceptionMatcher;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.code.ChartType;
import net.zdsoft.bigdata.data.dao.ChartDao;
import net.zdsoft.bigdata.data.dao.CockpitChartDao;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.manager.api.IDataSourceService;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceInitException;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author ke_shen@126.com
 * @since 2018/4/17 上午10:00
 */
@Rollback
@Transactional(transactionManager = "txManagerJap")
public class IDataSourceServiceTest extends BaseTest {

    @Resource
    private IDataSourceService iDataSourceService;
    @Resource
    private CockpitChartDao cockpitChartDao;
    @Resource
    private ChartDao chartDao;


    private Database database;
    private Database errorDatabase;

    @Before
    public void createCorrectAndErrorDatabase() throws InvocationTargetException, IllegalAccessException {
        database = new Database();
        database.setDbName("center");
        database.setDomain("192.168.0.155");
        database.setPort(1521);
        database.setUsername("eis6_develop");
        database.setPassword("zdsoft6");
        database.setType(DatabaseType.ORACLE.getType());
        database.setName("eis6");
        database.setCreationTime(new Date());
        database.setModifyTime(new Date());
        database.setId(UuidUtils.generateUuid());

        errorDatabase = new Database();
        BeanUtils.copyProperties(errorDatabase, database);
        errorDatabase.setUsername("eis2");
    }

    @Test
    public void _checkDataSource() throws IDataSourceInitException {
        boolean error = iDataSourceService.checkDataSource(IDataSourceUtils.createLazyIDataSource(database));
        Assert.assertTrue(error);

        try {
            iDataSourceService.checkDataSource(IDataSourceUtils.createLazyIDataSource(errorDatabase));
        } catch (IDataSourceInitException e) {
            System.out.println(e.getMessage());
            Assert.assertThat(e, new ExceptionMatcher<>(IDataSourceInitException.class, "数据源配置错误"));
        }
    }

    @Test
    public void _isUsed() {
        //prepare data
        Chart cockpitChart = new Chart();
        cockpitChart.setName("line 测试");
        cockpitChart.setChartType(ChartType.LINE_BROKEN);
        cockpitChart.setDatabaseId(database.getId());
        //cockpitChart.setC(UuidUtils.generateUuid());
        cockpitChart.setDataSet("");
        cockpitChart.setCreationTime(new Date());
        cockpitChart.setModifyTime(cockpitChart.getCreationTime());
        //cockpitChart.set(UuidUtils.generateUuid());
        cockpitChart.setDataSourceType(DataSourceType.DB.getValue());
        cockpitChart.setId(UuidUtils.generateUuid());
        cockpitChart.setApiId(UuidUtils.generateUuid());
        chartDao.save(cockpitChart);

        //存在引用
        boolean used = iDataSourceService.isUsed(cockpitChart.getDatabaseId(), DataSourceType.DB);
        Assert.assertTrue(used);
        used = iDataSourceService.isUsed(cockpitChart.getApiId(), DataSourceType.API);
        Assert.assertTrue(used);

        //不存在引用
        used = iDataSourceService.isUsed("1111111", DataSourceType.DB);
        Assert.assertFalse(used);

        used = iDataSourceService.isUsed("222", DataSourceType.API);
        Assert.assertFalse(used);
    }
}
