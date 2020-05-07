package net.zdsoft.basedata.dao;

import org.springframework.data.jpa.repository.Query;
import java.util.List;import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;



import org.springframework.data.jpa.repository.Query;

/**
 * @author shenke
 * @since 2017.07.19
 */
public interface SchoolSemesterDao extends BaseJpaRepositoryDao<SchoolSemester, String> {

	@Query(" From SchoolSemester where acadyear= ?1 and semester= ?2 and schoolId= ?3 and isDeleted=0")
	SchoolSemester findByAcadyearAndSemester(String acadyear ,int semester , String schoolId);
	
	@Query("from SchoolSemester WHERE schoolId=?1 and (to_date(?2,'yyyy-MM-dd')-workBegin ) >=0 AND (to_date(?2,'yyyy-MM-dd') - workEnd ) <=0 AND isDeleted = 0")
	SchoolSemester findByCurrentDay(String schid,String currentDay);
	@Query(" From SchoolSemester where schoolId in ?1 and isDeleted=0 ")
	List<SchoolSemester> findBySchoolIdIn(String[] schoolIds);

}
