package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.TeachGroupEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeachGroupExDao extends BaseJpaRepositoryDao<TeachGroupEx, String>{

	List<TeachGroupEx> findByTeachGroupIdIn(String[] teachGroupIds);

	List<TeachGroupEx> findByTypeAndTeachGroupIdIn(Integer type,String[] teachGroupIds);
	@Modifying
	@Query("delete from TeachGroupEx where teachGroupId = ?1")
	void deleteByTeacherGroupId(String teachGroupId);
	
	void deleteByTeacherIdIn(String... teacherIds);

}
