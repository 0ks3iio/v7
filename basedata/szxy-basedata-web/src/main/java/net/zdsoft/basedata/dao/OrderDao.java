package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Order;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface OrderDao extends BaseJpaRepositoryDao<Order, String>{
	
	public static final String SQL_CUSTOMERID = "SELECT * FROM base_order "
            + "WHERE customer_id= ?1 AND customer_type= ?2 AND state=1 AND is_deleted=0 "
            + "AND (trunc(start_time,'DD') <= to_date(?3,'yyyy-mm-dd') AND trunc(end_time,'DD') >=to_date(?4,'yyyy-mm-dd'))";
	
	@Query("from Order where isDeleted = 0 and serverId = ?1 and customerId in (?2)")
	List<Order> findByServer(Integer serverId, String[] customerIds);
	
	@Query(nativeQuery=true, value=SQL_CUSTOMERID)
	List<Order> findByCustomerId(String customerId, Integer customerType,
			String formattedTime, String formattedTime2);

}
