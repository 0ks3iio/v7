package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public interface NewGkDivideClassDao extends BaseJpaRepositoryDao<NewGkDivideClass, String>{
	
    @Query("From NewGkDivideClass dc where divideId =?1 and sourceType = ?2  and not exists(From NewGkDivideClass where id=dc.id and classType=2 and"
    		+ " batch is null) order by classType,subjectType,orderId,className")
    List<NewGkDivideClass> findByDivideIdAndSourceTypeOrderByClassName(String divideId,String sourceType);

    List<NewGkDivideClass> findByDivideIdAndClassTypeAndSubjectIds(String divideId, String classType, String subjectIds);

    List<NewGkDivideClass> findByParentIdIn(String[] parentIds);
    
    @Query("select t1 From NewGkDivideClass as t1,NewGkDivide as t2 where t2.gradeId=?1 and t2.isDeleted=0 and t2.id = t1.divideId and t1.sourceType =?2 and t1.parentId is null")
    List<NewGkDivideClass> findByGradeId(String gradeId,String sourceType);
    
    
	
	@Query("select t1.id,t1.className From NewGkTimetable t2,NewGkDivideClass t1 where t2.arrayId=?1 and t2.classId =t1.id ")
	List<Object[]> findByArrayIdMap(String arrayId);
	
	NewGkDivideClass findByDivideIdAndClassNameLike(String divide, String className);
	
	@Query("From NewGkDivideClass where divideId =?1 and sourceType = ?2  and classType= ?3 and subjectIds =?4 and parentId is null order by orderId,className")
	List<NewGkDivideClass> findClassBySubjectIds(String divideId,
			String sourceType, String classType, String subjectIds);
	
	List<NewGkDivideClass> findByDivideIdIn(String[] divideIds);

	@Query("From NewGkDivideClass where divideId=?1 and sourceType=?2 and classType=?3 and subjectIds=?4 and subjectType=?5  and batch=?6 and parentId is null")
	List<NewGkDivideClass> findBySameBatchClassList(String divideId, String sourceType,String classType, String subjectIds, String subjectType, 
			String batch);
	
	/**
	 * 查询各组合班以及行政班数量
	 * @param divideIds
	 * @param classTypes
	 * @return
	 */
	@Query("select count(d.id), d.divideId, d.classType,d.subjectType from NewGkDivideClass d where d.divideId in (?1) and d.classType in (?2) and d.parentId is null group by d.divideId, d.classType,d.subjectType ")
	List<Object[]> findCountByDivideIdAndClassType(String[] divideIds,String[] classTypes);
	/**
	 * 查询各科目教学班数量
	 * @param divideIds
	 * @return
	 */
	@Query("select count(d.id), d.divideId, d.subjectIds,d.subjectType,d.bestType from NewGkDivideClass d, NewGkDivide dv "
			+ "where d.divideId in (?1) and d.divideId = dv.id and dv.stat=1 and class_type=2 and d.parentId is null group by  d.divideId, d.subjectIds,d.subjectType,d.bestType ")
	List<Object[]> findCountByDivideIdAndSubjectType(String[] divideIds);

	@Query("From NewGkDivideClass where divideId =?1 and sourceType = ?2  and classType= ?3 and subjectIds =?4 and subjectType=?5 and parentId is null")
	List<NewGkDivideClass> findClassBySubjectIdsAndType(String divideId, String sourceType, String classType,
			String subjectId, String subjectType);

	@Query("From NewGkDivideClass where divideId =?1 and sourceType = ?2  and classType= ?3 and relateId in (?4) and parentId is null")
	List<NewGkDivideClass> findListByRelateId(String divideId, String sourceType,String classType, String[] relateIds);

	@Query("select tt.classId from NewGkTimetable tt,NewGkTimetableOther tto where tt.arrayId = ?1 and tt.classType in (?5) and tt.id = tto.timetableId"
			+ " and tto.dayOfWeek = ?2 and tto.periodInterval = ?3 and tto.period = ?4")
	List<String> findByClassTypeAndTime(String arrayId, Integer dayOfWeek, String periodInterval,
			Integer period, String[] classTypes);

    // Basedata Sync Method
    void deleteBySubjectIdsLike(String s);

    // Basedata Sync Method
    void deleteByOldClassIdIn(String... classIds);
    
    void deleteByIdIn(String... classIds);

    void deleteByParentIdIn(String[] parentIds);

    @Query("select count(id) from NewGkDivideClass where  divideId =?1 and classType= ?2 and  sourceType = ?3 ")
    Object countByDivideId(String divideId, String classType, String classSourceType);

    @Query("From NewGkDivideClass where divideId =?1 and sourceType = ?2  and classType= ?3 and relateId like ?4 ")
	List<NewGkDivideClass> findListLikeRelateId(String divideId,String sourceType,String classType,String id);

	/**
	 * id -> oldDivideClassId
	 * @param arrayId
	 * @return
	 */
	@Query("select id, oldDivideClassId from NewGkDivideClass where divide_id=?1 and old_divide_class_id is not null")
	List<Object[]> findOldDivideClassIdMap(String arrayId);
}
