package net.zdsoft.bigdata.data.manager.datasource;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.api.IDataSource;
import org.slf4j.Logger;

import java.sql.ResultSet;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午4:44
 */
public abstract class AbstractDataSource implements IDataSource {

    private DataSourceType dataSourceType;
    private String username;
    private String password;
    private String uri;

    public AbstractDataSource() {
    }

    public AbstractDataSource(DataSourceType dataSourceType) {
        this(dataSourceType, null);
    }

    public AbstractDataSource(DataSourceType dataSourceType, String uri) {
        this(dataSourceType, null, null, uri);
    }

    public AbstractDataSource(DataSourceType dataSourceType, String username, String password, String uri) {
        this.dataSourceType = dataSourceType;
        this.username = username;
        this.password = password;
        this.uri = uri;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public DataSourceType getDataSourceType() {
        return this.dataSourceType;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractDataSource that = (AbstractDataSource) o;

        if (dataSourceType != that.dataSourceType) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return uri != null ? uri.equals(that.uri) : that.uri == null;
    }

    @Override
    public int hashCode() {
        int result = dataSourceType != null ? dataSourceType.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public ResultSet executeResultSet(String queryStatement) throws IQueryException {
        return executeResultSet(queryStatement, -1);
    }

    @Override
    public ResultSet executeResultSet(String queryStatement, long timeout) throws IQueryException {
        throw new IQueryException("该数据源不支持查询ResultSet结果集", "", queryStatement);
    }

    @Override
    public Object executeCreateSql(String createSql) throws IQueryException {
        return new IQueryException("该数据源不支持创建ResultSet结果集", "", createSql);
    }

    protected abstract Logger getLogger();
}
