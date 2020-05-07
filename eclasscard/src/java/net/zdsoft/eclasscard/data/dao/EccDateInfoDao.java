package net.zdsoft.eclasscard.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccDateInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EccDateInfoDao extends BaseJpaRepositoryDao<EccDateInfo, String>{
	
//	@Query("From EclasscardDateInfo where schId = ?1 and grade = ?2 and acadyear = ?3 and semester = ?4 order by week,weekDay")
//	public List<EccDateInfo> getDateList(String schoolId,String grade,String acadyear,
//			Integer semester);
//
	@Query("From EccDateInfo Where gradeId = ?1 and to_date(?2,'YYYY/mm/DD') = infoDate")
	public EccDateInfo getByDateGrade(String gradeId,String date);
	@Modifying
    @Query("delete from EccDateInfo Where id in (?1)")
	public void deleteDateInfo(String[] ids);
	
	@Query("From EccDateInfo Where schoolId = ?1 and to_date(?2,'YYYY/mm/DD') = infoDate")
	public List<EccDateInfo> getDateSchoolId(String unitId, String date);
	@Query("select infoDate From EccDateInfo Where schoolId = ?1 and gradeId = ?2 and to_date(?3,'YYYY/mm/DD') <= infoDate and to_date(?4,'YYYY/mm/DD') >= infoDate")
	public List<Date> getInfoDateList(String unitId, String gradeId,
			String bDate, String eDate);

}
