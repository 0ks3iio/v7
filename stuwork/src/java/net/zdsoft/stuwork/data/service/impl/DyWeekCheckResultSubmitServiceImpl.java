package net.zdsoft.stuwork.data.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.dao.DyWeekCheckResultSubmitDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;
import net.zdsoft.stuwork.data.service.DyWeekCheckResultSubmitService;

@Service("dyWeekCheckResultSubmitService")
public class DyWeekCheckResultSubmitServiceImpl extends BaseServiceImpl<DyWeekCheckResultSubmit, String> implements DyWeekCheckResultSubmitService{
	
	@Autowired
	private DyWeekCheckResultSubmitDao dyWeekCheckResultSubmitDao;
	
	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckResultSubmit, String> getJpaDao() {
		return dyWeekCheckResultSubmitDao;
	}

	@Override
	protected Class<DyWeekCheckResultSubmit> getEntityClass() {
		return DyWeekCheckResultSubmit.class;
	}

	@Override
	public void saveSub(DyWeekCheckResultSubmit sub) {
		dyWeekCheckResultSubmitDao.deleteByCheckDateAndRoleType(sub.getSchoolId(),sub.getAcadyear(),sub.getSemester(),sub.getRoleType(),sub.getCheckDate());
		checkSave(sub);
		save(sub);
	}
	@Override
	public List<DyWeekCheckResultSubmit> findByWeek(String unitId,
			String acadyear, String semester, int week) {
		return dyWeekCheckResultSubmitDao.findByWeek(unitId, acadyear, semester, week);
	}
	@Override
	public DyWeekCheckResultSubmit findByRoleTypeAndCheckDate(String schoolId,
			String acadyear, String semester, String roleType, Date checkDate) {
		return dyWeekCheckResultSubmitDao.findByRoleTypeAndCheckDate(schoolId, acadyear, semester, roleType, checkDate);
	}
	
	@Override
	public List<DyWeekCheckResultSubmit> findByRoleType(String schoolId, String acadyear, String semester, String roleType) {
		return dyWeekCheckResultSubmitDao.findByRoleType(schoolId, acadyear, semester, roleType);
	}
	
}
