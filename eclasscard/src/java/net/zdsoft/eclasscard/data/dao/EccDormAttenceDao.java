package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccDormAttence;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EccDormAttenceDao extends BaseJpaRepositoryDao<EccDormAttence, String>{

	@Query("From EccDormAttence Where periodId = ?1 and to_char(clockDate,'yyyy-MM-dd') = ?2")
	public List<EccDormAttence> findDormAttenceByPeriodId(String periodId,
			String date);
	@Query("From EccDormAttence Where placeId = ?1 and isOver = 0 and to_char(clockDate,'yyyy-MM-dd') = ?2 and periodId in (?3)")
	public List<EccDormAttence> findListByPlaceIdNotOver(String placeId,String today,String[] periodIds);
	@Query("From EccDormAttence Where unitId = ?1 and periodId = ?2 and clockDate = ?3 and  placeId= ?4 and periodId in (?5)")
	public List<EccDormAttence> findListByCon(String unitId,String periodId,Date date,String buildingId,String[] periodIds);
	@Query("From EccDormAttence Where unitId = ?1 and clockDate = ?2 and placeId= ?3 and periodId in (?4)")
	public List<EccDormAttence> findListByCon(String unitId,Date date,String buildingId,String[] periodIds);

	@Query("From EccDormAttence Where unitId = ?1 and clockDate <= ?2 ")
	public List<EccDormAttence> findStatByCon(String unitId,Date endTime);
	@Query("From EccDormAttence Where unitId = ?1 and clockDate >= ?2 and clockDate <= ?3 ")
	public List<EccDormAttence> findStatByCon(String unitId,Date startTime,Date endTime);
	@Query("From EccDormAttence Where  clockDate < ?1 and is_over = 0")
	public List<EccDormAttence> findStatByNoOverBefToday(Date today);
	
	@Query("From EccDormAttence Where unitId = ?1 and periodId = ?2 and to_char(clockDate,'yyyy-MM-dd') = ?3 and  placeId in (?4)")
	public List<EccDormAttence> findListByEccNotInit(String unitId,
			String periodId, String date, String[] placeIds);

}
