package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.Adjusted;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AdjustedDao extends BaseJpaRepositoryDao<Adjusted, String>{

    @Query("FROM Adjusted where acadyear = ?1 and semester = ?2 and operator = ?3 and weekOfWorktime = ?4 order by creationTime desc")
    List<Adjusted> findListByTeacherIdAndWeek(String acadyear, Integer semester, String teacherId, Integer week);

    @Modifying
    @Query("delete from Adjusted where id in (?1)")
    void deleteByAdjustedIds(String[] adjustedIds);

    @Query("FROM Adjusted where acadyear = ?1 and semester = ?2 and schoolId = ?3 and weekOfWorktime = ?4 order by creationTime desc")
    List<Adjusted> findListBySchoolIdAndWeek(String acadyear, Integer semester, String unitId, Integer week);

    @Modifying
    @Query("update Adjusted set state=?2, modifyTime=?3 where id = ?1")
    void updateStateById(String adjustedId, String state, Date modifyTime);

    @Query("FROM Adjusted where acadyear = ?1 and semester = ?2 and operator = ?3 order by creationTime desc")
	List<Adjusted> findListByTeacherId(String acadyear, Integer valueOf, String teacherId);
    
    @Query("FROM Adjusted where acadyear = ?1 and semester = ?2 and schoolId = ?3 order by creationTime desc")
    List<Adjusted> findListBySchoolId(String acadyear, Integer semester, String unitId);

    @Modifying
    @Query("delete from Adjusted where id in (select adjustedId from AdjustedDetail where classId=?1)")
	void deleteByClassId(String classId);
    
    @Query("select a.id FROM Adjusted a, AdjustedDetail d where a.acadyear = ?1 and a.semester = ?2 and a.schoolId = ?3 and a.weekOfWorktime in (?4)"
    		+ "and a.id = d.adjustedId and d.classId =?5")
	List<String> findByClassAndWeeks(String acadyear, Integer semester, String schoolId, Integer[] weeks,
			String classId);
}
