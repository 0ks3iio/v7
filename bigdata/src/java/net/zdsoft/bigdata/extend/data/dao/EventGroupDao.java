package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.EventGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EventGroupDao extends BaseJpaRepositoryDao<EventGroup, String>{

	@Query("From EventGroup where userId = ?1 order by orderId ")
	public List<EventGroup> findGroupListByUserId(String userId);
	
	@Query("From EventGroup where userId = ?1 and groupName = ?2  ")
	public List<EventGroup> findGroupListByGroupName(String userId,String groupName);
	
	 @Query("select max(orderId) from EventGroup where userId = ?1")
	 public Integer getMaxOrderIdByUserId(String userId);
}
