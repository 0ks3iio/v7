package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Clazz;

import java.util.Date;

public interface ClassRemoteService extends BaseRemoteService<Clazz,String> {

	/**
	 * 获取班级信息，并根据班级编号排序
	 * 
	 * @param schoolId
	 * @param courseId
	 * @param oepnAcadyear
	 * @param acadyear
	 * @param semester
	 * @param section
	 * @return List(Clazz)
	 */
	public String findClassList(String schoolId, String courseId, String oepnAcadyear, String acadyear, String semester,
			int section);
	
	/**
	 * 根据老师ID，得到班主任是该老师的班级列表（去除软删除和已毕业的班级），包括副班主任的情况
	 * @param teacherId
	 * @return
	 */
	public String findByTeacherId(String teacherId);


	/**
	 * 根据教师ID和毕业学年得到毕业班列表（去除软删除），包括副班主任的情况
	 * @param teacherId
	 * @param graduateAcadyear
	 * @return 班级列表
	 */
	public String findByIdAcadyear(String teacherId, String graduateAcadyear);

	/**
	 * 根据教师ID和毕业学年得到已经毕业的班级列表（去除软删除），包括副班主任的情况
	 * @param teacheId
	 * @param graduateAcadyear
	 * @return
	 */
	public String findByteacherIdAcadyear(String teacheId,String graduateAcadyear);

	/**
	 * 班级列表(包括已毕业的班级)，自定义报表使用
	 * @param schoolId
	 * @return
	 */
	public String findByAllSchoolId(String schoolId);

	public String findByOpenAcadyear(String schoolId, String openAcadyear);

	public String findByGradeId(String schoolId, String gradeId,String teacherId);

	/**
	 * 根据学校ID和指定学年（非入学学年）得到有效的班级列表
	 * @param schoolId
	 * @param curAcadyear
	 * @return
	 */
	public String findByIdCurAcadyear(String schoolId, String curAcadyear);

	/**
	 * 得到学校中，在当前学年超过学制而还没有毕业的班级列表
	 * @param schoolId
	 * @param curAcadyear
	 * @return
	 */
	public String findByOverSchoolinglen(String schoolId, String curAcadyear);

	/**
	 * 根据学校，毕业学年得到毕业班列表（即：指定学年内的毕业班列表，不论毕业标志是0还是1）
	 * @param schoolId
	 * @param graduateAcadyear
	 * @return
	 */
	public String findByGraduateyear(String schoolId, String graduateAcadyear);

	/**
	 * 根据校区ID获得班级信息列表，并按所属学段、入学学年、班级编码分别升序排序
	 * @param campusId
	 * @return
	 */
	public String findByCampusId(String campusId);

	/**
	 * 根据分校区ID和毕业学年得到毕业班列表（即：指定分校区的毕业班列表，不论毕业标志是0还是1）
	 * @param campusId
	 * @param graduateAcadyear
	 * @return
	 */
	public String findByIdYear(String campusId, String graduateAcadyear);

	/**
	 * 修改毕业标志
	 * @param classId
	 * @param sign
	 */
	public void updateGraduateSign(int sign, Date currentDate, String classId);


	/**
	 * @param schoolId
	 * @param section
	 * @param enrollyear
	 * @param artScienceType
	 * @return
	 */
	public String findByIdSectionYearType(String schoolId, int section,
			String enrollyear, String artScienceType);

	/**
	 * @param schoolId
	 * @param section
	 * @param acadyear
	 * @return
	 */
	public String findByIdSectionYear(String schoolId, int section,
			String acadyear);

	/**
	 * @param schoolId
	 * @return
	 */
	public String findBySchoolId(String schoolId);

	
	/**
	 * @param gradeId
	 * @return 未删除的数据包括毕业数据
	 */
	public String findByGradeIdSortAll(String gradeId);

	/**
	 * @param gradeIds
	 * @return
	 */
	public String findByInGradeIds(String[] gradeIds);

	/**
	 * @param schoolId
	 * @param gradeIds
	 * @return
	 */
	public String findBySchoolIdInGradeIds(String schoolId, String[] gradeIds);

