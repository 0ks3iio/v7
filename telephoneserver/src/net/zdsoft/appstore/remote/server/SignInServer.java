package net.zdsoft.appstore.remote.server;

import net.zdsoft.appstore.remote.ServerConfig;

import org.apache.log4j.Logger;
import org.apache.mina.common.TransportType;
import org.apache.mina.io.filter.IoThreadPoolFilter;
import org.apache.mina.registry.Service;
import org.apache.mina.registry.ServiceRegistry;
import org.apache.mina.registry.SimpleServiceRegistry;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:34:25 $
 */
public class SignInServer implements AbstractServer {

    private int idleTime = 130;
    private ServerConfig serverConfig = null;
    private static Logger logger = Logger.getLogger(SignInServer.class);

    public SignInServer(String name, int serverPort) {
        serverConfig = new ServerConfig(name, serverPort);
    }

    public int getSessionManagerSize() {
        return serverConfig.getSessionManager().getSize();
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public void startService() throws Exception {
        ServiceRegistry registry = new SimpleServiceRegistry();

        IoThreadPoolFilter threadPoolFilter = (IoThreadPoolFilter) registry.getIoAcceptor(TransportType.SOCKET)
                .getFilterChain().getChild("threadPool");

        threadPoolFilter.setMaximumPoolSize(25);

        Service service = new Service("WirelessServer", TransportType.SOCKET, serverConfig.getPort());
        SignInHandler signInHandler = new SignInHandler(serverConfig);
        signInHandler.setIdleTime(idleTime);
        registry.bind(service, signInHandler);
        if (logger.isInfoEnabled()) {
            logger.info("Zdsoft Listening on port " + serverConfig.getPort());
            logger.info("启动远距离进出校考勤服务......");
        }
    }
}
