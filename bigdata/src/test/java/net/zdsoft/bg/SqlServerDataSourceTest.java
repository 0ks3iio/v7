package net.zdsoft.bg;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.manager.datasource.IColumn;
import net.zdsoft.bigdata.data.manager.datasource.IQueryException;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午2:15
 */
public class SqlServerDataSourceTest extends AbstractDataSourceTest {

	@Override
	protected String getUsername() {
		return "sa";
	}

	@Override
	protected String getPassword() {
		return "zdsoft.net2010";
	}

	@Override
	protected String getJdbcUrl() {
		return "jdbc:sqlserver://192.168.0.156:1433;database=eis_fj";
	}

	@Override
	protected DatabaseType getDatabaseType() {
		return DatabaseType.SQL_SERVER;
	}

	@Test
	public void _parseColumns() throws SQLException {
		String sql = "select * from basic_class";
		ResultSet resultSet = query(sql);
		IColumn[] iColumns = parseIColumnArray(sql, resultSet);
		Assert.assertTrue(iColumns.length > 1);
		System.out.println(Arrays.toString(iColumns));
	}

	@Test
	public void _parseColumnWithAs() throws SQLException {
		String sql = "select classid as xId from basic_class";
		ResultSet resultSet = query(sql);
		IColumn[] iColumns = parseIColumnArray(sql, resultSet);
		Assert.assertTrue(iColumns.length == 1);
		System.out.println(Arrays.toString(iColumns));
	}

	@Test
	public void _executeQuery() throws IQueryException {
		Object object = iDataSource.executeQuery("select classid as xId from basic_class where classid='A2D016DF-293B-4CBA-BEE1-00CF5E10827D'");
		System.out.println(object);
	}
}
