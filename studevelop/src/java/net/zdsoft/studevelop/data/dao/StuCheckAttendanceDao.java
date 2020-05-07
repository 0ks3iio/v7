package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;

public interface StuCheckAttendanceDao extends BaseJpaRepositoryDao<StuCheckAttendance,String>{

	@Query("From StuCheckAttendance where acadyear = ?1 and semester = ?2 and studentId in (?3)")
	public List<StuCheckAttendance> findListByCls(String acadyear, String semester,
			String[] array);

	@Query("From StuCheckAttendance where acadyear = ?1 and semester = ?2 and studentId = ?3")
	public StuCheckAttendance findBystudentId(String acadyear, String semester,
			String stuId);
	@Modifying
	@Query("Delete From StuCheckAttendance where acadyear = ?1 and semester = ?2 and studentId in (?3)")
	public Integer deleteByStuIds(String acadyear, String semester,String[] studentIds);
}
