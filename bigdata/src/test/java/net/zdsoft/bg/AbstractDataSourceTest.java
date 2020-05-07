package net.zdsoft.bg;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.manager.datasource.IColumn;
import net.zdsoft.bigdata.data.manager.api.IDataSource;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceImpl;
import net.zdsoft.bigdata.data.manager.datasource.IQueryException;
import net.zdsoft.bigdata.data.manager.datasource.ISQLUtils;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午1:23
 */
public abstract class AbstractDataSourceTest {

	protected IDataSource iDataSource;

	@Before
	public void prepare() {
		iDataSource = new IDataSourceImpl(getUsername(), getPassword(), getJdbcUrl(), getDatabaseType());
	}

	@After
	public void destory() throws IQueryException {
		if (iDataSource != null) {
			iDataSource.destroy();
		}
	}

	//protected abstract DataSourceType getDataSourceType();

	protected abstract String getUsername();

	protected abstract String getPassword();

	protected abstract String getJdbcUrl();

	protected abstract DatabaseType getDatabaseType();

	protected ResultSet query(String sql) throws SQLException {
		Connection connection = ((IDataSourceImpl)iDataSource).getDataSource().getConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		return statement.executeQuery();
	}

	protected IColumn[] parseIColumnArray(String sql, ResultSet resultSet) throws SQLException {
		return ISQLUtils.parseColumns(sql, () -> {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			IColumn[] iColumns = new IColumn[resultSetMetaData.getColumnCount()];
			for (int i = 1, length = iColumns.length; i<=length; i++) {
				iColumns[i-1] = new IColumn(resultSetMetaData.getColumnLabel(i), resultSetMetaData.getColumnType(i));
			}
			return iColumns;
		});
	}
}
