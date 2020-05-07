package net.zdsoft.dataimport.cache;

import com.google.common.collect.Maps;
import net.zdsoft.dataimport.biz.ImportState;
import net.zdsoft.dataimport.biz.Reply;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author shenke
 * @since 2017.08.03
 */
@Component
public class ReplyCache {

    private Map<String, Reply> cache = Maps.newConcurrentMap();


    public Reply put(String key, Reply reply) {
        return cache.put(key, reply);
    }

    public Reply get(String key) {
        return cache.get(key);
    }

    public ImportState getState(String key) {
        Reply reply = cache.get(key);

        return reply != null ? reply.getImportState() : null;
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
