package net.zdsoft.power.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.entity.SysUserPower;

/**
 * @author yangsj  2018年6月7日下午2:33:37
 */
public interface SysUserPowerDao extends BaseJpaRepositoryDao<SysUserPower, String> {

	/**
	 * @param targetId
	 * @param type
	 * @return
	 */
	@Query("From SysUserPower  Where targetId = ?1 and type = ?2 ")
	List<SysUserPower> findByTargetIdAndType(String targetId, int type);

	/**
	 * @param targetId
	 * @param PowerIds
	 * @param type
	 */
	@Modifying
    @Query("delete from SysUserPower where targetId = ?1 and powerId in ?2 and type = ?3")
	void deleteByTargetIdAndPowerIdInAndType(String targetId, String[] pids, int type);

	/**
	 * @param type
	 * @param targetIds
	 * @return
	 */
	@Query("From SysUserPower  Where type = ?1 and targetId in ?2 ")
	List<SysUserPower> findByTypeAndTargetIdIn(int type, String[] targetIds);

	/**
	 * @param targetId
	 * @param type
	 */
	@Modifying
    @Query("delete from SysUserPower where targetId = ?1 and  type = ?2")
	void deleteByTargetIdAndType(String targetId, int type);

}
