package net.zdsoft.desktop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.desktop.entity.UserSubscribe;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2017-6-12下午6:18:04
 */
public interface UserSubscribeDao extends BaseJpaRepositoryDao<UserSubscribe, String>{
    
	String SQL_AFTER=" and status= 1 ";
	/**
	 * @param userId
	 * @return
	 */
	@Query("From UserSubscribe  Where userId = ?1 "+SQL_AFTER)
	List<UserSubscribe> findByUserId(String userId);
	/**
	 * @param serverId
	 */
	@Query("From UserSubscribe  Where serverId = ?1 and userId = ?2 ")
	UserSubscribe findByUserServerId(Integer serverId,String userId);
	/**
	 * @param id
	 * @return
	 */
	@Query("From UserSubscribe  Where serverId = ?1 "+SQL_AFTER)
	List<UserSubscribe> findByServerId(Integer id);

	
}
