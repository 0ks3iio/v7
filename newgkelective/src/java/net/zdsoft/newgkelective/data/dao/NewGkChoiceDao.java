package net.zdsoft.newgkelective.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NewGkChoiceDao extends BaseJpaRepositoryDao<NewGkChoice, String>{

	@Query("From NewGkChoice where gradeId =?1 and isDeleted=0  order by isDefault desc, creationTime desc")
	List<NewGkChoice> findByGradeId(String gradeId);
	
	@Query("From NewGkChoice where gradeId =?1 and isDeleted=0 and isDefault=1")
	List<NewGkChoice> findDefaultByGradeId(String gradeId);
	
	@Query("From NewGkChoice where gradeId =?1 and isDeleted=0 and startTime<=?2 and endTime>=?2")
	NewGkChoice findByGradeIdAndNowTimeBetween(String gradeId,Date nowTime);
	
	@Query("select max(times) from NewGkChoice where unitId=?1 and gradeId=?2 ")
	Integer getChoiceMaxTime(String unitId, String gradeId);

	@Query("From NewGkChoice where gradeId =?1 and isDeleted=0" )
	List<NewGkChoice> findListByGradeId(String gradeId);
	
	@Modifying
	@Query("update NewGkChoice set isDeleted = 1,modifyTime=?1 where id = ?2")
	void deleteById(Date cuTime, String id);

	@Query("From NewGkChoice where gradeId =?1 and isDeleted=0 and  startTime<=?2 and endTime>=?2")
	List<NewGkChoice> findListByGradeIdAndNowTimeBetween(String gradeId,Date nowTime);

    // Basedata Sync Method
	@Modifying
    @Query("update NewGkChoice set isDeleted=1, modifyTime=?1 where gradeId in(?2)")
    void deleteByGradeIds(Date currentDate, String... gradeIds);
}
