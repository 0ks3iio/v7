package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.DeveloperPower;

/**
 * @author yangsj  2018年7月12日上午9:18:10
 */
public interface DeveloperPowerDao extends BaseJpaRepositoryDao<DeveloperPower, String> {

	/**
	 * @param developerId
	 * @return
	 */
	@Query("from DeveloperPower where developerId = ?1")
	List<DeveloperPower> findByDeveloperId(String developerId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	@Query("from DeveloperPower where developerId = ?1 and unitId in ?2")
	List<DeveloperPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	@Modifying
    @Query("delete from DeveloperPower where developerId = ?1 and unitId in ?2")
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

}
