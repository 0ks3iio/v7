package net.zdsoft.openapi.pushjob.service;

import java.util.List;

import net.zdsoft.base.entity.eis.OpenApiPushJob;
import net.zdsoft.basedata.service.BaseService;

public interface BasePushJobService extends BaseService<OpenApiPushJob, String> {
	/**
	 * 查找数据库中所有要跑的任务
	 * @return
	 */
	public List<OpenApiPushJob> getBasePushJobAll();
}
