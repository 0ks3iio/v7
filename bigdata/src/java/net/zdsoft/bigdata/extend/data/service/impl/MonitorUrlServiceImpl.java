package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.MonitorUrlDao;
import net.zdsoft.bigdata.extend.data.entity.MonitorUrl;
import net.zdsoft.bigdata.extend.data.service.MonitorUrlService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
/**
 * 系统监控url 
 * @author jiangf
 *
 */
@Service("monitorUrlService")
public class MonitorUrlServiceImpl extends BaseServiceImpl<MonitorUrl, String>
		implements MonitorUrlService {

	@Autowired
	private MonitorUrlDao monitorUrlDao;

	@Override
	protected BaseJpaRepositoryDao<MonitorUrl, String> getJpaDao() {
		return monitorUrlDao;
	}

	@Override
	protected Class<MonitorUrl> getEntityClass() {
		return MonitorUrl.class;
	}

	@Override
	public List<MonitorUrl> getMonitorList() {
		return monitorUrlDao.getMonitorList();
	}

}
