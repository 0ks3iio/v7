package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleStudent;

public interface DyWeekCheckRoleStudentService extends BaseService<DyWeekCheckRoleStudent, String> {

	public void deleteByContion(String schoolId, String acadyear, String semester, int week, String section);

	public void saveList(List<DyWeekCheckRoleStudent> roleStuList);

	public List<DyWeekCheckRoleStudent> findByRoleIds(String... roleIds);

	public List<DyWeekCheckRoleStudent> findByStuId(String unitId, String acadyear, String semester, String stuId);

	public void deleteByRoleId(String... roleIds);

}
