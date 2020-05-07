package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeachClassEx;

public interface TeachClassExService extends BaseService<TeachClassEx, String>{
	/**
	 * 
	 * @param teachClassIds
	 * @param isMake 是否组装额外字段
	 * @return
	 */
	List<TeachClassEx> findByTeachClassIdIn(String[] teachClassIds,boolean isMake);

	void deleteByTeachClass(String[] teachClassId);

	List<TeachClassEx> findByTeachClassId(String teachClassId);

	/**
	 * 删除年级后删除年级下教学班扩展表相关数据
	 * @param gradeIds
	 */
	void deleteByGradeIds(String... gradeIds);

	/**
	 * 删除课程后删除课程相关教学班扩展表数据
	 * @param subjectIds
	 */
	void deleteBySubjectIds(String... subjectIds);
}
