package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccFullObj;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EccFullObjDao extends BaseJpaRepositoryDao<EccFullObj,String>{

	@Query("From EccFullObj Where eccInfoId = ?1 and beginTime <= ?2 and endTime > ?2 order by beginTime desc,createTime desc")
	public List<EccFullObj> findByBetweenTime(String eccInfoId,String nowTime);
	
	@Query("From EccFullObj Where objectId in (?1)")
	public List<EccFullObj> findByObjectId(String... objectId);
	
	@Modifying
    @Query("delete from EccFullObj Where objectId in (?1)")
	public void deleteByObjectIds(String... objectIds);

	@Modifying
	@Query("delete from EccFullObj Where sourceId in (?1)")
	public void deleteBySourceIds(String[] sourceId);

	@Query("From EccFullObj Where sourceId in (?1)")
	public List<EccFullObj> findBySourceIds(String[] sourceIds);

	@Query("From EccFullObj Where status != 2")
	public List<EccFullObj> findListNotShow();

}
