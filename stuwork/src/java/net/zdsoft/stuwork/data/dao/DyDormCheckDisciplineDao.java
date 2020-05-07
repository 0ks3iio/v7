package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyDormCheckDiscipline;

public interface DyDormCheckDisciplineDao extends BaseJpaRepositoryDao<DyDormCheckDiscipline, String>{
	@Query(" FROM DyDormCheckDiscipline WHERE school_id= ?1 and acadyear =?2 and semester= ?3 and class_id in (?4)")
	public List<DyDormCheckDiscipline> getPoolByCon(String unitId,String acadyear,String semesterStr,String[] classIds);
	@Query(" FROM DyDormCheckDiscipline WHERE school_id= ?1 and acadyear =?2 and semester= ?3 and student_id= ?4 and class_id in (?5)")
	public List<DyDormCheckDiscipline> getPoolByConOne(String unitId,String acadyear,String semesterStr,String studentId,String[] classIds);
	@Query(" FROM DyDormCheckDiscipline WHERE school_id= ?1 and acadyear =?2 and semester= ?3 and building_id= ?4 and class_id in (?5)")
	public List<DyDormCheckDiscipline> getPoolByConTwo(String unitId,String acadyear,String semesterStr,String buildingId,String[] classIds);
	@Query(" FROM DyDormCheckDiscipline WHERE school_id= ?1 and acadyear =?2 and semester= ?3 and building_id= ?4 and student_id= ?5 and class_id in (?6)")
	public List<DyDormCheckDiscipline> getPoolByConThree(String unitId,String acadyear,String semesterStr,String buildingId,String studentId,String[] classIds);

	@Query(" FROM DyDormCheckDiscipline WHERE school_id= ?1 and acadyear =?2 and semester= ?3  and student_id= ?4 ")
	public List<DyDormCheckDiscipline> getDetailByCon(String unitId,String acadyear,String semesterStr,String studentId);
}
