package net.zdsoft.framework.config;

import org.springframework.stereotype.Component;

import net.zdsoft.framework.utils.UuidUtils;

@Component
public class StaticDataCache {
	private static int sessionCount = 0;
	private static String serverUuid = UuidUtils.generateUuid();
	
	public static int addSessionCount() {
		sessionCount++;
		return sessionCount;
	}
	
	public static String getServerUuid() {
		return serverUuid;
	}

	public int getSessionCount() {
		return sessionCount;
	}
	
	public static int minusSessionCount() {
		sessionCount--;
		return sessionCount;
	}
}
