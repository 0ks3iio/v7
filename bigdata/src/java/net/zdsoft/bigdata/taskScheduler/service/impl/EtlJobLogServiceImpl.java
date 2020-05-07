package net.zdsoft.bigdata.taskScheduler.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.taskScheduler.dao.EtlJobLogDao;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author feekang
 */

@Service("etlJobLogService")
public class EtlJobLogServiceImpl extends BaseServiceImpl<EtlJobLog, String>
		implements EtlJobLogService {

	@Autowired
	private EtlJobLogDao etlJobLogDao;

	@Override
	protected BaseJpaRepositoryDao<EtlJobLog, String> getJpaDao() {
		return etlJobLogDao;
	}

	@Override
	protected Class<EtlJobLog> getEntityClass() {
		return EtlJobLog.class;
	}

	@Override
	public List<EtlJobLog> findByUnitId(String unitId, Pagination page,
			String name, Date beginDate, Date endDate) {
		name = StringUtils.isNotBlank(name) ? "%" + name + "%" : "%%";
		List<EtlJobLog> etlLogs = etlJobLogDao.findByUnitId(unitId, name,
				beginDate, endDate, Pagination.toPageable(page));
		Integer count = countByUnitId(unitId, name, beginDate, endDate);
		page.setMaxRowCount(count);
		return etlLogs;
	}

	@Override
	public List<EtlJobLog> findByJobId(String jobId, Pagination page) {
		return etlJobLogDao.findByJobId(jobId, Pagination.toPageable(page));
	}

	private Integer countByUnitId(String unitId, String name, Date beginDate,
			Date endDate) {
		Long count = etlJobLogDao.countByUnitId(unitId, name, beginDate,
				endDate);
		return count == null ? 0 : count.intValue();
	}
}
