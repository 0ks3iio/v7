package net.zdsoft.basedata.service;

import net.zdsoft.basedata.dto.TeachGroupDto;
import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.entity.TeachGroupEx;

import java.util.List;

public interface TeachGroupService extends BaseService<TeachGroup, String>{

	List<TeachGroup> findBySchoolId(String unitId);

	List<TeachGroup> findBySchoolIdWithMaster(String unitId);
	
	List<TeachGroup> findBySchoolId(String unitId,boolean isMakeTeacher);

	/**
	 * 调用此方法的 可以切换下面方法 findBySchoolId
	 * @param unitId 必填
	 * @param type 1：负责人 0成员 空：全部人员
	 * @return
	 */
	List<TeachGroup> findBySchoolIdAndType(String unitId,Integer type);

	void deleteAndSave(String teachGroupId, TeachGroup teachGroup,
			List<TeachGroupEx> teachGroupExList);

	void delete(String[] teachGroupIds);

	void save(List<TeachGroup> saveTeachGroupList,
			List<TeachGroupEx> saveTeachGroupExList);

	List<TeachGroup> findBySchoolIdAndSubjectIdIn(String unitId,String[] subids);
	 /**
	  * 教研组教师数据
	  * @param unitId
	  * @return
	  */
	 public List<TeachGroupDto> findTeachers(String unitId);

	 public Integer findMaxOrder(String unitId);
	 /**
	  * 全校教师数据 如果不属于任何教研组 就放入未分配教研组(teachGroupId默认32个0)
	  * @param unitId
	  * @param isSortFirst 教研组下教师是否按照姓名首字母排序
	  * @return teachGroupId teachGroupName mainTeacherList(teacherId,teacherName)
	  */
	 public List<TeachGroupDto> findAllTeacherGroup(String unitId,boolean isSortFirst);

	 /**
	  * 删除课程后清空教研组courseId
	  * @param subjectIds
	  */
	void deleteBySubjectIds(String... subjectIds);
	 
	 
}
