package net.zdsoft.eclasscard.data.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccPermission;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccPermissionDao extends BaseJpaRepositoryDao<EccPermission, String>{

	@Modifying
    @Query("delete from EccPermission Where eccName = ?1 and unit_id = ?2")
	public void deleteByEccName(String eccName,String unitId);
}
