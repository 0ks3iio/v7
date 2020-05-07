package net.zdsoft.bigdata.datasource.event;

import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;

/**
 * @author shenke
 * @since 2018/11/27 下午5:54
 */
public class JdbcDatabaseUpdateEvent extends DatabaseEvent {

    public JdbcDatabaseUpdateEvent(JdbcDatabaseAdapter source) {
        super(source);
    }

    public final JdbcDatabaseAdapter getJdbcDatabaseAdapter() {
        return (JdbcDatabaseAdapter) source;
    }
}
