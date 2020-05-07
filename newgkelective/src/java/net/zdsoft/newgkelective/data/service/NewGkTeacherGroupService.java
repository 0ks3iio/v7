package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroup;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;

public interface NewGkTeacherGroupService extends BaseService<NewGkTeacherGroup, String> {

	List<NewGkTeacherGroup> findByObjectId(String objectId, boolean makeTeachers);

	/**
	 * 添加 或 修改 教师组
	 * @param teacherGroupList
	 * @param tgExList
	 */
	void saveOrUpdate(List<NewGkTeacherGroup> teacherGroupList, List<NewGkTeacherGroupEx> tgExList);

	/**
	 * 教师组 初始化
	 * @param gradeId
	 */
    void dealTeacherGroupInit(String gradeId);

    /**
	 * 批量 删除 教师组
	 * @param teacherGroupIds
	 */
	void deleteGroups(String gradeId, String[] teacherGroupIds);

	/**
	 * 删除 教师组老师 时 删除对应的 老师的 教师组 禁排时间
	 * @param tgTidMap
	 * @param teaLtList
	 * @param tgLtList
	 */
	void deleteRelaTeacherTime(Map<String, List<String>> tgTidMap, List<NewGkLessonTime> teaLtList,
			List<NewGkLessonTime> tgLtList);

}
