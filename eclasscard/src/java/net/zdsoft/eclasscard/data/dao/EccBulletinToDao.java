package net.zdsoft.eclasscard.data.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccBulletinTo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccBulletinToDao extends BaseJpaRepositoryDao<EccBulletinTo, String>{

	@Modifying
    @Query("delete from EccBulletinTo Where bulletinId = ?1")
	public void deleteByBulletinId(String bulletinId);

}
