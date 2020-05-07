package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeachPlanEx;

public interface TeachPlanExService extends BaseService<TeachPlanEx, String> {

	public void deleteByTeacherIdAndPrimaryTableIdIn(String teacherId, String[] primaryTableIds);

	public List<TeachPlanEx> findByPrimaryTableIdIn(String[] primaryTableIds);

	public void deleteByTeacherIdInAndPrimaryTableIdIn(String[] teacherIds, String[] primaryTableIds);

}
