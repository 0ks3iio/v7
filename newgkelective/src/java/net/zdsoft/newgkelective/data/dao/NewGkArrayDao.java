package net.zdsoft.newgkelective.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkArray;

public interface NewGkArrayDao extends BaseJpaRepositoryDao<NewGkArray, String>{

	@Query("From NewGkArray where divideId=?1 and isDeleted=0")
	List<NewGkArray> findByDivideId(String divideId);
	@Query("from NewGkArray where unitId=?1 and gradeId=?2 and isDeleted=0 order by isDefault desc,times desc,creationTime desc")
	List<NewGkArray> findByGradeId(String unitId, String gradeId);
	
	@Query("from NewGkArray where unitId=?1 and gradeId=?2 and isDeleted=0 order by times desc,creationTime desc")
	List<NewGkArray> findByGradeId(String unitId, String gradeId, Pageable page);
	
	@Query("from NewGkArray where unitId=?1 and gradeId=?2 and stat=?3 and isDeleted=0 order by times desc,creationTime desc")
	List<NewGkArray> findByGradeId(String unitId, String gradeId,String stat);
	
	@Query("from NewGkArray where unitId=?1 and gradeId=?2 and stat=?3 and isDeleted=0 order by times desc,creationTime desc")
	List<NewGkArray> findByGradeId(String unitId, String gradeId,String stat, Pageable page);

	@Query("select max(times) from NewGkArray where unitId=?1 and gradeId=?2 and arrangeType=?3 ")
	Integer findMaxByGradeIdArrangeType(String unitId, String gradeId,String arrangeType);
	@Modifying
	@Query("update NewGkArray set stat=?1 where id=?2 ")
	void updateStatById(String stat, String id);
	@Modifying
	@Query("update NewGkArray set isDeleted=1,modifyTime=?1 where id=?2 ")
	void deleteById(Date currentDate,String id);
	@Query("from NewGkArray where id = ?1")
	NewGkArray findbyId(String arrayId);
	
	@Query("from NewGkArray where unitId=?1 and divideId in (?2) and isDeleted=0 order by times desc,creationTime desc")
	List<NewGkArray> findByUnitIdAndDivideIdIn(String unitId, String[] divideIds);
	
	@Query("select max(times) from NewGkArray where unitId=?1 and arrangeType=?2 ")
	Integer findMaxByArrangeType(String unitId, String arrangeType);

    // Basedata Sync Method
	@Modifying
    @Query("update NewGkArray set isDeleted=1, modifyTime=?1 where gradeId in(?2)")
    void deleteByGradeIds(Date currentDate, String... gradeIds);
}
