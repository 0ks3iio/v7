package net.zdsoft.framework.config;

import java.io.Serializable;

import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

public class Session implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Session ID */
    private String id;
    /** Session创建时间 */
    private long creationTime;
    /** Session最后一次访问时间 */
    private long lastAccessedTime;
    /** Session的最大空闲时间间隔 */
    private int maxInactiveInterval;
    /** 是否是新建Session */
    private boolean newSession;

    private static final String SESSION_KEY_PREFIX = "SESSION_";
    private String sessionKey;

    public Session() {
    }

    /**
     * 创建新的Session。
     * 
     * @param maxIdleSeconds
     */
    public Session(String sessionId, int maxIdleSeconds) {
        this.id = sessionId;
        long now = System.currentTimeMillis();
        creationTime = now;
        lastAccessedTime = now;
        this.maxInactiveInterval = maxIdleSeconds;
        newSession = true;

        sessionKey = SESSION_KEY_PREFIX + id;
        RedisUtils.setObject(sessionKey, this, this.maxInactiveInterval);
    }

    public Session(String sessionId) {
        this.id = sessionId;
        long now = System.currentTimeMillis();
        creationTime = now;
        lastAccessedTime = now;
        String v = Evn.getString("redis_sesion_timeout");
        if (StringUtils.isBlank(v))
            v = "108000";
        this.maxInactiveInterval = NumberUtils.toInt(v); // 半小时
        newSession = true;

        sessionKey = SESSION_KEY_PREFIX + id;
        RedisUtils.setObject(sessionKey, this, this.maxInactiveInterval);
    }

    /**
     * 通过Session id获取已经存在的Session，如果没有，返回null。
     * 
     * @return
     */
    public static Session get(String id) {
        String sessionKey = SESSION_KEY_PREFIX + id;
        Session session = RedisUtils.getObject(sessionKey, Session.class);
        if (session != null) {
            session.setSessionKey(sessionKey);
            session.newSession = false;
            session.refresh();
        }
        return session;
    }

    @JSONField(serialize = false)
    public static Session getWithCreate(String id) {
        Session session = get(id);
        if (session == null) {
            session = new Session(id);
            String sessionKey = SESSION_KEY_PREFIX + id;
            session.setSessionKey(sessionKey);
        }
        
        return session;
    }

    /**
     * 更新 lastAccessedTime 。
     */
    public void refresh() {
        this.lastAccessedTime = System.currentTimeMillis();
        RedisUtils.setObject(sessionKey, this, this.maxInactiveInterval);
        RedisUtils.expire(getSessionAttributeKey(), this.maxInactiveInterval);
    }

    /**
     * 是否超时过期。
     * 
     * @param session
     * @return
     */
    @JSONField(serialize = false)
    public boolean isExpired() {
        Session session = get(sessionKey);
        // 先查看缓存层面的超时控制
        if (session == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        long last = this.getLastAccessedTime();
        long interal = now - last;
        if (interal > this.getMaxInactiveInterval()) {
            this.invalidate();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 强制Session立即失效。
     */
    public synchronized void invalidate() {
        RedisUtils.del(sessionKey);
        RedisUtils.del(getSessionAttributeKey());
    }

    /**
     * 移除属性。
     * 
     * @param attrName
     * @return
     */
    public synchronized void removeAttribute(String attrName) {
        this.refresh();
        RedisUtils.hdel(getSessionAttributeKey(), attrName);
    }

    /**
     * 设置属性。
     * 
     * @param attrName
     * @param attrValue
     */
    public synchronized <T> void setAttribute(String attrName, T attrValue) {
        this.refresh();
        RedisUtils.hsetObject(getSessionAttributeKey(), attrName, attrValue);
    }

    @JSONField(serialize = false)
    public <T> T getAttribute(String attrName,TypeReference<T> type) {
        this.refresh();
        return RedisUtils.hgetObject(getSessionAttributeKey(), attrName, type);
    }
    /**
     * 获取属性的值。
     * 
     * @param attrName
     * @return
     */ 
    @JSONField(serialize = false)
    public <T> T getAttribute(String attrName, Class<T> clazz) {
        this.refresh();
        return RedisUtils.hgetObject(getSessionAttributeKey(), attrName, clazz);
    }
    

    public String getSessionAttributeKey() {
        return sessionKey + "_Attributes";
    }

    public int getMaxInactiveInterval() {
        if (maxInactiveInterval == -1) {
            maxInactiveInterval = 3600;
        }
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public String getId() {
        return id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public boolean isNewSession() {
        return newSession;
    }

    @JSONField(serialize = false)
    public LoginInfo getLoginInfo() {
        return getAttribute("loginInfo", LoginInfo.class);
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        setAttribute("loginInfo", loginInfo);
        this.refresh();
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

	public String getSessionKey() {
		return sessionKey;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public void setNewSession(boolean newSession) {
		this.newSession = newSession;
	}
}
