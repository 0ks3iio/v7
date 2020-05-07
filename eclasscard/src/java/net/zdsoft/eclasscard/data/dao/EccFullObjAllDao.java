package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccFullObjAll;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccFullObjAllDao extends BaseJpaRepositoryDao<EccFullObjAll,String>{

	@Query("From EccFullObjAll Where objectId in (?1)")
	public List<EccFullObjAll> findByObjectId(String... objectId);
	
	@Modifying
    @Query("delete from EccFullObjAll Where objectId in (?1)")
	public void deleteByObjectIds(String... objectId);
	
	@Query("From EccFullObjAll Where status != 2")
	public List<EccFullObjAll> findListNotShow();


}
