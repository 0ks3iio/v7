package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccClassAttenceDao extends BaseJpaRepositoryDao<EccClassAttence, String> ,EccClassAttenceJdbcDao{

	@Query("From EccClassAttence Where isOver = 0 and unitId = ?1 and periodInterval = ?2 and period = ?3 and to_char(clockDate,'yyyy-MM-dd') = ?4")
	public List<EccClassAttence> findListByPeriodNotOver(String unitId,String periodInterval,int period,String date);
	@Query("From EccClassAttence Where clockDate < ?1 and isOver = 0")
	public List<EccClassAttence> findListNotOver(Date today );
	@Query("From EccClassAttence Where classId = ?1 and sectionNumber = ?2 and to_char(clockDate,'yyyy-MM-dd') = ?3 and unitId = ?4")
	public EccClassAttence findListByClassIdNotOver(String classId,int sectionNumber,String date,String unitId);
	@Query("From EccClassAttence Where placeId = ?1 and sectionNumber = ?2 and to_char(clockDate,'yyyy-MM-dd') = ?3 and unitId = ?4")
	public EccClassAttence findListByPlaceIdNotOver(String placeId,int sectionNumber,String date,String unitId);
	@Query("From EccClassAttence Where teacherId = ?1 and  to_char(clockDate,'yyyy-MM-dd') = ?2 and unitId = ?3 order by sectionNumber")
	public List<EccClassAttence> findListByTeacherId(String teacherId,String date,String unitId);
	@Query("From EccClassAttence Where to_char(clockDate,'yyyy-MM-dd') = ?1 and sectionNumber=?2 and unitId = ?3")
	public List<EccClassAttence> findBySectionClass(String date,
			int section ,String unitId);
	@Query("From EccClassAttence Where to_char(clockDate,'yyyy-MM-dd') = ?1 and unitId = ?2")
	public List<EccClassAttence> findByUnitIdThisDate(String date,String unitId);
//	@Query("From EccClassAttence Where id in (?1) order by clockDate desc,sectionNumber")
//	public List<EccClassAttence> findByIdsSort(String[] ids);
	@Query("From EccClassAttence Where id in (?1)")
	public List<EccClassAttence> findByIdsIsOver(String[] ids);
	
	@Query("From EccClassAttence Where unitId = ?1 and placeId = ?2 and sectionNumber = ?3 and to_char(clockDate,'yyyy-MM-dd') = ?4")
	public EccClassAttence findbyPlaceIdSecNumAndDay(String unitId,
			String placeId, Integer sectionNumber, String toDay);
	@Query("From EccClassAttence Where unitId = ?1 and classId = ?2 and sectionNumber = ?3 and to_char(clockDate,'yyyy-MM-dd') = ?4")
	public EccClassAttence findbyClassIdSecNumAndDay(String unitId,
			String classId, Integer sectionNumber, String toDay);
	@Query("From EccClassAttence Where section = ?1 and sectionNumber = ?2 and to_char(clockDate,'yyyy-MM-dd') = ?3 and unitId = ?4")
	public List<EccClassAttence> findListBySecNotOver(String section,int sectionNumber,String date,String unitId);

}
