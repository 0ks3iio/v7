package net.zdsoft.appstore.remote;

/**
 * 
 * @author zhangxm
 * @version $Revision: 1.0 $, $Date: 2015-7-28 下午9:24:33 $
 */
public abstract interface AbstractServer {

    public abstract void setIdleTime(int idleTime);

    public abstract void startService() throws Exception;

}
