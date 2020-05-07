package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;

public interface NewGkTeacherGroupExService extends BaseService<NewGkTeacherGroupEx, String> {
	
	List<NewGkTeacherGroupEx> findByTeacherGroupIdIn(String[] teaherGroupIds);

	void deleteByTeacherGroupIds(String[] strings);

	/**
	 * 根据教师 删除 教师组数据
	 * @param gradeId
	 * @param tids
	 */
	void deleteByTeacherIds(String gradeId, List<String> tids);

	List<NewGkTeacherGroupEx> findByGradeIdAndTid(String gradeId, String[] array);
}
