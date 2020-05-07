package net.zdsoft.appstore.remote;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.zdsoft.appstore.entity.AttendanceRecord;

import org.apache.log4j.Logger;

public class SigninServerLocator {

    // signinRecordQueue存放收到的需要入库的的点到消息。
    private ConcurrentLinkedQueue<AttendanceRecord> signinRecordQueue = null;

    private boolean ipFilter = false; // 是否对同一IP客户端进行过滤

    private int maxSize = 50000;// 默认队列的最大容量

    private static SigninServerLocator instance = new SigninServerLocator();

    private final Logger queueLog = Logger.getLogger("siginsmsrecord");// 用日志文件来保存被移除的点到消息

    public static SigninServerLocator getInstance() {
        return instance;
    }

    public ConcurrentLinkedQueue<AttendanceRecord> getSigninRecordQueue() {
        return signinRecordQueue;
    }

    public void setSigninRecordQueue(ConcurrentLinkedQueue<AttendanceRecord> signinRecordQueue) {
        this.signinRecordQueue = signinRecordQueue;
    }

    public boolean isIpFilter() {
        return ipFilter;
    }

    public void setIpFilter(boolean ipFilter) {
        this.ipFilter = ipFilter;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @SuppressWarnings("unchecked")
    public synchronized void offerQueue(ConcurrentLinkedQueue queue, Object n, byte[] msgBody) {
        if (queue.size() > maxSize - 1) {
            Object o = queue.poll();
            queueLog.debug(o.getClass().getSimpleName() + "\t" + new String(msgBody));
        }
        queue.offer(n);
    }

}
