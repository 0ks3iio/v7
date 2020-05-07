package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableOther;

import org.springframework.data.jpa.repository.Query;

public interface NewGkTimetableOtherDao extends BaseJpaRepositoryDao<NewGkTimetableOther, String>{

	@Query("select t1 From NewGkTimetableOther as t1, NewGkTimetable t2 where t1.unitId=?1 and t2.arrayId=?2 and t2.id = t1.timetableId order by t1.dayOfWeek,t1.periodInterval,t1.period ")
	List<NewGkTimetableOther> findListByUnitIdAndArrayId(String unitId, String arrayId);
	
	@Query("select t1 From NewGkTimetableOther as t1, NewGkTimetable t2 where t1.unitId=?1 and t2.arrayId=?2 and t2.id = t1.timetableId and t1.placeId =?3 ")
	List<NewGkTimetableOther> findByUnitIdAndArrayIdAndPlaceId(String unitId,String arrayId,String placeId);
	/**
	 * 根据timetableids查询每周每天每个教室有几节课
	 * @param timetableIds
	 * @return
	 */
	@Query("select tto.placeId,tto.dayOfWeek||'-'||tto.periodInterval ||'-'||tto.period,count(tto.id) from NewGkTimetableOther tto,"
			+ " NewGkTimetable t2 where tto.unitId=?1 and t2.arrayId=?2 and t2.id = tto.timetableId  group by tto.placeId,tto.dayOfWeek||'-'||tto.periodInterval ||'-'||tto.period")
	List<Object[]> seachClassroomUseage(String unitId, String arrayId);
	
	List<NewGkTimetableOther> findByUnitIdAndTimetableIdIn(String unitId, String[] timetableIds);
	
	void deleteByIdIn(String[] timetableOtherIds);
	
	@Query("select distinct tto.placeId from NewGkTimetable tt, NewGkTimetableOther tto where tt.id = tto.timetableId and tt.unitId=?1 "
			+ "and tt.arrayId=?2 and tto.placeId is not null")
	List<String> findPlaceIds(String unitId, String arrayId);
}
