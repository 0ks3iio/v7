package net.zdsoft.basedata.dingding.action;

import net.zdsoft.basedata.dingding.service.DingDingSyncDataService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@Component
@Lazy(false)
public class DingDingSyncdataJob {
	Logger log =Logger.getLogger(DingDingSyncdataJob.class);
	@Autowired
	DingDingSyncDataService dingDingSyncDataService;

	@Scheduled(cron = "0 0/30 * * * ?")
	// 每隔30分钟执行一次
	public void run() {
		if (Evn.isScheduler()) {
			if (Evn.getBoolean("dingding.scheduler.start")) {
				// 获取30分钟之内更新的数据并同步到钉钉
				dingDingSyncDataService.dealData2DingDingByUpdateTime();
			}
		}

	}
}
