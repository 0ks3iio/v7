package net.zdsoft.syncdata.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;



/**
 * @author yangsj  2018年3月13日上午10:46:03
 */
@Lazy(false)
@Component
public class ThreadPool {
	
	
	private volatile static ExecutorService singleThreadPool;
	private final static Object lockObj = new Object();
	
	public static ExecutorService getSingleThreadPool() {
		if(singleThreadPool == null) {
			synchronized (lockObj) {
				if(singleThreadPool == null) {
					singleThreadPool = Executors.newSingleThreadExecutor();
				}
//				((ThreadPoolExecutor)fixedThreadPool).setRejectedExecutionHandler(new RejectedExecutionHandler() {
//					
//					@Override
//					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//						//do nothing
//					}
//				});
			}
		}
		return singleThreadPool;
	}

	public static void setFixedThreadPool(ExecutorService singleThreadPool) {
		ThreadPool.singleThreadPool = singleThreadPool;
	}

	public  static ExecutorService getThreadPool(){
        return getSingleThreadPool();
    }
	
	
	
}
