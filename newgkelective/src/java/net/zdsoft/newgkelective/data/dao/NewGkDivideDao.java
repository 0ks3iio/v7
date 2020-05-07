package net.zdsoft.newgkelective.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivide;

public interface NewGkDivideDao extends BaseJpaRepositoryDao<NewGkDivide, String>{
	
	@Query("from NewGkDivide where unitId=?1 and gradeId=?2 and openType<>'07' and isDeleted=0 order by isDefault desc, times desc,creationTime desc")
	List<NewGkDivide> findByGradeId(String unitId,String gradeId);

	@Modifying
	@Query("update NewGkDivide set isDeleted=1,modifyTime=?1 where id=?2 ")
	void updateDelete(Date creationTime, String divideId);

	@Query("select max(times) from NewGkDivide where unitId=?1 and gradeId=?2 and openType<>'07' ")
	Integer findMaxByGradeId(String unitId, String gradeId);
	@Modifying
	@Query("update NewGkDivide set stat=?2 where id=?1 ")
	void updateStat(String divideId, String stat);
	@Query("from NewGkDivide where choiceId=?1 and isDeleted=0 order by times desc,creationTime desc")
	List<NewGkDivide> findByChoiceId(String choiceId);
	
	@Query("from NewGkDivide where unitId=?1 and gradeId=?2 and stat=?3 and openType<>'07' and isDeleted=0 order by times desc,creationTime desc")
	List<NewGkDivide> findByGradeIdAndStat(String unitId, String gradeId,
			String stat);
	
	@Query("from NewGkDivide where unitId=?1 and openType=?2 and gradeId in (?3) and isDeleted=0 order by gradeId,times desc,creationTime desc")
	List<NewGkDivide> findByOpenTypeAndGradeIdIn(String unitId, String openType, String[] gradeIds);
	@Query("from NewGkDivide where unitId=?1 and openType=?2 and isDeleted=0 order by gradeId,times desc,creationTime desc")
	List<NewGkDivide> findByOpenType(String unitId, String openType);

	@Query("select max(times) from NewGkDivide where unitId=?1 and gradeId=?2 and openType='07' and isDeleted=0 ")
	Integer findMaxByGradeIdXzb(String unitId, String gradeId);

    // Basedata Sync Method
	@Modifying
    @Query("update NewGkDivide set isDeleted=1, modifyTime=?1 where gradeId in(?2)")
    void deleteByGradeIds(Date date, String... gradeIds);
}
