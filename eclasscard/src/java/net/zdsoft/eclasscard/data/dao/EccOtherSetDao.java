package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccOtherSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface EccOtherSetDao extends BaseJpaRepositoryDao<EccOtherSet,String>{

	@Modifying
	@Query("update EccOtherSet set nowvalue = ?1 Where unitId = ?2 and type = ?3")
	public void updateOtherSet(Integer nowvalue,String unitId,Integer type);

	public EccOtherSet findByUnitIdAndType(String unitId, Integer type);
	
	@Query("From EccOtherSet where nowvalue = ?1 and type = ?2")
	public List<EccOtherSet> findByOpenAndType(Integer nowvalue, Integer type);

	@Query("From EccOtherSet where unitId = ?1")
	public List<EccOtherSet> findListByUnitId(String unitId);

//	@Query("From EccOtherSet where type like '%'||?1||'%'")
//	public List<EccOtherSet> findListByType(Integer type);
}
