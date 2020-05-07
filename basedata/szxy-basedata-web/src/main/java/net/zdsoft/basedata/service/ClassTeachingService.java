package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.OpenTeachingSearchDto;
import net.zdsoft.basedata.entity.ClassTeaching;

public interface ClassTeachingService extends BaseService<ClassTeaching, String> {


    /**
     * 多个班级的任课信息
     * 
     * @param acadyear
     * @param semester
     * @param classIds
     *            (不能为空)
     * @return
     */
    public List<ClassTeaching> findBySearchForList(String acadyear, String semester, String[] classIds);


    public Map<String, Map<String, ClassTeaching>> findByCourseIdsMap(String unitId, String acadyear, String semester,
            String... subjectIds);
    /**
     * 
     * @param acadyear
     * @param semester
     * @param classIds
     * @return map<subid,map<classid,ent>>
     */
    public Map<String, Map<String, ClassTeaching>> findBySearchMap(String unitId,String acadyear,String semester,String[] classIds);
    /**
     * 
     * @param subjectId
     * @param acadyear
     * @param semester
     * @param classIds
     * @return
     */
    public List<ClassTeaching> findBySearchForList(String subjectId, String acadyear, String semester, String[] classIds);

    /**
     * 获取教师所教的所有课程
     * @param unitId
     * @param teaherIds
     * @return
     */
	public List<ClassTeaching> findClassTeachingListByTeacherId(String unitId, String... teaherIds);

	/**
	 * 获取教师所教行政班课程班级信息
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param teaherIds
	 * @return
	 */
	public List<ClassTeaching> findClassTeachingList(String unitId, String acadyear, String semester, String... teaherIds);

	public List<ClassTeaching> findClassTeachingList(String acadyear, String semester, String[] classId, String unitId, Integer isDeleted,String[] subjectIds,boolean flag);
	public List<ClassTeaching> findClassTeachingListWithMaster(String acadyear, String semester, String[] classId, String unitId, Integer isDeleted,String[] subjectIds,boolean flag);
	/**
	 * 
	 * @param classTeachDto
	 * @param makeEx 组装 助教老师id
	 * @return
	 */
	public List<ClassTeaching> findBySearch(OpenTeachingSearchDto classTeachDto, Boolean makeEx);
	public List<ClassTeaching> findBySearchWithMaster(OpenTeachingSearchDto openTeachingSearchDto, Boolean b);

	/**
	 * 根据科目类型查询
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param subjectType
	 * @return
	 */
	public List<ClassTeaching> findByUnitIdAndAcadyearAndSemesterAndSubjectType(String unitId, String acadyear, String semester, String subjectType);
	/**
	 * 返回该年级下行政班某科目任课主老师
	 * @param acadyear not null
	 * @param semester not null
	 * @param unitId not null
	 * @param gradeId
	 * @param subjectId
	 * @return
	 */
	public List<String> findBySubidTeacherList(String acadyear,
			String semester, String unitId,String gradeId, String subjectId);

	/**
	 * 软删除教学计划数据
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classIds
	 */
	public void deleteCurrentClassIds(String unitId, String acadyear, String semester, String[] classIds);
	/**
	 * 删除相关信息表 同步计划 课程表
	 * @param ids
	 */
	public void deleteClassTeachCouSch(String[] ids);
	/**
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param gradeId
	 * @return
	 */
	public List<ClassTeaching> findListByGradeId(String acadyear,String semester, String unitId,String gradeId);

	/**
	 * 删除老师后清空相应课程开设任课老师teacherId
	 * @param teacherIds
	 */
	public void deleteByTeacherIds(String... teacherIds);

	/**
	 * 删除班级后删除相应课程开设计划
	 * @param classIds
	 */
	public void deleteByClassIds(String... classIds);

	/**
	 * 删除课程后删除相应课程开设计划
	 * @param subjectIds
	 */
	public void deleteBySubjectIds(String... subjectIds);


	public String deleteAndSave(String acadyear, String semester, String classId, String unitId, String[] delSubjectIds, List<ClassTeaching> newClassTeachingList, List<ClassTeaching> delClassTeachingList);

	/**
	 * 根据班级教学计划 获取 某年级 每个班应该上几节课；不包含虚拟课程 和 以教学班上课的课程
	 * @param unitId
	 * @param gradeIds
	 * @param acadyear
	 * @param semester
	 * @param weekIndex
	 * @return
	 */
	Map<String, Integer> getClassTeachingHourMap(String unitId, String[] gradeIds, String acadyear, String semester,
                                                 int weekIndex);

	public List<ClassTeaching> findListByGradeIdAndSubId(String acadyear, String semester, String unitId, String gradeId,
			String subjectId);

	/**
	 * 包括毕业班级
	 * @param unitId
	 * @param teaherIds
	 * @return
	 */
	public List<ClassTeaching> findClassTeachingListHistoryByTeacherId(String unitId, String[] teaherIds);

	/**
	 * 返回相应 行政班和教学班 +科目
	 * @param unitId
	 * @param teaherIds
	 * @return  list<ClassTeaching>
	 */
	public List<ClassTeaching> findClassTeachingListByBlendTeacherIds(String unitId, String[] teaherIds);

	/**
	 * 返回相应 行政班和教学班 +科目
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param teaherIds
	 * @return  list<ClassTeaching>
	 */
	public List<ClassTeaching> findClassTeachingListByBlendTeacherIds(String unitId,String acadyear,String semester, String[] teaherIds);
}
