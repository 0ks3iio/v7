package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;

public interface NewGkTimetableDao extends BaseJpaRepositoryDao<NewGkTimetable, String>{

	List<NewGkTimetable> findByUnitIdAndArrayId(String unitId, String arrayId);
	
	@Query("From NewGkTimetable where unitId=?1 and subjectId =?2 and classId =?3")
	NewGkTimetable findbySubIdAndClaId(String unitId, String subjectId,String classId);
	
	@Query("From NewGkTimetable where unitId=?1 and arrayId =?2 and classType in (?3)")
	List<NewGkTimetable> findByArrayIdAndClassType(String unitId, String arrayId,
			String[] classTypes);

	List<NewGkTimetable> findByUnitIdAndArrayIdAndClassId(String unitId, String arrayId, String classId);

	List<NewGkTimetable> findByUnitIdAndArrayIdAndClassTypeInAndSubjectIdIn(String unitId, String arrayId,String[] classTypes,
			String[] subjectIds);

	List<NewGkTimetable> findByUnitIdAndArrayIdAndClassIdIn(String unitId, String arrayId,
			String[] classIds);

	List<NewGkTimetable> findByUnitIdAndArrayIdAndIdIn(String unitId, String arrayId, String[] array);
	
	List<NewGkTimetable> findByUnitIdAndArrayIdAndSubjectIdIn(String unitId, String arrayId,
			String[] subjectId);

    // Basedata Sync Method
    void deleteByClassIdIn(String... classIds);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subjectIds);

    @Query("select distinct t2.id From NewGkTimetableOther as t1, NewGkTimetable t2 where"
    		+ " t2.unitId=?1 and t2.arrayId=?2 and t2.id = t1.timetableId and t1.placeId in (?3) ")
	List<String> findIdByPlaceId(String unitId, String arrayId, String[] placeIds);
}
