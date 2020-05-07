package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.service.EccBulletinService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;

public class BulletinTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(BulletinTask.class);

	public BulletinTask(String bulletinId,boolean isEnd) {
		this.bizId = bulletinId;
		this.isEnd = isEnd;
	}

	@Override
	public void run() {
		EccBulletinService eccBulletinService = Evn.getBean("eccBulletinService");
		logger.info("推送通知公告开始，id="+bizId);
		eccBulletinService.bulletinTaskRun(bizId,isEnd);
		logger.info("推送通知公告结束，id="+bizId);
	}


}
