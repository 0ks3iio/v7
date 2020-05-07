package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccFaceSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccFaceSetDao extends BaseJpaRepositoryDao<EccFaceSet, String>{
	
	public List<EccFaceSet> findByUnitIdAndInfoId(String unitId,String infoId);
	
	public List<EccFaceSet> findByUnitIdAndInfoIdIn(String unitId,String[] infoIds);
	
	public List<EccFaceSet> findByUnitId(String unitId);

	@Modifying
	@Query("delete from EccFaceSet where infoId=?1 ")
	public void deleteByInfoId(String infoId);
}
