package net.zdsoft.power.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.entity.SysPower;

/**
 * @author yangsj  2018年6月7日下午2:33:01
 */
public interface SysPowerDao extends BaseJpaRepositoryDao<SysPower, String> {

	String SQL_AFTER=" and isActive= 1";
	/**
	 * @param source
	 * @return
	 */
	@Query("From SysPower  Where source = ?1 ")
	List<SysPower> findBySource(int source);
	
	@Query("From SysPower  Where value = ?1 and id in ?2 ")
	SysPower findByValueAndIdIn(String value, String[] ids);
	
	@Query("From SysPower  Where value = ?1 and source = ?2 ")
	SysPower findByValueAndSource(String value, int defaultSourceValue);

	/**
	 * @param source
	 * @param unitId
	 * @return
	 */
	@Query("From SysPower  Where source = ?1 and unitId = ?2")
	List<SysPower> findBySourceAndUnitId(int source, String unitId);

}
