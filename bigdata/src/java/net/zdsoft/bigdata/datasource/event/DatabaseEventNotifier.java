package net.zdsoft.bigdata.datasource.event;

import net.zdsoft.framework.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import redis.clients.jedis.BinaryJedisPubSub;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 业务代码调用
 * @author shenke
 * @since 2018/11/27 下午6:00
 */
@Component
@Lazy(false)
public final class DatabaseEventNotifier {

    private static List<DatabaseListener> listeners = new ArrayList<>();
    private static final byte[] CHANEL_KEY = "bigdata:datasource::change-event".getBytes(Charset.forName("UTF-8"));
    private static final RedisSerializer serializer = new JdkSerializationRedisSerializer();

    static {
        ServiceLoader.load(DatabaseListener.class).iterator().forEachRemaining(listeners::add);
    }

    public static void notify(DatabaseEvent databaseEvent) {
        //推送到redis
        RedisUtils.publish(CHANEL_KEY, serializer.serialize(databaseEvent));
    }

    /**
     * 初始化订阅通道
     */
    @PostConstruct
    void initSubscribe() {
        RedisUtils.subscribe(new DatabaseEventListener(), CHANEL_KEY);
    }

    private final static class DatabaseEventListener extends BinaryJedisPubSub {

        private Logger logger = LoggerFactory.getLogger(DatabaseEventListener.class);

        @Override
        public void onMessage(byte[] channel, byte[] message) {
            DatabaseEvent databaseEvent = null;
            try {
                databaseEvent = (DatabaseEvent) serializer.deserialize(message);
            } catch (SerializationException e) {
                logger.error("订阅的消息不符合指定的规则", e);
                return;
            }
            for (DatabaseListener listener : listeners) {
                notifyIgnoreException(listener, databaseEvent);
            }
        }

        private void notifyIgnoreException(DatabaseListener databaseListener, DatabaseEvent databaseEvent) {
            try {
                databaseListener.onEvent(databaseEvent);
            } catch (Exception e) {
                logger.error("Notify listener {} error", databaseListener.getClass().getName(), e);
            }
        }
    }
}
