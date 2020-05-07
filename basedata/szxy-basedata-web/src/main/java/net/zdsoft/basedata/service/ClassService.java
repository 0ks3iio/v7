package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Clazz;

public interface ClassService extends BaseService<Clazz, String> {

    /**
     * 获取班级信息
     * 
     * @param schoolId
     * @param gradeIds
     *            年级ID数组，可以为空
     * @return
     */
    public List<Clazz> findBySchoolIdAndGradeIds(String schoolId, String... gradeIds);

    /**
     * 通用方法 根据年级找未删除未毕业的班级 排序：order by classCode
     * 
     * @param gradeIds
     * @return
     */
    public List<Clazz> findByGradeIdIn(String... gradeIds);
    /**
     * 给了走索引提供性能
     * @param gradeId 不给班级名称拼接
     * @return
     */
    public List<Clazz> findByGradeId(String gradeId);
    /**
     * 根据年级找未删除的班级数量 key greadId
     * 
     * @param gradeIds
     * @return
     */
    public Map<String, Integer> countByGradeIds(String[] gradeIds);

    /**
     * 根据年级、班级名称（非模糊）找未删除的班级 排序：无
     * 
     * @param gradeId
     * @param className
     * @return
     */
    public List<Clazz> findClassList(String gradeId, String className);

    /**
     * 根据年级找未删除、未毕业的班级 key greadId
     * 
     * @param gradeIds
     * @return
     */
    public Map<String, List<Clazz>> findMapByGradeIdIn(String[] gradeIds);

    /**
     * 根据id软删
     * 
     * @param ids
     */
    void deleteAllIsDeleted(String... ids);

    /**
     * 取得班级的下一个编号
     * 
     * @param schoolId
     * @param section
     * @param acadyear
     *            班级所在的开设学年
     * @param schoolingLength
     * @return
     */
    public String findNextClassCode(String schoolId, String section, String acadyear, int schoolingLength);

    /**
     * 根据校区找未删除、未毕业的班级 排序：order by classCode
     * 
     * @param teachAreaIds
     * @return
     */
    public List<Clazz> findByTeachAreaIdIn(String... teachAreaIds);

    /**
     * 根据单位、学科、开设学年、学期、学段找未删除的班级 排序：order by classCode
     * 
     * @param schoolId
     * @param courseId
     * @param oepnAcadyear
     * @param acadyear
     * @param semester
     * @param section
     * @return
     */
    public List<Clazz> findClassList(String schoolId, String courseId, String oepnAcadyear, String acadyear,
            String semester, int section);

    /**
     * 根据id找班级 排序：无
     * 
     * @param ids
     * @return
     */
    public List<Clazz> findListByIds(String[] ids);

    /**
     * 根据id找班级，带年级名的班级名classnamedynamic
     * 
     * @param ids
     * @return
     */
    public Map<String, Clazz> findByIdInMapName(String... ids);

    /**
     * 根据老师ID，得到班主任是该老师的班级列表（去除软删除和已毕业的班级），包括副班主任的情况
     * 
     * @param teacherId
     * @return
     */
    public List<Clazz> findByTeacherId(String teacherId);

    /**
     * 根据教师ID和毕业学年得到毕业班列表（去除软删除），包括副班主任的情况
     * 
     * @param teacherId
     * @param graduateAcadyear
     * @return
     */
    public List<Clazz> findByIdAcadyear(String teacherId, String graduateAcadyear);

    /**
     * 根据教师ID和毕业学年得到已经毕业的班级列表（去除软删除），包括副班主任的情况
     * 
     * @param teacheId
     * @param graduateAcadyear
     * @return
     */
    public List<Clazz> findByteacherIdAcadyear(String teacheId, String graduateAcadyear);

    /**
     * 班级列表(包括已毕业的班级)，自定义报表使用
     * 
     * @param schoolId
     * @return
     */
    public List<Clazz> findBySchoolId(String schoolId);

    public List<Clazz> findByOpenAcadyear(String schoolId, String openAcadyear);

    public List<Clazz> findByGradeId(String schoolId, String gradeId, String teacherId);

    /**
     * 根据学校ID和指定学年（非入学学年）得到有效的班级列表
     * 
     * @param schoolId
     * @param curAcadyear
     * @return
     */
    public List<Clazz> findByIdCurAcadyear(String schoolId, String curAcadyear);

    /**
     * 得到学校中，在当前学年超过学制而还没有毕业的班级列表
     * 
     * @param schoolId
     * @param curAcadyear
     * @return
     */
    public List<Clazz> findByOverSchoolinglen(String schoolId, String curAcadyear);

    /**
     * 根据学校，毕业学年得到毕业班列表（即：指定学年内的毕业班列表，不论毕业标志是0还是1）
     * 
     * @param schoolId
     * @param graduateAcadyear
     * @return
     */
    public List<Clazz> findByGraduateyear(String schoolId, String graduateAcadyear);

    /**
     * 根据校区ID获得班级信息列表，并按所属学段、入学学年、班级编码分别升序排序
     * 
     * @param campusId
     * @return
     */
    public List<Clazz> findByCampusId(String campusId);

    /**
     * 根据分校区ID和毕业学年得到毕业班列表（即：指定分校区的毕业班列表，不论毕业标志是0还是1）
     * 
     * @param campusId
     * @param graduateAcadyear
     * @return
     */
    public List<Clazz> findByIdYear(String campusId, String graduateAcadyear);

    /**
     * 修改毕业标志
     * 
     * @param classId
     * @param sign
     */
    public void updateGraduateSign(int sign, Date currentDate, String classId);

    /**
     * 根据id获取有效的班级，有排序
     * 
     * @param ids
     * @return
     */
    public List<Clazz> findClassListByIds(String[] ids);

    public List<Clazz> saveAllEntitys(Clazz... clazz);

    /**
     * 根据班级code获取班级 order By classCode
     * @param classCodes
     * @return
     */
    public List<Clazz> findByClassCode(String unitId, String[] classCodes);
    
    public List<Clazz> findClassList(String schoolId, String courseId, String oepnAcadyear, String acadyear,
            String semester, int section,Integer isTeach);

	public List<Clazz> findByIdsSort(String[] classIds);

	public List<Clazz> findBySchoolIdIn(String[] schoolIds);

	Clazz findOneDynamic(String classId);

    void deleteClazzBySchoolId(String unitId);
   /**
    * 根据场地 获取对应 班级
    * @param schoolId
    * @param placeIds
    * @return
    */
	List<Clazz> findByPlaceIds(String schoolId, String[] placeIds);

	/**
	 * 获取本单位下的所有数据，包括软删的数据 
	 * @param schoolIds
	 * @return
	 */
    List<Clazz> findAllBySchoolIdIn(String[] schoolIds);
    /**
     * 获取班级名称名称ids 包括教学班id 和行政班id
     * @param ids
     * @return
     */
    List<Clazz> findListBlendIds(String[] ids);
}
