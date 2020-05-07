package net.zdsoft.background.common;

import java.util.List;

import org.apache.log4j.Logger;

public class ShutdownHook extends Thread{
	private static Logger logger = Logger.getLogger(ShutdownHook.class);

	public void run() {
		List<AbstractService> serviceList = ServiceManager.getInstance().getServiceList();
		for (int i = serviceList.size() - 1; i >= 0; i--) {
			AbstractService cycleService = (AbstractService) serviceList.get(i);
			logger.fatal("Stopping service: " + cycleService.getName());
			try {
				cycleService.stop();
			} catch (Exception e) {
				logger.error("Stop service error", e);
			}
		}
		logger.fatal("All services stopped");
	}
}
