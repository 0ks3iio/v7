package net.zdsoft.appstore.remote;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:24:23 $
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
