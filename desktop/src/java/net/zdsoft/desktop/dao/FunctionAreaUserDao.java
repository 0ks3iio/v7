package net.zdsoft.desktop.dao;

import java.util.List;

import net.zdsoft.basedata.constant.BaseConstants;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.desktop.entity.FunctionAreaUser;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FunctionAreaUserDao extends BaseJpaRepositoryDao<FunctionAreaUser, String>{
	
	@Query("From FunctionAreaUser where state = " + FunctionAreaUser.STATE_NORMAL_STRING + " and  customerId in ?1 order by displayOrder")
	List<FunctionAreaUser> findByCustomerIds(String... customerIds);

	@Query("From FunctionAreaUser where  customerId = ?1  order by displayOrder")
	List<FunctionAreaUser> findByUserIdAndNotZeroId(String userId);

	/**
	 * @param userId
	 * @param functionId
	 * @return
	 */
	@Query("From FunctionAreaUser where  customerId = ?1 and functionAreaId in (?2) order by displayOrder")
	FunctionAreaUser findOwnerByFunctionAreaIds(String userId, List<String> functionId);

	/**
	 * @return
	 */
	@Query("select max(f.displayOrder) from FunctionAreaUser as f where state = 1 and customerId in (?1) ")
	Integer getMaxDisplayOrder(String ... customerId);

	/**
	 * @param order
	 * @param id
	 */
	@Modifying
	@Query("update FunctionAreaUser set displayOrder= ?1 ,state = 1  where id = ?2")
	void updateDisplayOrderAndState(int order, String id);

	/**
	 * @param functionId
	 * @param id
	 */
	@Modifying
	@Query("update FunctionAreaUser set functionAreaId= ?1 where id = ?2")
	void updateFunctionAreaId(String functionId, String id);

    @Query("FROM FunctionAreaUser WHERE displayOrder > ?1 and state='" + FunctionAreaUser.STATE_NORMAL_STRING + "'")
    List<FunctionAreaUser> findDisplayOrderMoreThan(Integer displayOrder);

	/**
	 * @param userId
	 * @param funId
	 * @return
	 */
    @Query("From FunctionAreaUser where  customerId = ?1 and functionAreaId = ?2 order by displayOrder")
	FunctionAreaUser findOwnerByFunctionAreaId(String userId, String funId);
}
