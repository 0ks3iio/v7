package net.zdsoft.api.openapi.pushjob.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiPushJob;
import net.zdsoft.basedata.service.BaseService;

public interface ApiPushJobService extends BaseService<ApiPushJob, String> {
	/**
	 * 查找数据库中所有要跑的任务
	 * @return
	 */
	public List<ApiPushJob> getBasePushJobAll();
}
