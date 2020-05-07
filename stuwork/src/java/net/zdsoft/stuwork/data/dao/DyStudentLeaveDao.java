package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStudentLeave;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface DyStudentLeaveDao extends BaseJpaRepositoryDao<DyStudentLeave,String>{
	@Query("From DyStudentLeave where studentId = ?1 order by startTime desc")
    public List<DyStudentLeave> findDyStudentLeaveByStuId(String studentId, Pageable page);
	@Query("From DyStudentLeave where to_date(to_char(startTime,'yyyy-MM-dd'),'yyyy-MM-dd') >=to_date(to_char(?1,'yyyy-MM-dd'),'yyyy-MM-dd') and to_date(to_char(startTime,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date(to_char(?2,'yyyy-MM-dd'),'yyyy-MM-dd') and studentId = ?3 order by startTime desc")
	public List<DyStudentLeave> findDyStudentLeaveByTime(Date startTime, Date endTime, String studentId, Pageable page);
	@Query("From DyStudentLeave where state = ?1 and studentId = ?2 order by startTime desc")
	public List<DyStudentLeave> findDyStudentLeaveByState(int state, String studentId, Pageable page);
	@Query("From DyStudentLeave where to_date(to_char(startTime,'yyyy-MM-dd'),'yyyy-MM-dd') >=to_date(to_char(?1,'yyyy-MM-dd'),'yyyy-MM-dd') and to_date(to_char(startTime,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date(to_char(?2,'yyyy-MM-dd'),'yyyy-MM-dd') and state = ?3 and studentId = ?4 order by startTime desc")
	public List<DyStudentLeave> findDyStudentLeaveByStateAndTime(Date startTime, Date endTime, int state, String studentId, Pageable page);
	@Query("From DyStudentLeave where studentId = ?1 and state = ?2 order by startTime desc")
	public List<DyStudentLeave> findDyStudentLeaveByStartTime(String studentId, int state);
}
