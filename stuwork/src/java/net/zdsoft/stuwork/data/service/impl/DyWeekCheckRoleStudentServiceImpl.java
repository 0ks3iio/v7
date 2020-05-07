package net.zdsoft.stuwork.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.dao.DyWeekCheckRoleStudentDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleStudent;
import net.zdsoft.stuwork.data.service.DyWeekCheckRoleStudentService;

@Service("dyWeekCheckRoleStudentService")
public class DyWeekCheckRoleStudentServiceImpl extends BaseServiceImpl<DyWeekCheckRoleStudent, String>  implements DyWeekCheckRoleStudentService{
	@Autowired
	private DyWeekCheckRoleStudentDao dyWeekCheckRoleStudentDao;
	
	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckRoleStudent, String> getJpaDao() {
		return dyWeekCheckRoleStudentDao;
	}

	@Override
	protected Class<DyWeekCheckRoleStudent> getEntityClass() {
		return DyWeekCheckRoleStudent.class;
	}

	@Override
	public void deleteByContion(String schoolId, String acadyear, String semester, int week, String section) {
		dyWeekCheckRoleStudentDao.deleteByContion(schoolId,acadyear,semester,week,section);
	}
	@Override
	public void deleteByRoleId(String... roleIds) {
		dyWeekCheckRoleStudentDao.deleteByRoleId(roleIds);
	}
	
	@Override
	public void saveList(List<DyWeekCheckRoleStudent> roleStuList) {
		DyWeekCheckRoleStudent[] rs = roleStuList.toArray(new DyWeekCheckRoleStudent[]{});
		checkSave(rs);
		saveAll(rs);
	}
	
	@Override
	public List<DyWeekCheckRoleStudent> findByRoleIds(String... roleIds) {
		return dyWeekCheckRoleStudentDao.findByRoleIds(roleIds);
	}
	
	@Override
	public List<DyWeekCheckRoleStudent> findByStuId(String unitId, String acadyear, String semester, String stuId) {
		return dyWeekCheckRoleStudentDao.findByStuId(unitId,acadyear,semester,stuId);
	}

}
