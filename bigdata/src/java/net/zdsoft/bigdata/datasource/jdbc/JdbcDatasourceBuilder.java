package net.zdsoft.bigdata.datasource.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import net.zdsoft.bigdata.data.DatabaseType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用数据库连接池
 * @author shenke
 * @since 2018/11/26 下午2:53
 */
final class JdbcDatasourceBuilder {

    static final Integer initialSize = 2;
    static final Integer minIdle = 1;
    static final Integer maxActive = 20;
    static final Integer connectErrorRetry = 5;

    static DataSource buildNativeDatasource(JdbcDatabaseAdapter databaseKey, String jdbcUrl) throws SQLException {
        return nativeForPool(databaseKey, jdbcUrl);
    }

    public static <E extends Throwable> void throwAny(Throwable e) throws E {
        throw (E) e;
    }

    private static DruidDataSource nativeForPool(JdbcDatabaseAdapter database, String url) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(database.getUsername());
        dataSource.setPassword(database.getPassword());
        //if (MYSQL.equals(databaseType)) {
        //    dataSource.setPoolPreparedStatements(false);
        //} else if (ORACLE.equals(databaseType)) {
        //    dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        //}
        dataSource.setInitialSize(2);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(20);
        dataSource.setConnectionErrorRetryAttempts(5);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setDriverClassName(DatabaseType.parse(database.getDataType().getType()).getDriverName());
        dataSource.setUrl(url);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(false);
        //dataSource.setQueryTimeout((int) (Invocation.QUERY_TIME_OUT / 1000));
        dataSource.init();
        return dataSource;
    }

}
