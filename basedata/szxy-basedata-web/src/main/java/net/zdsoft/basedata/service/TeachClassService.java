package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.framework.entity.Pagination;

public interface TeachClassService extends BaseService<TeachClass, String> {

    public List<TeachClass> findByAcadyearAndSemesterAndUnitId(String acadyear, String semester, String unitId);

    public List<TeachClass> findTeachClassList(String unitId, String acadyear, String semester, String courseId);
    /**
     * 判断当前单位教学班名称是否重复
     * @param id
     * @param schoolId
     * @param acadyear
     * @param semester
     * @param className
     * @return
     */
    public boolean findExistsClassName(String id,String schoolId, String acadyear, String semester, String className);

    /**
     * 拷贝教学班同时学生信息也拷贝过来
     * 
     * @param tc
     */
    public void saveCopyTeachClaStu(TeachClass tc);

    /**
     * 获取正常状态的教学班
     * 
     * @param unitId
     * @param acadyear
     * @param semester
     * @param subjectId
     * @param gradeIds 
     *            为null时，不判断gradeId(即可为空)
     * @param  isUsing 是不是获取只获取正常状态教学班
     * is_filtration 是否过滤小班添加合班信息
     * @return
     */
    public List<TeachClass> findTeachClassList(String unitId, String acadyear, String semester, String subjectId,boolean isUsing,String[] gradeIds,boolean isFiltration);

    /**
     * 查询学年学期下的教学班(分页)
     * 
     * @param acadyear
     * @param semester
     * @param unitId
     * @param page
     * @return
     */
    public List<TeachClass> findByAcadyearSemesterUnitId(String acadyear, String semester, String unitId,
            Pagination page);

    /**
     * 根据id获取有效教学班
     * 
     * @param ids
     * @return
     */
    public List<TeachClass> findTeachClassListByIds(String[] ids);
    /**
     * 根据id获取未删除教学班（包含isUsing=0）
     * 
     * @param ids
     * @return
     */
    public List<TeachClass> findTeachClassContainNotUseByIds(String[] ids);

    public List<TeachClass> findByCourseIdAndInIds(String courseId, String[] ids);

    /**
     * 删除教学班，同时删除该教学班下学生(都是硬删除)
     * 
     * @param ids
     */
    public void deleteByIds(String... ids);

    public List<TeachClass> saveAllEntitys(TeachClass... teachClass);

    /**
     * 查询指定教师某个学年学期的教学班信息
     * 
     * @author dingw
     * @param teacherId
     * @param acadyear
     * @param semester
     */
    List<TeachClass> findListByTeacherId(String teacherId, String acadyear, String semester);

	public List<TeachClass> findTeachClassList(String unitId, String acadyear, String semester, boolean isGk, String studentId);

	public void notUsing(String[] ids);

	public void yesUsing(String[] ids);
	/**
	 * 根据班级id 获取班级总人数，男，女 
	 * @param ids
	 * @return new string[]{sum-num,male-num,female-num}
	 */
    public Map<String,Integer[]> countNumByIds(String[] ids);
    
    
    @Deprecated
    List<TeachClass> findByStuIdAndAcadyearAndSemester(String studentId, String acadyear, String semester);
    
    List<TeachClass> findByStuIdAndAcadyearAndSemester(String studentId, String schoolId,String acadyear, String semester);
    
    public List<TeachClass> findByGradeId(String gradeId);
    
    public List<TeachClass> findByNames(String unitId, String[] names);
    /**
     * 查询某个单位 某个学期下 某个年级(或者多个gradeIds)的教学班列表
     * @param dto
     * @return
     */
	public List<TeachClass> findListByDto(TeachClassSearchDto dto);
	public List<TeachClass> findListByDtoWithMaster(TeachClassSearchDto dto);

	/**
	 * 组装exList 上课时间场地  科目名称 教师
	 * isMakeNum 是否组装学生人数
	 * @param teachClassList
	 */
	public void makeTimePlace(List<TeachClass> teachClassList,boolean isMakeNum);
	
	/**
	 * 查询正在使用的教学班
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classType 可为空
	 * @param gradeId 可为空
	 * @param subjectId 可为空
	 * @return
	 */
	public List<TeachClass> findBySearch(String unitId, String acadyear, String semester,String classType,
			String gradeId,String subjectId);
	public List<TeachClass> findBySearch(String unitId, String acadyear, String semester,String... gradeId);
	/**
	 * 
	 * @param teachClass
	 * @param teachExList 上课时间地点
	 * @param insertList 进入课表
	 * @param deleteDto
	 */
	public void saveAllTeachClassAndCourseSchedule(TeachClass teachClass,List<TeachClassEx> teachExList,
			List<CourseSchedule> insertList, CourseScheduleDto deleteDto,String oldTeachClassId);
	/**
	 * 不走缓存
	 * @param id
	 * @return
	 */
	public TeachClass findById(String id);
	/**
	 * 删除教学班（软删） 以及教学班课程表数据
	 * @param id
	 * @param dto
	 */
	public void deleteClassAndScheduleById(String id, CourseScheduleDto dto);
	/**
	 * 完成
	 * @param oldTeachClass
	 * @param delDto
	 */
	public void updateIsUsing(TeachClass oldTeachClass, CourseScheduleDto delDto);
	/**
	 * 根据学生ids取得教学班
	 * @param acadyear
	 * @param semester
	 * @param schoolId
	 * @param studentId
	 * @return
	 */
	public List<TeachClass> findByStudentIds(String acadyear, String semester,
			String schoolId, String[] studentId);
	/**
	 *
	 * @param teachClassList
	 * @param exList
	 * @param scheduleList
	 */
	public void insertTeachClassAndCourseSchedule(
			List<TeachClass> teachClassList,
			List<TeachClassEx> exList,
			List<CourseSchedule> scheduleList,CourseScheduleDto deleteDto);
	/**
     * 删除该教学班下学生(都是硬删除)
     * 
     * @param ids
     */
    public void deleteStusByClaIds(String[] ids);

    /**
     * 根据合并后的班级查询原班级
     * @param parentId
     * @return
     */
	public List<TeachClass> findByParentIds(String[] parentIds);
	
	/**
     * 根据学生id，获取相关的教学班
     * 添加一个参数控制 返回 大班列表 还是小班列表
     * @param stuIds
     * @param isBigClass 0 返回小班 1 返回大班
     * @return
     */
	public List<TeachClass> findByStuIds(String acadyear, String semester,String isBigClass, String[] stuIds);

	/**
	 * 删除年级后删除年级下的所有教学班
	 * @param gradeIds
	 */
	public void deleteByGradeIds(String... gradeIds);

	/**
	 * 删除课程后删除的所有教学班
	 * @param gradeId
	 */
	public void deleteBySubjectIds(String... subjectIds);

	public List<TeachClass> findbyUnitIdIn(String... unitIds);

	/**
	 * 查找虚拟课程对应的行政班级id
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param virtualIds
	 * @return
	 */
	public Map<String, List<String>> findClassIdsByVirtualIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds);

	/**
	 * 查找虚拟课程关联的教学班
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param virtualIds
	 * @return
	 */
	public List<TeachClass> findByRelaCourseIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds);

	List<TeachClass> findClassHasEx(String unitId, String acadyear, String semester, String gradeId,
			String[] classTypes);

	/**
	 * 获取老师班级信息
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param teacherIds
	 * @return
	 */
	List<TeachClass> findByUnitIdAndAcadyearAndSemesterAndTeaIds(String unitId, String acadyear, String semester,String[] teacherIds);

	List<TeachClass> findByUnitIdAndTeaIds(String unitId, String[] teacherIds);
}
