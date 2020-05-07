package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeacherDuty;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeacherDutyDao extends BaseJpaRepositoryDao<TeacherDuty, String>{

	public static final String SQL_ORDER = "order by teacherId, dutyCode";
	
	@Query("from TeacherDuty where isDeleted = 0 and dutyCode = ?1 and teacherId in (?2)" + SQL_ORDER)
	List<TeacherDuty> findByTeacherIds(String dutyCode, String[] teacherIds);

	@Query("from TeacherDuty where isDeleted = 0 and dutyCode <> '114' and teacherId in (?1)" + SQL_ORDER)
	List<TeacherDuty> findByTeacherIds(String[] teacherIds);

}
