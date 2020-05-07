package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Map;

public interface StudentService extends BaseService<Student, String> {
	
	
	/**
	 * 支持id超过1000已经处理
	 */
	List<Student> findListByIds(String[] ids);
	

    Map<String, Integer> countMapByClassIds(String... classIds);

    /**
     * 根据学生id数组取得学生姓名的map 不包括删除的
     *
     * @param studentIds
     * @return Map key=>id value=>studentName
     */
    Map<String, String> getStudentNameMap(String[] studentIds);

    /**
     * 调入调出用(查询已离校的学生:已离校无验证码和在的学生)
     *
     * @param studentName
     * @param identityCard
     * @return
     */
    List<Student> getStudentIsLeaveSchool(String studentName, String identityCard, String unitId, Pagination page);

    /**
     * 通用方法 根据班级找未删除、未离校的学生 排序：无
     *
     * @param classId
     * @return
     */
    List<Student> findByClassIds(String... classIds);

    /**
     * 教学班下学生
     *
     * @param teachClassIds
     * @param page
     * @return
     */
    List<Student> findByTeachClass(String[] teachClassIds, Pagination page);

    List<Student> findByTeachClass(String[] teachClassIds);

    /**
     * 行政班下学生
     *
     * @param teachClassIds
     * @param stuName       学生姓名 可为空 模糊查询
     * @param page
     * @return
     */
    List<Student> findByClassIdIn(String stuName, String[] classIds, Pagination page);

    List<Student> findByClassIdIn(String stuName, String[] classIds);

    /**
     * 单位下学生
     *
     * @param stuName  学生姓名 可为空 模糊查询
     * @param unitId   不能为空
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param page
     * @return
     */
    List<Student> findBy(String stuName, String unitId, String schoolId, String gradeId, String classId,
                         Pagination page);

    /**
     * @param stuName   学生姓名 可为空 模糊查询
     * @param schoolIds
     * @param page
     * @return
     */
    List<Student> findBySchoolIdIn(String stuName, String[] schoolIds, Pagination page);

    /**
     * @param stuName   学生姓名 可为空 模糊查询
     * @param schoolIds
     * @return
     */
    List<Student> findBySchoolIdIn(String stuName, String[] schoolIds);

    /**
     * 删除
     *
     * @param ids
     */
    void updateIsDeleteds(String[] ids);

    /**
     * 查询某学校离校学生的身份证号为空的学生
     *
     * @param schoolId
     * @param name     学生姓名 精确查询
     * @param sex      性别 必填
     * @return
     */
    List<Student> findByNameSexNoCard(String schoolId, String name, Integer sex);

    /**
     * 修改离校学生身份证号
     *
     * @param studentList
     */
    void updateIdCard(List<Student> studentList);

    /**
     * 根据单位id范围根据学号查找学生
     *
     * @param schoolId
     * @param studentCode
     * @return
     */
    Student findBySchIdStudentCode(String schoolId, String studentCode);

    /**
     * 根据单位id范围根据学号查找学生
     *
     * @param schoolId
     * @param studentCodes
     * @return
     */
    List<Student> findBySchIdStudentCodes(String schoolId, String[] studentCodes);

    /**
     * 根据身份证号查询学生
     *
     * @param identityCards
     * @return
     */
    List<Student> findByIdentityCards(String[] identityCards);

    /**
     * 根据年级id范围根据学号查找学生
     *
     * @param schoolId
     * @param studentCode
     * @return
     */
    Student findByGreadIdStuCode(String greadId, String studentCode);

    List<Student> saveAllEntitys(Student... student);

    /**
     * 根据条件查询未被删除的学生
     *
     * @param unitId必填
     * @param ids           必填
     * @param classIds      可以为空
     * @param searchStudent 提供性别，姓名，学号
     * @param page          可以为空
     * @return order by class_id,studentCode
     */
    List<Student> findByIdsClaIdLikeStuCodeNames(String unitId, String[] ids, String[] classIds,
                                                 Student searchStudent, Pagination page);

    /**
     * 根据条件查询未被删除的学生
     *
     * @param unitId必填
     * @param ids           必填
     * @param classIds      可以为空
     * @param searchStudent 提供性别，姓名，学号
     * @param page          可以为空
     * @return order by class_id,studentCode
     */
    List<Student> findByNotIdsClaIdLikeStuCodeNames(String unitId, String[] ids, String[] classIds,
                                                    Student searchStudent, Pagination page);

    /**
     * 根据条件查询 valCode like 'val%' 如学号 valCode=studentCode
     *
     * @param unitId  必填
     * @param valCode 必填
     * @param Val     必填
     * @return
     */
    List<Student> findByUnitLikeCode(String unitId, String valCode, String Val);

    /**
     * 根据班级找未删除、未毕业的学生 key classId
     *
     * @param gradeIds
     * @return
     */
    Map<String, List<Student>> findMapByClassIdIn(String[] classIds);

    /**
     * 通用方法 根据年级找未删除、未离校的学生 排序：无
     *
     * @param gradeIds
     * @return
     */
    List<Student> findByGradeIds(String... gradeIds);

    /**
     * 通用方法 根据年级找未删除、未离校的学生 排序：无
     *
     * @param gradeId 走索引提供性能
     * @return
     */
    List<Student> findByGradeId(String gradeId);

    /**
     * 根据年级找未删除的学生,包括离校和毕业的
     * @return
     */
    List<Student> findAllStudentByGradeId(String gradeId);

    Student findByCardNumber(String unitId, String cardNumber);

    List<Student> findByStudentNameAndIdentityCardWithNoUser(String studentName,
                                                             String identityCard);

    List<Student> findByStudentNameAndIdentityCard(String studentName,
                                                   String identityCard);

    /**
     * 更新学生一卡通号
     *
     * @param studentList
     */
    int[] updateCardNumber(List<String[]> studentList);

    List<Student> findByClaIdsLikeStuCodeNames(String unitId, String gradeId, String[] classIds, Student searchStudent);

    List<Student> findBySchoolId(String schoolId);

    /**
     * 获取年级中在校学生只是显示（学号姓名性别身份证等基础信息）
     * <p>三个参数每次只能传一个，其他需要为空</p>
     *
     * @param unitId     必须传
     * @param gradeId    所需年级
     * @param classIds   所需班级
     * @param studentIds 所需学生
     * @return
     */
    List<Student> findPartStudByGradeId(String unitId, String gradeId, String[] classIds, String[] studentIds);

    /**
     * 获取年级中在校学生数
     *
     * @param classIds
     * @return long
     */
    long CountStudByGradeId(String gradeId);

    void updateClaIds(List<Student> studentList);

    List<Student> findPartStudentById(String[] studentIds);

    Map<String, Object> findMapByAttr(String[] ids, String attrName);

    void deleteStudentsBySchoolId(String schoolId);

    /**
	  * 根据姓名或者学号精确查询学生
	  * @param unitId 不能为空
	  * @param studentName 
	  * @param studentCode
	  * @return List<Student> 提供学生id,姓名
	  */
	List<Student> findBySchoolIdStudentNameStudentCode(String unitId, String studentName, String studentCode);

	Student findByStudentCode(String studentCode);

    Map<String,Integer> countListBySchoolIds(String[] schoolIds);

    Map<String,Integer> countMapByGradeIds(String[] gradeIds);

    /**
     * 获取学生信息 包括教学班ids 和行政班ids
     * @param classIds
     * @return List<Student>
     */
    List<Student> findListBlendClassIds(String[] classIds);
}
