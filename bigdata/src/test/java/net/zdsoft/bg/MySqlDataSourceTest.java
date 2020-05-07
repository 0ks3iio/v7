package net.zdsoft.bg;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.manager.datasource.IColumn;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceImpl;
import net.zdsoft.bigdata.data.manager.datasource.IQueryException;
import net.zdsoft.bigdata.data.manager.datasource.ISQLUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * test SQL utils and IDataSourceImpl
 * dataSourceType is MySql
 * @author ke_shen@126.com
 * @since 2018/4/9 上午10:49
 */
public class MySqlDataSourceTest extends AbstractDataSourceTest {


	@Override
	protected String getUsername() {
		return "root";
	}

	@Override
	protected String getPassword() {
		return "123456";
	}

	@Override
	protected String getJdbcUrl() {
		return "jdbc:mysql://localhost:3306/uc?useUnicode=true&characterEncoding=utf8&&failOverReadOnly=false&useSSL=false";
	}

	@Override
	protected DatabaseType getDatabaseType() {
		return DatabaseType.MYSQL;
	}

	@Test
	public void _parseColumns() throws IQueryException, SQLException {
		String sql = "select * from ac_user";
		ResultSet resultSet = query(sql);
		IColumn[] iColumnArray = parseIColumnArray(sql, resultSet);
		System.out.println(Arrays.toString(iColumnArray));
		Assert.assertNotNull(iColumnArray);
	}

	@Test
	public void _parseColumnsWithAs() throws SQLException {
		String sql = "select id as xId from ac_user";
		ResultSet resultSet = query(sql);
		IColumn[] iColumns = parseIColumnArray(sql, resultSet);
		System.out.println(Arrays.toString(iColumns));
	}

	@Test
	public void _parseColumnUseCount() throws SQLException {
		String sql = "select count(*) as x from ac_user";
		ResultSet resultSet = query(sql);
		IColumn[] iColumns = parseIColumnArray(sql, resultSet);
		System.out.println(Arrays.toString(iColumns));
		Assert.assertTrue(iColumns.length == 1);
	}


	@Test
	public void _parseValue() throws SQLException {
		Connection connection = ((IDataSourceImpl)iDataSource).getDataSource().getConnection();
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM ac_user");
		ResultSet resultSet = statement.executeQuery();

		IColumn[] iColumnArray = parseIColumnArray("SELECT * FROM ac_user", resultSet);
		while (resultSet.next()) {
			for (int i = 0; i< iColumnArray.length; i++) {
				ISQLUtils.parseValue(resultSet, iColumnArray[i]);
			}
		}
		System.out.println(Arrays.toString(iColumnArray));
	}

	@Test
	public void _executeQuery() throws IQueryException {
		Object result = iDataSource.executeQuery("select id as x from ac_user");
		if (result.getClass().isArray()) {
			System.out.println(Arrays.toString((IColumn[]) result));
		}
		else {
			System.out.println(result.toString());
		}
		Assert.assertNotNull(result);
	}

	@Test
	public void _executeQueryTimout() throws IQueryException {
		Object result = null;
		try {
			//iDataSource.executeQuery("select sleep(2)");
			iDataSource.executeQuery("select sleep(1)", 2 * 1000);
		} catch (IQueryException e) {
			System.out.println(e.getQueryStatement());
			Assert.assertThat(e, new ExceptionMatcher<>(IQueryException.class, "query Error"));
		}
	}

}
