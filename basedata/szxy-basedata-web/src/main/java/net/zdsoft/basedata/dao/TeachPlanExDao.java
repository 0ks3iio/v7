package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;

import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachPlanExDao extends BaseJpaRepositoryDao<TeachPlanEx, String> {

	@Modifying
	public void deleteByTeacherIdAndPrimaryTableIdIn(String teacherId, String[] primaryTableIds);

	public List<TeachPlanEx> findByPrimaryTableIdIn(String[] primaryTableIds);

	public void deleteByPrimaryTableIdIn(String[] primaryTableIds);

	@Modifying
	public void deleteByTeacherIdInAndPrimaryTableIdIn(String[] teacherIds, String[] primaryTableIds);
	
}
