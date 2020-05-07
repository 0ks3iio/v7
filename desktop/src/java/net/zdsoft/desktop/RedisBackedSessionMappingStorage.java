package net.zdsoft.desktop;

import javax.servlet.http.HttpSession;

import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iflytek.edu.ew.session.SessionMappingStorage;

public class RedisBackedSessionMappingStorage implements SessionMappingStorage {
	   private final Log log = LogFactory.getLog(getClass());
	   
	   public synchronized void addSessionById(String mappingId, HttpSession session) {
	     RedisUtils.set("session:" + session.getId() + ":ticket", mappingId);
	     RedisUtils.set("ticket:" + mappingId + ":session", session.getId());
	   }
	   
	   public synchronized void removeBySessionById(String sessionId) {
	     if (log.isDebugEnabled()) {
	       log.debug("Attempting to remove Session=[" + sessionId + "]");
	     }
	     
	     String key = RedisUtils.get("session:" + sessionId + ":ticket");
	     
	     if (log.isDebugEnabled()) {
	       if (key != null) {
	         log.debug("Found mapping for session.  Session Removed.");
	       } else {
	         log.debug("No mapping for session found.  Ignoring.");
	       }
	     }
	     RedisUtils.del(new String[] { "ticket:" + key + ":session" });
	     RedisUtils.del(new String[] { "session:" + sessionId + ":ticket" });
	   }
	   
	   public synchronized String getSessionIdByMappingId(String mappingId) {
	     String sessionId = RedisUtils.get("ticket:" + mappingId + ":session");
	     return sessionId;
	   }
	   
	   public synchronized HttpSession removeSessionByMappingId(String mappingId) {
	     return null;
	   }
}
