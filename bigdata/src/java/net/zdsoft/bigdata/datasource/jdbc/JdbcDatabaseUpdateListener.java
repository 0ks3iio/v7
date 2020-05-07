package net.zdsoft.bigdata.datasource.jdbc;

import net.zdsoft.bigdata.datasource.event.DatabaseEvent;
import net.zdsoft.bigdata.datasource.event.DatabaseListener;
import net.zdsoft.bigdata.datasource.event.JdbcDatabaseUpdateEvent;

/**
 * @author shenke
 * @since 2018/11/28 上午10:07
 */
public class JdbcDatabaseUpdateListener implements DatabaseListener {

    @Override
    public void onEvent(DatabaseEvent event) {
        if (event instanceof JdbcDatabaseUpdateEvent) {
            JdbcQueryUtils.removeCache(((JdbcDatabaseUpdateEvent) event).getJdbcDatabaseAdapter());
        }
    }
}
