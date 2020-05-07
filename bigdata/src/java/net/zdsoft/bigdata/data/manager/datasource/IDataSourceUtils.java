package net.zdsoft.bigdata.data.manager.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.manager.api.IDataSource;
import net.zdsoft.bigdata.data.manager.api.Invocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static net.zdsoft.bigdata.data.DatabaseType.*;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午4:29
 */
final public class IDataSourceUtils {

    static boolean checkDatabaseConfig(String username, String password, String uri, DatabaseType databaseType) throws IDataSourceInitException, ClassNotFoundException {
        try {
            DriverManager.getDriver(uri);
        } catch (SQLException e) {
            //no Driver register
            Class.forName(databaseType.getDriverName());
        }
        return checkDatabaseConfig(username, password, uri);
    }

    /**
     * 利用DriverManager#getConnection检查数据库参数是否正常
     */
    static boolean checkDatabaseConfig(String username, String password, String uri) throws IDataSourceInitException {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(uri, username, password);
        } catch (SQLException e) {
            throw new IDataSourceInitException("数据源配置错误", e, uri);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //TODO
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * 创建native数据源
     */
    public static DataSource createNativeDataSource(String username, String password,
                                                    String uri, DatabaseType databaseType) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        if (MYSQL.equals(databaseType)) {
            dataSource.setPoolPreparedStatements(false);
        } else if (ORACLE.equals(databaseType)) {
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        }
        dataSource.setInitialSize(2);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(20);
        dataSource.setConnectionErrorRetryAttempts(5);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setDriverClassName(databaseType.getDriverName());
        dataSource.setUrl(uri);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(false);
        //dataSource.get
        //this is seconds
        dataSource.setQueryTimeout((int) (Invocation.QUERY_TIME_OUT / 1000));
        dataSource.init();
        return dataSource;
    }

    /** 创建数据库型数据源 */
    public static IDataSource createLazyIDataSource(Database database) {
        Pair<String, DatabaseType> pair = buildJDBCUrl(database);
        return new IDataSourceServiceImpl.LazyIDataSource(database.getUsername(), database.getPassword(), pair.getLeft(), pair.getRight());
    }

    public static Pair<String, DatabaseType> buildJDBCUrl(Database database) {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:");
        if (MYSQL.getType().equals(database.getType())) {
        	//jdbc:mysql://MyDbComputerNameOrIP:3306/myDatabaseName
            return Pair.of(builder.append(MYSQL.getName()).append("://").append(database.getDomain())
                    .append(":").append(database.getPort()).append("/")
                    .append(database.getDbName())
                    .append("?useUnicode=true&characterEncoding=")
                    .append(Optional.ofNullable(database.getCharacterEncoding()).orElse(Database.DEFAULT_CHASET_ENCODING)).toString(), MYSQL);
        } else if (ORACLE.getType().equals(database.getType())) {
            //jdbc:oracle:thin:@192.168.0.155:1521:center
            return Pair.of(builder.append(ORACLE.getName()).append(":@").append(database.getDomain())
                    .append(":").append(database.getPort()).append(":")
                    .append(database.getDbName()).toString(), ORACLE);
        } else if (SQL_SERVER.getType().equals(database.getType())) {
            //jdbc:sqlserver://localhost:1433;database=student
            return Pair.of(builder.append(SQL_SERVER.getName()).append("://").append(database.getDomain())
                    .append(":").append(database.getPort()).append(";database=")
                    .append(database.getDbName()).toString(), SQL_SERVER);
        } else if (DB2.getType().equals(database.getType())) {
        	//jdbc:db2://192.9.200.108:6789/SAMPLE
        	 return Pair.of(builder.append(DB2.getName()).append("://").append(database.getDomain())
                     .append(":").append(database.getPort()).append("/")
                     .append(database.getDbName()).toString(), DB2);
        } else if (PostgreSQL.getType().equals(database.getType())) {
        	 // jdbc:postgresql://localhost:5432/myDatabaseName
        	 return Pair.of(builder.append(PostgreSQL.getName()).append("://").append(database.getDomain())
                     .append(":").append(database.getPort()).append("/")
                     .append(database.getDbName()).toString(), PostgreSQL);
        } else if (HBASE.getType().equals(database.getType())) {
            // jdbc:phoenix:172.16.10.113,172.16.10.115,172.16.10.116:2181
            return Pair.of(builder.append(HBASE.getName()).append(":").append(database.getDomain())
                    .append(":").append(database.getPort()).toString(), HBASE);
        }else {
            throw new RuntimeException(String.format("not support database type [%s]", database.getType()));
        }
    }
}
