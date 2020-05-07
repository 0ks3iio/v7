package net.zdsoft.stuwork.data.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.dao.DyStudentLeaveDao;
import net.zdsoft.stuwork.data.entity.DyStudentLeave;
import net.zdsoft.stuwork.data.service.DyStudentLeaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("dyStudentLeaveService")
public class DyStudentLeaveServiceImpl extends BaseServiceImpl<DyStudentLeave, String> implements DyStudentLeaveService{
    @Autowired
	private DyStudentLeaveDao dyStudentLeaveDao;
	
	@Override
	public List<DyStudentLeave> findDyStudentLeaveByStuId(String studentId, Pagination page) {
		List<DyStudentLeave> dyStudentLeaveList;
		if(null!=page){
			Pageable pageable = Pagination.toPageable(page);
			dyStudentLeaveList = dyStudentLeaveDao.findDyStudentLeaveByStuId(studentId, pageable);
			List<DyStudentLeave> dyStudentLeaveList2 = dyStudentLeaveDao.findDyStudentLeaveByStuId(studentId, null);
			page.setMaxRowCount(dyStudentLeaveList2.size());
		}else{
			dyStudentLeaveList = dyStudentLeaveDao.findDyStudentLeaveByStuId(studentId, null);
		}		
		return dyStudentLeaveList;
	}

	@Override
	public List<DyStudentLeave> findDyStudentLeaveByTime(Date startTime,
			Date endTime, String studentId, Pagination page) {
		Pageable pageable = Pagination.toPageable(page);
		List<DyStudentLeave> dyStudentLeaveList = dyStudentLeaveDao.findDyStudentLeaveByTime(startTime, endTime, studentId, pageable);
		List<DyStudentLeave> dyStudentLeaveList2 = dyStudentLeaveDao.findDyStudentLeaveByTime(startTime, endTime, studentId, null);
		page.setMaxRowCount(dyStudentLeaveList2.size());
		return dyStudentLeaveList;
	}

	@Override
	public List<DyStudentLeave> findDyStudentLeaveByState(int state,
			String studentId, Pagination page) {
		Pageable pageable = Pagination.toPageable(page);
		List<DyStudentLeave> dyStudentLeaveList = dyStudentLeaveDao.findDyStudentLeaveByState(state, studentId, pageable);
		List<DyStudentLeave> dyStudentLeaveList2 = dyStudentLeaveDao.findDyStudentLeaveByState(state, studentId, null);
		page.setMaxRowCount(dyStudentLeaveList2.size());
		return dyStudentLeaveList;
	}

	@Override
	public List<DyStudentLeave> findDyStudentLeaveByStateAndTime(
			Date startTime, Date endTime, int state, String studentId, Pagination page) {
		Pageable pageable = Pagination.toPageable(page);
		List<DyStudentLeave> dyStudentLeaveList = dyStudentLeaveDao.findDyStudentLeaveByStateAndTime(startTime, endTime, state, studentId, pageable);
		List<DyStudentLeave> dyStudentLeaveList2 = dyStudentLeaveDao.findDyStudentLeaveByStateAndTime(startTime, endTime, state, studentId, null);
		page.setMaxRowCount(dyStudentLeaveList2.size());
		return dyStudentLeaveList;
	}

	@Override
	protected BaseJpaRepositoryDao<DyStudentLeave, String> getJpaDao() {
		return dyStudentLeaveDao;
	}

	@Override
	protected Class<DyStudentLeave> getEntityClass() {
		return DyStudentLeave.class;
	}

	@Override
	public List<DyStudentLeave> findDyStudentLeaveByStartTime(String studentId,
			int state) {
		return dyStudentLeaveDao.findDyStudentLeaveByStartTime(studentId, state);
	}

}
