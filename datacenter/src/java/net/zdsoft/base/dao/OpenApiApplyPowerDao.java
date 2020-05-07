package net.zdsoft.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.base.entity.eis.OpenApiApplyPower;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2018年7月12日上午9:18:10
 */
public interface OpenApiApplyPowerDao extends BaseJpaRepositoryDao<OpenApiApplyPower, String> {

	/**
	 * @param developerId
	 * @return
	 */
	@Query("from OpenApiApplyPower where developerId = ?1")
	List<OpenApiApplyPower> findByDeveloperId(String developerId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	@Query("from OpenApiApplyPower where developerId = ?1 and unitId in ?2")
	List<OpenApiApplyPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	@Modifying
    @Query("delete from OpenApiApplyPower where developerId = ?1 and unitId in ?2")
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	@Query("from OpenApiApplyPower where ticketKey = ?1 and (interfaceId = ?2 or type = ?3)")
	List<OpenApiApplyPower> findByTicketKeyAndInterfaceIdOrType(
			String ticketKey, String interfaceId, String type);

}
