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
 * @since 2018/4/9 下午1:23
 */
public class OracleDataSourceTest extends AbstractDataSourceTest {

	@Override
	protected String getUsername() {
		return "eis6_develop";
	}

	@Override
	protected String getPassword() {
		return "zdsoft6";
	}

	@Override
	protected String getJdbcUrl() {
		return "jdbc:oracle:thin:@192.168.0.155:1521:center";
	}

	@Override
	protected DatabaseType getDatabaseType() {
		return DatabaseType.ORACLE;
	}

	@Test
	public void _parseColumns() throws SQLException {
		String sql = "select * from base_user";
		ResultSet resultSet = query(sql);
		IColumn[] iColumns = parseIColumnArray(sql, resultSet);
		Assert.assertTrue(iColumns.length > 0);
		System.out.println(Arrays.toString(iColumns));
	}

	@Test
	public void _parseColumnsWithAs() throws SQLException {
		String sql = "select unit_id as unitId from base_user";
		ResultSet resultSet = query(sql);
		IColumn[] iColumns = parseIColumnArray(sql, resultSet);
		Assert.assertTrue(iColumns.length == 1);
		System.out.println(Arrays.toString(iColumns));
	}

	@Test
	public void _executeQuery() throws IQueryException {
		String sql = "select unit_id as unitId from base_user where username='shengld'";
		//ResultSet resultSet = query(sql);
		Object object = iDataSource.executeQuery(sql);
		System.out.println(object);
	}
}
