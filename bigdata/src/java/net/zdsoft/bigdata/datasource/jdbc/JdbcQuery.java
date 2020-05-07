package net.zdsoft.bigdata.datasource.jdbc;

import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.datasource.AbstractQuery;
import net.zdsoft.bigdata.datasource.CheckResponse;
import net.zdsoft.bigdata.datasource.DataType;
import net.zdsoft.bigdata.datasource.Query;
import net.zdsoft.bigdata.datasource.QueryExtractor;
import net.zdsoft.bigdata.datasource.QueryStatement;
import net.zdsoft.bigdata.datasource.QueryStatementWithArgs;
import net.zdsoft.bigdata.datasource.Statement;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 关系型数据库查询
 * @author shenke
 * @since 2018/11/26 下午2:37
 */
@Component(JdbcQuery.NAME)
public class JdbcQuery extends AbstractQuery implements Query {

    public static final String NAME = "jdbcQuery";

    private Set<DataType> supportTypes;

    @PostConstruct
    public void init() {
        supportTypes = new HashSet<>(8);
        supportTypes.add(DatabaseType.ORACLE);
        supportTypes.add(DatabaseType.MYSQL);
        supportTypes.add(DatabaseType.DB2);
        supportTypes.add(DatabaseType.SQL_SERVER);
        supportTypes.add(DatabaseType.PostgreSQL);
        supportTypes.add(DatabaseType.IMPALA);
        supportTypes.add(DatabaseType.KYLIN);
        supportTypes.add(DatabaseType.HBASE);
        supportTypes.add(DatabaseType.HADOOP_HIVE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws SQLException {
        //JdbcDatabaseAdapter 一定要在这里创建
        //以为
        JdbcDatabaseAdapter databaseKey = JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder
                .builder().buildWithCache((JdbcDatabaseAdapter) queryStatement.getAdapter());
        Object[] args = null;
        if (queryStatement instanceof QueryStatementWithArgs) {
            args = ((QueryStatementWithArgs) queryStatement).getArgs();
        }
        return (VL) JdbcQueryUtils.nativeQueryFor(databaseKey, queryStatement.getQueryStatement(), args, rs->{
            try {
                return extractor.extractData(rs);
            } catch (Throwable throwable) {
                JdbcDatasourceBuilder.throwAny(throwable);
                //never run here
                return null;
            }
        });
    }

    @Override
    protected Set<DataType> getSupportDatabaseType() {
        return supportTypes;
    }

    @Override
    public CheckResponse check(Statement statement) {
        JdbcDatabaseAdapter adapter = (JdbcDatabaseAdapter) statement.getAdapter();
        try (Connection ignored = DriverManager.getConnection(JdbcBuilder.buildNoCache(adapter), adapter.getUsername(), adapter.getPassword())) {
            return CheckResponse.ok();
        } catch (Throwable e) {
            return CheckResponse.error(e);
        }
    }

    @Override
    public <T> T getNativeExecutor(Statement statement) throws SQLException {
        if (statement.getAdapter() instanceof JdbcDatabaseAdapter) {
            return (T) JdbcQueryUtils.getJdbcTemplate((JdbcDatabaseAdapter) statement.getAdapter());
        }
        throw new IllegalArgumentException(String.format("Not support adapter %s", statement.getAdapter().getClass().getName()));
    }
}
