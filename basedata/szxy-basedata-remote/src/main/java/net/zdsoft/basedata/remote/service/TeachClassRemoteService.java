package net.zdsoft.basedata.remote.service;


import net.zdsoft.basedata.entity.TeachClass;

public interface TeachClassRemoteService extends BaseRemoteService<TeachClass,String> {

    /**
     * 获取正常状态的教学班
     * 
     * @param unitId
     * @param acadyear
     * @param semester
     * @param courseId
     * @return List&lt;TeachClass&gt;
     */
    public String findTeachClassList(String unitId, String acadyear, String semester, String courseId);

    /**
     * 根据id获取有效教学班
     * 
     * @param ids
     * @return
     */
    public String findTeachClassListByIds(String[] ids);
    /**
     * 根据id获取未删除教学班（包含isUsing=0）
     * 
     * @param ids
     * @return
     */
    public String findTeachClassContainNotUseByIds(String[] ids);

    public String findByCourseIdAndInIds(String courseId, String[] ids);

    /**
     * 删除教学班，同时删除该教学班下学生(都是硬删除)
     * 
     * @param ids
     */
    public void deleteByIds(String[] ids);
    
    /**
     * 删除该教学班下学生(都是硬删除)
     * 
     * @param ids
     */
    public void deleteStusByClaIds(String[] ids);

    /**
     * 批量保存
     */
    public void saveAll(String teachClasses);

    /**
     * 获取正常状态的教学班
     * 
     * @param unitId
     * @param acadyear
     * @param semester
     * @param subjectId
     * @param gradeIds
     *            为null时获取gradeId为空的教学班
     * @param is_filtration 是否过滤小班添加合班信息
     * @return
     */
    public String findTeachClassList(String unitId, String acadyear, String semester, String subjectId,
            String[] gradeIds, boolean isFiltration);
    /**
     * 获取所有的教学班
     * 
     * @param unitId
     * @param acadyear 
     * @param semester 
     * @param subjectId 
     * @param gradeId 不为空
     * @return
     */
    public String findTeachClassListByGradeId(String unitId, String acadyear, String semester, String subjectId,
            String gradeId);

    /**
     * 查询指定教师某个学年学期的教学班信息
     * 
     * @author dingw
     * @param teacherId
     * @param acadyear
     * @param semester
     * @return JSON(List<TeachClass>)
     */
    String findListByTeacherId(String teacherId, String acadyear, String semester);

    /**
     * 查询指定学生参与的教学班信息
     * @param schoolId 必须
     * @param acadyear
     * @param semester
     * @param isGk 是否新高考
     * @param studentId 必须
     * @return JSON(List<TeachClass>)
     */
	public String findTeachClassList(String unitId, String acadyear, String semester, boolean isGk, String studentId);
	/**
	 * 不启用一些班级
	 * @param teaClsIds
	 */
	public void notUsing(String[] teaClsIds);

	public void yesUsing(String[] array);
	
	/**
	 * 根据班级id 获取班级总人数，男，女 
	 * @param ids
	 * @return new string[]{sum-num,male-num,female-num}
	 */
    public String countNumByIds(String[] ids);
    
    public String findByGradeId(String gradeId);
    
    public String findByNames(String unitId, String[] names);
    /**
     * 调用findByStuIdAndAcadyearAndSemester(String studentId, String schoolId,String acadyear, String semester)
     * @param studentId
     * @param acadyear
     * @param semester
     * @return
     */
    @Deprecated
    public String findByStuIdAndAcadyearAndSemester(String studentId, String acadyear, String semester);
    
    /**
     * 根据学年学期查询某个学生的教学班（一个学生不会查出在多个学校，增加schoolId,提高查询速度）
     * @param studentId
     * @param schoolId
     * @param acadyear
     * @param semester
     * @return
     */
    public String findByStuIdAndAcadyearAndSemester(String studentId, String schoolId,String acadyear, String semester);

    /**
     * 获取所有的正在使用的教学班(实际不是正在使用的)
     * 
     * @param unitId
     * @param acadyear 
     * @param semester 
     * @param classType 
     * @param gradeId 可为空
     * @return
     */
	public String findBySearch(String unitId, String acadyear, String semester,String classType,
			String gradeId,String subjectId);
	
	/**
	 * 杭外定制，根据大班获取小班
	 * @param parentId
	 * @return
	 */
	public String findByParentIds(String[] parentId);
	
	/**
     * 根据学生id，获取相关的教学班
     * 添加一个参数控制 返回 大班列表 还是小班列表
     * @param stuIds
     * @param isBigClass 0 返回小班 1 返回大班
     * @return
     */
	public String findByStuIds(String acadyear, String semester,String isBigClass, String[] stuIds);

	/**
	 * 根据单位获取所有的班级信息，不包含软删除的
	 * @param unitIds
	 * @return
	 */
	public String findbyUnitIdIn(String... unitIds);

	String findClassHasEx(String unitId, String acadyear, String semester, String gradeId, String[] classTypes);

    /**
     * 获取老师班级信息
     * @param unitId
     * @param acadyear
     * @param semester
     * @param teacherIds
     * @return
     */
    String findByUnitIdAndAcadyearAndSemesterAndTeaIds(String unitId, String acadyear, String semester,String[] teacherIds);
}
