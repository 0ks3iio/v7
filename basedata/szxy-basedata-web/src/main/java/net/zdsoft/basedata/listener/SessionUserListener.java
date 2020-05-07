package net.zdsoft.basedata.listener;

import java.util.Date;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import net.zdsoft.basedata.entity.CountOnlineTime;
import net.zdsoft.basedata.service.CountOnlineTimeService;
import net.zdsoft.framework.config.Evn;

public class SessionUserListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("=================================sessionDestroyed");
		CountOnlineTimeService countOnlineTimeService = Evn.getBean("countOnlineTimeService");
		CountOnlineTime countOnlineTime = countOnlineTimeService.getCountOnlineTimeBySessId(se.getSession().getId());
    	if(null != countOnlineTime && countOnlineTime.getLogoutTime() == null){
			System.out.println("===================================sessionDestroyed===="+countOnlineTime.getSessionId());
    		long a = countOnlineTime.getLoginTime().getTime();
    		long b = new Date().getTime();
    		int c = (int)((b - a) / 1000);
    		countOnlineTime.setLogoutTime(new Date());
    		countOnlineTime.setOnlineTime(c);
    		countOnlineTimeService.save(countOnlineTime);
    	}
	}
}
