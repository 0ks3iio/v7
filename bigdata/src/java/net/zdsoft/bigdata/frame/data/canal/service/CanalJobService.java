package net.zdsoft.bigdata.frame.data.canal.service;

import java.math.BigDecimal;
import java.util.Date;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.frame.data.canal.entity.CanalJob;

public interface CanalJobService extends BaseService<CanalJob, String>{

	/**
	 * 更新提交时间和数量
	 * @param jobId
	 * @param submitCount
	 * @param lastCommitDate
	 */
	public void updateJobById(String jobId,BigDecimal submitCount,Date lastCommitDate);
}
