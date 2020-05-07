package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.EventIndicator;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventIndicatorDao extends BaseJpaRepositoryDao<EventIndicator, String>{

	@Query("From EventIndicator where eventId = ?1 order by orderId ")
	public List<EventIndicator> findIndicatorListByEventId(String eventId);

	List<EventIndicator> findAllByUnitIdOrderByOrderId(String unitId);
}
