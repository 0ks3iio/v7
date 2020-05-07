package net.zdsoft.bigdata.datasource.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenke
 * @since 2018/11/26 下午3:21
 */
final class JdbcQueryUtils {

    private static Map<JdbcDatabaseAdapter, JdbcTemplate> cache = new ConcurrentHashMap<>();

    static Object nativeQueryFor(JdbcDatabaseAdapter databaseKey, String sql, ResultSetExtractor<?> extractor) throws SQLException {
        return getJdbcTemplate(databaseKey).query(sql, extractor);
    }

    static Object nativeQueryFor(JdbcDatabaseAdapter databaseKey, String sql, Object[] args, ResultSetExtractor<?> extractor) throws SQLException {
        if (args == null) {
            return nativeQueryFor(databaseKey, sql, extractor);
        }
        return getJdbcTemplate(databaseKey).query(sql, args, extractor);
    }

    static void removeCache(JdbcDatabaseAdapter databaseAdapter) {
        cache.remove(databaseAdapter);
    }

    static JdbcTemplate getJdbcTemplate(JdbcDatabaseAdapter databaseKey) throws SQLException {
        synchronized (databaseKey) {
            return cache.computeIfAbsent(databaseKey, k -> {
                try {
                    return new JdbcTemplate(JdbcDatasourceBuilder.buildNativeDatasource(databaseKey, JdbcBuilder.build(databaseKey)), true);
                } catch (SQLException e) {
                    JdbcDatasourceBuilder.throwAny(e);
                    //never here
                    return null;
                }
            });
        }
    }
}