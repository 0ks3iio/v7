package net.zdsoft.power.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.entity.SysApPower;

/**
 * @author yangsj  2018年6月7日下午2:31:00
 */
public interface SysApPowerDao extends BaseJpaRepositoryDao<SysApPower, String> {

	/**
	 * @param powerId
	 * @param serverId
	 */
	@Modifying
    @Query("delete from SysApPower where powerId = ?1 and serverId = ?2")
	void deleteBypowerIdAndServerId(String powerId, Integer serverId);

	/**
	 * @param serverId
	 * @return
	 */
	@Query("From SysApPower where serverId = ?1 ")
	List<SysApPower> findByServerId(Integer serverId);

	/**
	 * @param serverIds
	 * @return
	 */
	@Query("From SysApPower where serverId in ?1 ")
	List<SysApPower> findByServerIdIn(Integer[] serverIds);

	/**
	 * @param powerId
	 */
	@Modifying
    @Query("delete from SysApPower where powerId = ?1 ")
	void deleteBypowerId(String powerId);

	/**
	 * @param powerId
	 * @return
	 */
	@Query("From SysApPower where powerId = ?1 ")
	SysApPower findByPowerId(String powerId);

	/**
	 * @param serverIds
	 * @param unitId
	 * @return
	 */
	@Query("From SysApPower where serverId in ?1 and unitId = ?2 ")
	List<SysApPower> findByServerIdInAndUnitId(Integer[] serverIds, String unitId);

	/**
	 * @param serverId
	 * @param unitId
	 * @return
	 */
	@Query("From SysApPower where serverId = ?1 and unitId = ?2 ")
	List<SysApPower> findByServerIdAndUnitId(Integer serverId, String unitId);

}
