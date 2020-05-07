package net.zdsoft.stuwork.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.entity.DyStudentLeave;

public interface DyStudentLeaveService extends BaseService<DyStudentLeave, String>{

	public List<DyStudentLeave> findDyStudentLeaveByStuId(String studentId, Pagination page);
	
	public List<DyStudentLeave> findDyStudentLeaveByTime(Date startTime, Date endTime, String studentId, Pagination page);
	
	public List<DyStudentLeave> findDyStudentLeaveByState(int state, String studentId, Pagination page);
	
	public List<DyStudentLeave> findDyStudentLeaveByStateAndTime(Date startTime, Date endTime, int state, String studentId, Pagination page);
	
	public List<DyStudentLeave> findDyStudentLeaveByStartTime(String studentId, int state);
	
}
