package net.zdsoft.eclasscard.data.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccUserFace;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccUserFaceDao extends BaseJpaRepositoryDao<EccUserFace, String>{
	@Query("From EccUserFace Where ownerId in (?1) ")
	public List<EccUserFace> findByOwnerIds(String[] ownerIds);
	
	@Query("select count(*) from EccUserFace where unitId = ?1")
    Integer countByUnitId(String unitId);

	@Modifying
	@Query("delete From EccUserFace Where ownerId in (?1) and unitId = ?2")
	public void deleteByOwnerIds(String[] array, String unitId);

	@Query("select distinct unitId from EccUserFace")
	public Set<String> findUseFaceUnitIds();
	
}
