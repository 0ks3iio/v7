package net.zdsoft.bigdata.system.dao;

import java.util.List;

import net.zdsoft.bigdata.system.entity.BgUserRole;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BgUserRoleDao extends BaseJpaRepositoryDao<BgUserRole, String> {

	@Query("From BgUserRole where userId=?1 ")
	public List<BgUserRole> findUserRoleListByUserId(String userId);

	@Query("From BgUserRole where roleId=?1 ")
	public List<BgUserRole> findUserRoleListByRoleId(String roleId);

	@Query("From BgUserRole where moduleId=?1 ")
	public List<BgUserRole> findUserRoleListByModuleId(String moduleId);

	public void deleteByRoleId(String roleId);

	public void deleteByUserId(String userId);

	@Query(value = "delete from bg_sys_user_role where role_id= ?1 and user_id = ?2 ", nativeQuery = true)
	@Modifying
	public void deleteByRoleIdAndUserId(String roleId, String userId);

	@Query(value = "delete from bg_sys_user_role where module_id= ?1 and user_id = ?2 ", nativeQuery = true)
	@Modifying
	public void deleteByModuleIdAndUserId(String moduleId, String userId);

}
