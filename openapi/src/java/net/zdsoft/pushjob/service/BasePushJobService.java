package net.zdsoft.pushjob.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.pushjob.entity.BasePushJob;

public interface BasePushJobService extends BaseService<BasePushJob, String> {
	/**
	 * 查找数据库中所有要跑的任务
	 * @return
	 */
	public List<BasePushJob> getBasePushJobAll();
}
