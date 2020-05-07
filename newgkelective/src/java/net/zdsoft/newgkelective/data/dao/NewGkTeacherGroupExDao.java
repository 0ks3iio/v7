package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;

public interface NewGkTeacherGroupExDao extends BaseJpaRepositoryDao<NewGkTeacherGroupEx, String> {
	
	List<NewGkTeacherGroupEx> findByTeacherGroupIdIn(String[] teacherGroupId);

	void deleteByTeacherGroupIdIn(String[] tgIds);

	@Modifying
	@Query("delete from NewGkTeacherGroupEx where id in( select tge.id from NewGkTeacherGroupEx tge, NewGkTeacherGroup tg where tge.teacherGroupId = tg.id"
			+ " and tg.objectId=?1 and tge.teacherId in (?2) )")
	void deleteByTeacherIds(String gradeId, String[] tids);

	@Query("select tge from NewGkTeacherGroupEx tge, NewGkTeacherGroup tg where tge.teacherGroupId = tg.id"
			+ " and tg.objectId=?1 and tge.teacherId in (?2)")
	List<NewGkTeacherGroupEx> findByGradeIdAndTid(String gradeId, String[] tids);
}
