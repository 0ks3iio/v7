package net.zdsoft.officework.dao;


import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.officework.entity.OfficeHealthCount;

import org.springframework.data.jpa.repository.Query;
/**
 * office_health_count
 * @author 
 * 
 */
public interface OfficeHealthCountDao extends BaseJpaRepositoryDao<OfficeHealthCount, String>{
	
	/**
	 * 根据studentIds获取list
	 * @param studentIds
	 * @return
	 */
	@Query("select s1 from OfficeHealthCount as s1  Where s1.ownerId in ?1")
	List<OfficeHealthCount> findByOwnerIds(String... ownerIds);
	
	/**
	 * 根据studentIds获取list
	 * @param checkDate (string  yyyy-MM-dd)
	 * @param studentIds
	 * @return
	 */
	@Query(value="select * from office_health_count where to_char(check_date, 'yyyy-MM-dd') in ?1 and owner_id in ?2", nativeQuery=true)
	List<OfficeHealthCount> findByDateOwnerIds(String[] checkDates, String[] ownerIds);
	
	@Query(value="select * from office_health_count where type=?1 and check_date  between to_date(?2,'yyyy-mm-dd') and to_date(?3,'yyyy-mm-dd') and owner_id in ?4 order by check_date", nativeQuery=true)
	List<OfficeHealthCount> getOfficeHealthCountByOwnerId(Integer type,String beforeDateStr, String nowDateStr,String[] ownerIds);

	@Query(value="select * from office_health_count where type=?1 and check_date = to_date(?2,'yyyy-mm-dd') and owner_id in ?3 order by hour" , nativeQuery=true)
	List<OfficeHealthCount> getOfficeHealthCountByOwnerId(Integer type, String date,String[] ownerIds);

	@Query(value="select ohc.* from office_health_count ohc ,(select max(check_date) check_date ,owner_id from office_health_count where type = 1 and owner_id in (?1) group by owner_id) cd where  type = 1 and ohc.check_date = cd.check_date and ohc.owner_id = cd.owner_id" , nativeQuery=true)
	List<OfficeHealthCount> findByLastDayData(String[] ownerIds);

	@Query(value="select * from office_health_count where to_char(check_date, 'yyyy-MM-dd') in ?1 and owner_id in ?2 and type = ?3", nativeQuery=true)
	List<OfficeHealthCount> findByTypeDateOwnerIds(String[] checkDates,
			String[] ownerIds, int type);
}