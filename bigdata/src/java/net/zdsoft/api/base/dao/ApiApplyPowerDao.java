package net.zdsoft.api.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiApplyPower;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2018年7月12日上午9:18:10
 */
public interface ApiApplyPowerDao extends BaseJpaRepositoryDao<ApiApplyPower, String> {

	/**
	 * @param developerId
	 * @return
	 */
	@Query("from ApiApplyPower where developerId = ?1")
	List<ApiApplyPower> findByDeveloperId(String developerId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	@Query("from ApiApplyPower where developerId = ?1 and unitId in ?2")
	List<ApiApplyPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	@Modifying
    @Query("delete from ApiApplyPower where developerId = ?1 and unitId in ?2")
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	@Query("from ApiApplyPower where ticketKey = ?1 and (interfaceId = ?2 or type = ?3)")
	List<ApiApplyPower> findByTicketKeyAndInterfaceIdOrType(
			String ticketKey, String interfaceId, String type);

}
