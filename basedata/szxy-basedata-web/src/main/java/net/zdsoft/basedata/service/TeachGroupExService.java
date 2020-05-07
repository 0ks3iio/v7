package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.TeachGroupEx;

import java.util.List;

public interface TeachGroupExService extends BaseService<TeachGroupEx, String>{

	List<TeachGroupEx> findByTeachGroupId(String[] teachGroupIds);

    List<TeachGroupEx> findByTeachGroupIdWithMaster(String[] teachGroupIds);

	/**
	 *	细分业务类型
	 * @param type 1：负责人0：成员
	 * @param teachGroupIds
	 * @return
	 */
	List<TeachGroupEx> findByTypeAndTeachGroupIdIn(Integer type,String[] teachGroupIds);
	/**
	 * 删除老师后删除教研组扩展表中该老师数据
	 * @param teacherIds
	 */
	void deleteByTeacherIds(String... teacherIds);
}
