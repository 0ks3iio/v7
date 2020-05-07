package net.zdsoft.dataimport.cache;

import com.alibaba.fastjson.JSON;
import net.zdsoft.dataimport.config.ImportActionAdvice;
import net.zdsoft.dataimport.biz.ImportRecord;
import net.zdsoft.dataimport.biz.ImportState;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shenke
 * @since 17-8-5 下午11:07
 */
@Component
public class ViewCache {

    @Resource(name = "redisTemplate") //framework
    private RedisTemplate redisTemplate;

    static final String KEY = "key_import_record_";

    private Lock lock = new ReentrantLock();

    public List<ImportRecord> getFromCache(String userId) {
        try {

            lock.lock();
            List<Object> object = redisTemplate.opsForList().range(KEY + getCurrentAction() + userId, 0, 20);
            return JSON.parseArray(object.toString(), ImportRecord.class);
        } finally {
            lock.unlock();
        }
    }

    public void add(String userId, ImportRecord importRecord) {
        try {
            lock.lock();
            long size = redisTemplate.opsForList().size(KEY + getCurrentAction() + userId);
            importRecord.setRedisIndex(size);
            redisTemplate.opsForList().rightPush(KEY + getCurrentAction() + userId, importRecord);
        } finally {
            lock.unlock();
        }
    }

    public void update(String userId, ImportState importState, ImportRecord importRecord) {
        try {
            lock.lock();
            redisTemplate.opsForList().remove(userId, 0, importRecord);
            importRecord.setStateCode(importState.getCode());
            importRecord.setStateMsg(importState.getState());
            redisTemplate.opsForList().set(KEY + getCurrentAction() + userId, importRecord.getRedisIndex(), importRecord);
        } finally {
            lock.unlock();
        }
    }

    private String getCurrentAction() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ( requestAttributes != null ) {
            return requestAttributes.getRequest().getAttribute(ImportActionAdvice.ACTION_NAME).toString();
        }
        return "";
    }
}
