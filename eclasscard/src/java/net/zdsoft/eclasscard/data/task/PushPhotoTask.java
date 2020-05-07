package net.zdsoft.eclasscard.data.task;

import net.zdsoft.eclasscard.data.utils.PushPotoUtils;

import org.apache.log4j.Logger;

public class PushPhotoTask extends EccTask{
	private static final Logger logger = Logger
			.getLogger(PushPhotoTask.class);

	public PushPhotoTask(String unitId) {
		this.bizId = unitId;
	}


	@Override
	public void run() {
		logger.info("推送相册开始");
		PushPotoUtils.pushView(bizId);
		logger.info("推送相册结束");
	}


}
