package net.zdsoft.bigdata.datasource;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.function.Function;

/**
 * @author shenke
 * @since 2018/11/28 下午4:16
 */
public interface DatasourceExecutor<E, T> {

    T execute(E executor);

    static <R> DatasourceExecutor<JdbcTemplate, R> jdbcExecutor(Function<JdbcTemplate, R> apply) {
        return apply::apply;
    }

    static DatasourceExecutor<JdbcTemplate, JdbcTemplate> jdbcTemplate() {
        return executor -> executor;
    }
}
