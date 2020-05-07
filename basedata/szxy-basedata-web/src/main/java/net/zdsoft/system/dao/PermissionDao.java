package net.zdsoft.system.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.Permission;

public interface PermissionDao extends BaseJpaRepositoryDao<Permission, String> {

	@Modifying
	@Query("delete from Permission where id in (?1)")
	void deleteAllByIds(String... id);
	
}
