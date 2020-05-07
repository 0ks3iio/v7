package net.zdsoft.eclasscard.data.dao;

import net.zdsoft.eclasscard.data.entity.EccFaceActivate;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EccFaceActivateDao extends BaseJpaRepositoryDao<EccFaceActivate,String>{

	@Query("From EccFaceActivate Where unitId = ?1 and infoId = ?2")
	public EccFaceActivate findByInfoId(String unitId, String infoId);

	@Query("From EccFaceActivate Where unitId = ?1")
	public List<EccFaceActivate> findListByUnitId(String unitId);

	@Modifying
	@Query("update From EccFaceActivate set needLower = 1 Where unitId = ?1 and needLower != 1")
	public void updateNeedLowerUnitId(String unitId);

	@Query("From EccFaceActivate Where needLower = 1")
	public List<EccFaceActivate> findAllNeedLower();
	
	@Modifying
	@Query("delete From EccFaceActivate Where unitId = ?1 and infoId = ?2")
	public void deleteByInfoId(String unitId, String infoId);

}
