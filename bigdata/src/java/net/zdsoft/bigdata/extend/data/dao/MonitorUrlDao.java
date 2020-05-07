package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.MonitorUrl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * 监控url DAO
 * 
 * @author jiangf
 *
 */
public interface MonitorUrlDao extends BaseJpaRepositoryDao<MonitorUrl, String> {

	@Query("FROM MonitorUrl order by orderId ")
	List<MonitorUrl> getMonitorList();
}
