package net.zdsoft.bigdata.data.manager.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用数据库数据查询
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 下午1:40
 */
public class IDataSourceImpl extends AbstractDataSource {

    private Logger logger;

    protected DataSource dataSource;
    protected DatabaseType databaseType;
    protected boolean enable = true;
    protected String stackTrace;


    public IDataSourceImpl(DatabaseType databaseType, boolean enable, String stackTrace, String uri, String username, String password) {
        super(DataSourceType.DB, username, password, uri);
        this.databaseType = databaseType;
        this.enable = enable;
        this.stackTrace = stackTrace;
        this.dataSource = null;
    }


    public IDataSourceImpl(String username, String password, String uri, DatabaseType databaseType) {
        super(DataSourceType.DB, username, password, uri);
        this.databaseType = databaseType;
    }

    @Override
    public void afterPropertiesSet() throws IDataSourceInitException {
        try {
            IDataSourceUtils.checkDatabaseConfig(getUsername(), getPassword(), getUri(), databaseType);
        } catch (IDataSourceInitException | ClassNotFoundException e) {
            this.enable = false;
            this.stackTrace = ExceptionUtils.getStackTrace(e);
            throw new IDataSourceInitException("数据源配置错误", e, getUri());
        }
        try {
            //创建数据库连接池或者手动持有数据库连接池
            dataSource = IDataSourceUtils.createNativeDataSource(getUsername(), getPassword(), getUri(), databaseType);
        } catch (SQLException e) {
            this.enable = false;
            this.stackTrace = ExceptionUtils.getStackTrace(e);
            throw new IDataSourceInitException("数据源初始化失败", e, getUri());
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("dataSource [{}] created", getUri());
        }
    }

    @Override
    public Object executeQuery(String queryStatement) throws IQueryException {
        return executeQuery(queryStatement, -1);
    }

    /**
     * @see IColumn
     * return IColumn or IColumn[]
     */
    @Override
    public Object executeQuery(String queryStatement, long timeout) throws IQueryException {
        ResultSet resultSet = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(queryStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        )
        {
            statement.setQueryTimeout((int) (timeout / 1000));
            resultSet = statement.executeQuery();
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            IColumn[] iColumnArray = ISQLUtils.parseColumns(queryStatement, () -> {
                IColumn[] iColumns = new IColumn[resultSetMetaData.getColumnCount()];
                for (int i = 1, length = iColumns.length; i <= length; i++) {
                    /*
                     * 经测试，在MySql数据库获取列名称会区分大小写而Oracle不会区分大小写
                     * MySql 5.7 mysql-connector-java-5.1.39-bin.jar
                     * Oracle eis6_develop classes12-10.2.0.2.0.jar
                     */
                    iColumns[i - 1] = new IColumn(StringUtils.lowerCase(resultSetMetaData.getColumnLabel(i)), resultSetMetaData.getColumnType(i));
                }
                return iColumns;
            });
            List<IColumn[]> values = new ArrayList<>();
            while (resultSet.next()) {
                IColumn[] iColumnResultArray = new IColumn[iColumnArray.length];
                for (int i = 0; i < iColumnArray.length; i++) {
                    iColumnResultArray[i] = ISQLUtils.parseValue(resultSet, new IColumn(iColumnArray[i].getName(), iColumnArray[i].getType()));
                }
                values.add(iColumnResultArray);
            }
            if (values.isEmpty()) {
                return "[]";
            }
            return IColumn.toJSONArrayString(values);
        } catch (SQLException e) {
            throw new IQueryException("Query Error " + e.getMessage(), e, getUri(), queryStatement);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("close result exception");
                }
            }
        }
    }

    @Override
    public ResultSet executeResultSet(String queryStatement, long timeout) throws IQueryException {
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(queryStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (timeout > 0) {
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("Database Query [{}], timeout [{}]", queryStatement, timeout);
                }
                statement.setQueryTimeout((int) (timeout / 1000));
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new IQueryException("Query Error " + e.getMessage(), e, getUri(), queryStatement);
        }
    }

    @Override
    public Object executeCreateSql(String createSql) throws IQueryException {
        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(createSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new IQueryException("执行出错 " + e.getMessage(), e, getUri(), createSql);
        }
    }

    @Override
    public void destroy() throws IQueryException {
        if (this.dataSource != null) {
            ((DruidDataSource) dataSource).close();
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("dataSource [{}] destroy", getUri());
            }
        }
    }

    @Override
    protected Logger getLogger() {
        if (this.logger != null) {
            return this.logger;
        }
        synchronized (dataSource) {
            if (logger == null) {
                this.logger = LoggerFactory.getLogger(IDataSourceImpl.class);
            }
        }
        return this.logger;
    }

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public String getInitStackTrace() {
        return this.stackTrace;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }
}
