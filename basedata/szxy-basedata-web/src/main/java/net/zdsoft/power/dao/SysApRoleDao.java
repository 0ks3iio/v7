package net.zdsoft.power.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.entity.SysApRole;

/**
 * @author yangsj  2018年6月7日下午2:31:50
 */
public interface SysApRoleDao extends BaseJpaRepositoryDao<SysApRole, String> {

	@Modifying
    @Query("delete from SysApRole where roleId = ?1")
	void deleteByRoleId(String roleId);

	@Query("From SysApRole where serverId = ?1")
	List<SysApRole> findByServerId(Integer serverId);

	@Query("From SysApRole where serverId in ?1")
	List<SysApRole> findByServerIdIn(Integer[] serverIds);

	@Query("From SysApRole where roleId = ?1")
	SysApRole findByRoleId(String roleId);

	@Query("From SysApRole where serverId = ?1 and unitId = ?2")
	List<SysApRole> findByServerIdAndUnitId(Integer serverId, String unitId);

	@Query("From SysApRole where serverId in ?1 and unitId = ?2")
	List<SysApRole> findByServerIdInAndUnitId(Integer[] serverIds, String unitId);

}
