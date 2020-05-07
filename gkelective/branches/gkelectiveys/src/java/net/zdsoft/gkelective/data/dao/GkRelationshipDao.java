package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkRelationship;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkRelationshipDao extends BaseJpaRepositoryDao<GkRelationship,String>{

	@Modifying
	@Query("delete from GkRelationship where primaryId = ?1 and relationshipType = ?2 ")
	public void deleteByPrimaryId(String primaryId,String type);
	
	@Modifying
	@Query("delete from GkRelationship where relationshipType = ?1 and primaryId in (?2)")
	public void deleteByTypePrimaryId(String type, String... primaryId);

	@Query("From GkRelationship where relationshipType = ?1 and primaryId in (?2)")
	public List<GkRelationship> findByTypePrimaryIdIn(String type,
			String[] primaryIds);
	@Modifying
	@Query("delete from GkRelationship where primaryId in (?1)")
	public void deleteByPrimaryIds(String[] primaryId);

}
