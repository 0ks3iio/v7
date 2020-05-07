package net.zdsoft.appstore.remote;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.mina.io.IoSession;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:23:57 $
 */
public class SessionManager {

    private static Logger logger = Logger.getLogger(SessionManager.class);

    private static final String KEY = "sessionName";

    private Map<String, IoSession> sessions = null;
    private Map<String, IoSession> ipTable = null;

    private String lastSessionName = null;
    private IoSession lastSession = null;

    // private boolean ipFilter = true; // 是否对同一IP客户端进行过滤

    public SessionManager() {
        sessions = new ConcurrentHashMap<String, IoSession>();
        ipTable = new ConcurrentHashMap<String, IoSession>();
        // ipFilter = SigninServerLocator.getInstance().isIpFilter();
        // logger.info("SessionManager ipFilter set:"+ipFilter);
    }

    @SuppressWarnings("unchecked")
    public void addSession(String sessionName, IoSession session) {
        // 话机最大连接数量
        int sessionMax = 10000;// Services.getSystemConfigBean().getIntValue("global.signin2.session.max", 10000);
        if (getSize() > sessionMax) {
            logger.error("Connections > " + sessionMax + " ,closed!");
            // removeSession(session);
            session.close();
            return;
        }
        session.setAttribute(KEY, sessionName);
        sessions.put(sessionName, session);
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        IoSession oldSession = ipTable.put(addr.getAddress().getHostAddress(), session);
        if ((oldSession != null) && SigninServerLocator.getInstance().isIpFilter()) {
            removeSession(oldSession);
            oldSession.close();
            if (logger.isInfoEnabled()) {
                logger.info(oldSession.getRemoteAddress() + " closed");
            }
        }

        // 处理留言下发服务器
        // if
        // (addr.getAddress().getHostAddress().equals(SigninServerLocator.getInstance().getMessageDownloadServerIp())) {
        // SigninServerLocator.getInstance().setCurrentMessageDownloadServerId(sessionName);
        // if (logger.isInfoEnabled()) {
        // logger.info("Download message client add :Session[" + sessionName + "]IP:["
        // + addr.getAddress().getHostAddress() + "]");
        // }
        // }

        if (logger.isDebugEnabled()) {
            logger.debug("Session[" + sessionName + "][" + sessions.size() + "] created");
        }
    }

    public IoSession getSession(String sessionName) {
        return sessions.get(sessionName);
    }

    public void removeSession(IoSession session) {
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        ipTable.remove(addr.getAddress().getHostAddress());

        // 处理留言下发服务器
        // if
        // (addr.getAddress().getHostAddress().equals(SigninServerLocator.getInstance().getMessageDownloadServerIp())) {
        // SigninServerLocator.getInstance().setCurrentMessageDownloadServerId(null);
        // if (logger.isInfoEnabled()) {
        // logger.info("Download message client remove :Session[" + (String) session.getAttribute(KEY) + "]IP:["
        // + addr.getAddress().getHostAddress() + "]");
        // }
        // }

        String sessionName = (String) session.getAttribute(KEY);
        sessions.remove(sessionName);

        /*
         * Iterator iterator = sessions.entrySet().iterator();
         * 
         * while (iterator.hasNext()) { Map.Entry entry = (Map.Entry) iterator.next();
         * 
         * if (entry.getValue() == session) { sessions.remove(entry.getKey()); return; } }
         */
    }

    public String getLastSessionName() {
        return lastSessionName;
    }

    public String getFirstSession() {
        if (sessions.entrySet().iterator().hasNext()) {
            return sessions.keySet().iterator().next();
        }
        return null;
    }

    public IoSession getLastSession() {
        return lastSession;
    }

    public void setLastSession(String lastSessionName, IoSession lastSession) {
        this.lastSessionName = lastSessionName;
        this.lastSession = lastSession;
    }

    public int getSize() {
        return sessions.size();
    }
}
