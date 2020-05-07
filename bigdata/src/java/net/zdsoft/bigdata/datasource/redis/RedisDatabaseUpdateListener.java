package net.zdsoft.bigdata.datasource.redis;

import net.zdsoft.bigdata.datasource.event.DatabaseEvent;
import net.zdsoft.bigdata.datasource.event.DatabaseListener;
import net.zdsoft.bigdata.datasource.event.RedisDatabaseUpdateEvent;

/**
 * @author shenke
 * @since 2018/11/28 上午10:15
 */
public class RedisDatabaseUpdateListener implements DatabaseListener {

    @Override
    public void onEvent(DatabaseEvent event) {
        if (event instanceof RedisDatabaseUpdateEvent) {
            RedisTemplateBuilder.removeCache(((RedisDatabaseUpdateEvent) event).getRedisDatabaseAdapter());
        }
    }
}
