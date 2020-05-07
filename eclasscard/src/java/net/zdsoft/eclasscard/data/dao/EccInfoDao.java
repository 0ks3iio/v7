package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccInfoDao extends BaseJpaRepositoryDao<EccInfo, String>{

	@Query("From EccInfo Where unitId = ?1 and type is not null and type != '10'")
	public List<EccInfo> findListByNotClass(String unitId);
	
	@Query("From EccInfo Where unitId = ?1 and type in (?2)")
	public List<EccInfo> findListByUnitAndType(String unitId,String... type);
	
	public List<EccInfo> findByPlaceId(String placeId);
	
	public List<EccInfo> findByClassIdIn(String[] classIds);
	
	@Query("From EccInfo Where type in (?1)")
	public List<EccInfo> findListByTypes(String... type);
	
	@Query("From EccInfo Where unitId = ?1 and type is not null order by name")
	public List<EccInfo> findByUnitId(String unitId);
	
	@Query("From EccInfo Where unitId = ?1 and name = ?2")
	public EccInfo findByUnitIdAndName(String unitId, String name);

	@Modifying
	@Query("update from EccInfo set status = ?2 Where id = ?1 and status != ?2 ")
	public void updateOnLineStatus(String id,Integer status);
}
