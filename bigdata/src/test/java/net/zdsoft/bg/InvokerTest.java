package net.zdsoft.bg;

import net.zdsoft.BaseTest;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dao.DatabaseDao;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.manager.InvocationBuilder;
import net.zdsoft.bigdata.data.manager.api.IDataSource;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceInitException;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceServiceImpl;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.bigdata.data.manager.datasource.IQueryException;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午2:39
 */
@Rollback
@Transactional(transactionManager = "txManagerJap")
public class InvokerTest extends BaseTest {

	@Resource
	private Invoker invoker;
	@Resource
	private DatabaseDao databaseDao;
	@Resource
	private IDataSourceServiceImpl iDataSourceServiceImpl;

	private Database database;
	private Database mysql;
	private Database sqlServer;
	private Database exceptionDataSource;

	@Before
	public void insertDatabase() throws InvocationTargetException, IllegalAccessException {
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

		mysql = new Database();
		mysql.setId(UuidUtils.generateUuid());
		mysql.setDbName("uc");
		mysql.setDomain("localhost");
		mysql.setPort(3306);
		mysql.setUsername("root");
		mysql.setPassword("123456");
		mysql.setType(DatabaseType.MYSQL.getType());
		mysql.setName("mysql-uc");
		mysql.setCreationTime(new Date());
		mysql.setModifyTime(mysql.getCreationTime());

		sqlServer = new Database();
		sqlServer.setId(UuidUtils.generateUuid());
		sqlServer.setDbName("eis_fj");
		sqlServer.setDomain("192.168.0.156");
		sqlServer.setPort(1433);
		sqlServer.setUsername("sa");
		sqlServer.setPassword("zdsoft.net2010");
		sqlServer.setType(DatabaseType.SQL_SERVER.getType());
		sqlServer.setName("sqlserver");
		sqlServer.setCreationTime(new Date());
		sqlServer.setModifyTime(sqlServer.getCreationTime());

		exceptionDataSource = new Database();
		BeanUtils.copyProperties(exceptionDataSource, sqlServer);
		exceptionDataSource.setDomain("192.168.0.155");
		exceptionDataSource.setId(UuidUtils.generateUuid());

		databaseDao.save(Arrays.asList(database, sqlServer, mysql, exceptionDataSource));
	}


	@Test
	public void _getIDataSource() throws IQueryException, IDataSourceInitException {
		//oracle
		IDataSource oracleDataSource = iDataSourceServiceImpl.getIDataSource(database.getId(), DataSourceType.DB);
		Assert.assertNotNull(oracleDataSource);
		IDataSource mysqlDataSource = iDataSourceServiceImpl.getIDataSource(mysql.getId(), DataSourceType.DB);
		Assert.assertNotNull(mysqlDataSource);
		IDataSource sqlServerDataSource = iDataSourceServiceImpl.getIDataSource(sqlServer.getId(), DataSourceType.DB);
		Assert.assertNotNull(sqlServerDataSource);
	}

	@Test
	public void _createDataSourceError() {
		try {
			boolean error = iDataSourceServiceImpl.checkDataSource(IDataSourceUtils.createLazyIDataSource(exceptionDataSource));
			Assert.assertFalse(error);
		} catch (Exception e) {
			Assert.assertThat(e, new ExceptionMatcher<>(IDataSourceInitException.class, "数据源初始化失败"));
		}
	}

	@Test
	public void _createDataSourceTrue() throws SQLException, IDataSourceInitException {
		boolean error = iDataSourceServiceImpl.checkDataSource(IDataSourceUtils.createLazyIDataSource(sqlServer));
		Assert.assertTrue(error);
	}

	@Test
	public void _invoke() {
		Result result = invoker.invoke(
				InvocationBuilder.getInstance().type(DataSourceType.DB).dataSourceId(database.getId())
						.queryStatement("select id as xId from base_user where username='shengld'")
						.timeout(2 * 1000).build()
		);
		if (!result.hasError()) {
			System.out.println(result.getValue().toString());
		}
		Result mysqlResult = invoker.invoke(
				InvocationBuilder.getInstance().type(DataSourceType.DB).dataSourceId(mysql.getId())
						.queryStatement("select id as mysqlId from ac_user ")
						.timeout(2 * 1000).build()
		);
		if (!mysqlResult.hasError()) {
			System.out.println(mysqlResult.getValue().toString());
		}

		Result sqlServerResult = invoker.invoke(
				InvocationBuilder.getInstance().type(DataSourceType.DB).dataSourceId(sqlServer.getId())
						.queryStatement("select classid as sqlServerId from basic_class")
						.timeout(2 * 1000).build()
		);
		if (!sqlServerResult.hasError()) {
			System.out.println(sqlServerResult.getValue().toString());
		}
	}

	@Test
	public void _invoke_retry() {
		Result sqServerResult = invoker.invoke(
				InvocationBuilder.getInstance().dataSourceId(sqlServer.getId()).type(DataSourceType.DB)
					.queryStatement("select classid as sqlServerId from basic_class")
				.timeout(2 * 1000).autoRetries(3).build()
		);
		if (sqServerResult.hasError()) {
			System.out.println(ExceptionUtils.getStackTrace(sqServerResult.getException()));
		}
	}
}
