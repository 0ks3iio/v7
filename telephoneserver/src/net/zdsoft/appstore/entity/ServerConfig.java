package net.zdsoft.appstore.entity;

import net.zdsoft.appstore.remote.SessionManager;

/**
 * 服务配置类
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2014-6-19 下午03:53:19 $
 */
public class ServerConfig {

    private String name;
    private int port;

    private SessionManager sessionManager;

    public ServerConfig(String name, int port) {
        this.name = name;
        this.port = port;
        sessionManager = new SessionManager();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

}