	/**
	 * @param schoolId
	 * @param teacherId
	 * @return
	 */
	public String findBySchoolIdTeacherIdAll(String schoolId, String teacherId);

	/**
	 * 根指指定的学校ID和指定学期得到班级中属于同一个年级的班级部分字段列表
	 * @param schoolId
	 * @param curAcadyear
	 * @return
	 */
	public String findBySchoolIdCurAcadyear(String schoolId, String curAcadyear);

	/**
	 * 根指指定的学校ID和指定学期得到班级中属于同一个年级的班级部分字段列表
	 * @param schoolId
	 * @param graduateAcadyear
	 * @return
	 */
	public String findBySchoolIdGraduateAcadyear(String schoolId,
			String graduateAcadyear);

	/**
	 * 根据学校，学年得到当年非毕业的年级列表 不包括当前期年度毕业班级
	 * @param schoolId
	 * @param acadyear
	 * @return
	 */
	public String findBySchoolIdAcadyear(String schoolId, String acadyear);

	/**
	 * 根指指定的学校ID和班主任ID得到由该班主任任教班级中属于同一个年级的班级部分字段列表
	 * @param schoolId
	 * @param teacherId
	 * @return
	 */
	public String findBySchoolIdTeacherId(String schoolId, String teacherId);

	/**
	 * 得到同一个年级下的班级表列（无分校区）
	 * @param schoolId
	 * @param section
	 * @param enrollyear
	 * @param sl
	 * @return
	 */
	public String findClassesByGrade(String schoolId, int section,
			String enrollyear, String sl);

	/**
	 * 得到同一个年级下的班级列表（有分校区）
	 * @param schoolId
	 * @param campusId
	 * @param section
	 * @param enrollyear
	 * @param schoolingLen
	 * @return
	 */
	public String findClassesByGrade(String schoolId, String campusId,
			int section, String enrollyear, int schoolingLen);

	/**
	 * 根据学校id和年级id获取班级列表
	 * @param schoolId
	 * @param gradeId
	 * @return获取未毕业班级
	 */
	public String findBySchoolIdGradeId(String schoolId, String gradeId);

	/**
	 * 得到同一个年级下相同文理类型的班级表列（无分校区）
	 * @param schoolId
	 * @param section
	 * @param enrollyear
	 * @param schoolingLen
	 * @param artScienceType
	 * @return
	 */
	public String findClassesByKinClass(String schoolId, int section,
			String enrollyear, int schoolingLen, String artScienceType);

	/**
	 * 根据id获取有效的班级，有排序
	 * @param ids
	 * @return
	 */
	public String findClassListByIds(String[] ids);

	/**
	 * 数组形式entitys参数，返回list的json数据
	 * @param entitys
	 * @return
	 */
	public String saveAllEntitys(String entitys);

	/**
	 * 根据年级ids找正常班级
	 * @param gradeIds
	 * @return Map<String, List<Clazz>>
	 */
	public String findMapByGradeIdIn(String[] gradeIds);
		
	/**
     * 根据班级code获取班级 order By classCode
     * @param classCodes
     * @return
     */
    public String findByClassCode(String unitId, String[] classCodes);
    /**
     * 
     * @param unitId
     * @param subjectId
     * @param openAcadyear
     * @param acadyear
     * @param semester
     * @param section
     * @param isTeach 走班教学 null:全部  1:走班 0:不走班
     * @return
     */
	public String findClassList(String unitId, String subjectId,
			String openAcadyear, String acadyear, String semester, int section,
			Integer isTeach);

	/**
	 * 班级从小到大排序
	 * @param classIds
	 * @return
	 */
	public String findByIdsSort(String[] classIds);

	/**
	 * 获取班级
	 * @param schoolIds
	 * @return
	 */
	public String findBySchoolIdIn(String[] schoolIds);

	
	
	public String findAllBySchoolIdIn(String[] schoolIds);

	/**
	 * 获取班级名称名称ids 包括教学班id 和行政班id
	 * @param ids
	 * @return gradeId,className,id,schoolId
	 */
	public String findListBlendIds(String[] ids);
}
