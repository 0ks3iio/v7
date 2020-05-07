package net.zdsoft.bigdata.datasource.event;

import net.zdsoft.bigdata.datasource.redis.RedisDatabaseAdapter;

/**
 * @author shenke
 * @since 2018/11/27 下午5:58
 */
public class RedisDatabaseUpdateEvent extends DatabaseEvent {

    public RedisDatabaseUpdateEvent(RedisDatabaseAdapter source) {
        super(source);
    }

    public final RedisDatabaseAdapter getRedisDatabaseAdapter() {
        return (RedisDatabaseAdapter) source;
    }
}
