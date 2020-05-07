package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StudentDao extends BaseJpaRepositoryDao<Student, String>, StudentJdbcDao {

    @Query("From Student Where classId in (?1) and isDeleted = 0 and isLeaveSchool = 0 order by classId, studentCode")
    List<Student> findByClassIdsIn(String... classIdIn);

    @Query("select s1 from Student as s1 ,TeachClassStu as s2,TeachClass s3 where s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s2.isDeleted = 0 and s1.id=s2.studentId and s3.id=s2.classId and s2.classId in  (?1) order by s1.schoolId, s3.name, s1.unitiveCode")
    List<Student> findByTeachClass(String[] teachClassIds, Pageable page);

    @Query("select s1 from  Student as s1,Clazz as s2 Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s2.id=s1.classId and s1.studentName like ?1 and s1.classId in (?2)  order by s1.schoolId, s2.section, s2.acadyear desc, s2.classCode asc, s1.identityCard")
    List<Student> findByClassIdIn(String stuname, String[] classIds, Pageable page);

    @Query("select s1 from Student as s1 ,TeachClassStu as s2,TeachClass s3 where s1.isDeleted = 0 "
    		+ "and s1.isLeaveSchool = 0 and s2.isDeleted = 0 and s1.id=s2.studentId and s3.id=s2.classId "
    		+ "and s2.classId in  (?1) order by s1.schoolId, s3.name, s1.studentCode,s1.unitiveCode")
    List<Student> findByTeachClass(String[] teachClassIds);

    @Query("select s1 from Student as s1 , Clazz as s2 Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s2.id=s1.classId and s1.studentName like ?1 and s1.classId in (?2)  order by s1.schoolId, s2.classCode desc, s1.identityCard")
    List<Student> findByClassIdIn(String stuname, String[] classIds);

    @Query("select s1 from Student as s1 , Clazz as s2 Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s2.id=s1.classId and s1.studentName like ?1 and s1.schoolId in (?2) order by s1.schoolId, s2.classCode desc, s1.identityCard")
    List<Student> findBySchoolIdIn(String stuName, String[] schoolIds, Pageable pageable);

    @Query("select s1 from Student as s1 , Clazz as s2 Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s2.id=s1.classId and s1.studentName like ?1 and s1.schoolId in (?2) order by s1.schoolId, s2.classCode desc, s1.identityCard")
    List<Student> findBySchoolIdIn(String stuName, String[] schoolIds);

    @Modifying
    @Query("update Student set isDeleted=1 , modifyTime=?1 where id in (?2)")
    void updateIsDeleteds(Date modifyTime, String[] ids);

    @Query("From Student Where  isDeleted = 0 and isLeaveSchool = 1 and schoolId =?1 and studentName = ?2 and sex =?3 and (identityCard is null or identityCard='') ")
    List<Student> findByNameSexNoCard(String schoolId, String name, Integer sex);

    @Query("select s1 from Student as s1  Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s1.schoolId = ?1 and s1.studentCode = ?2")
    Student findBySchIdStudentCode(String schoolId, String studentCode);

    @Query("select s1 from Student as s1  Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s1.schoolId = ?1 and s1.studentCode in ?2")
    List<Student> findBySchIdStudentCodes(String schoolId, String[] studentCodes);

    @Query("select s1 from  Student as s1,Clazz as s2 Where  s1.isDeleted = 0 and s1.isLeaveSchool = 0 and s2.id=s1.classId and s2.gradeId=?1 and s1.studentCode = ?2 and s2.isDeleted = 0")
    Student findByGreadIdStuCode(String greadId, String studentCode);

    @Query("From  Student Where schoolId = ?1 and cardNumber = ?2 and isDeleted = 0 and isLeaveSchool = 0")
    Student findByCardNumber(String unitId, String cardNumber);

    @Query("From  Student Where  studentName = ?1 and identityCard=?2 and isDeleted = 0 and isLeaveSchool = 0 and id not in (select ownerId from User u1 where u1.isDeleted = 0 and u1.ownerType= 1)")
    List<Student> findByStudentNameAndIdentityCardWithNoUser(String studentName,
                                                             String identityCard);

    @Query("From  Student Where  studentName = ?1 and identityCard=?2 and isDeleted = 0 and isLeaveSchool = 0 ")
    List<Student> findByStudentNameAndIdentityCard(String studentName,
                                                   String identityCard);

    @Query("select s1 from  Student as s1,Clazz as s2 Where s2.gradeId=?1 and s2.isDeleted = 0 and s2.isGraduate=0 and s2.id=s1.classId and s1.isDeleted = 0 and s1.isLeaveSchool = 0")
    List<Student> findByGreadId(String greadId);

    @Query("select s1 from  Student as s1,Clazz as s2 Where s2.gradeId=?1 and s2.isDeleted = 0 and s2.id=s1.classId and s1.isDeleted = 0")
    List<Student> findAllStudentByGradeId(String gradeId);

    @Query("From  Student Where schoolId = ?1 and isDeleted = 0 and isLeaveSchool = 0")
    List<Student> findBySchoolId(String schoolId);

    @Query(
            value = "update Student set isDeleted=0 where schoolId=?1"
    )
    @Modifying
    void deleteStudentsBySchoolId(String schoolId);

    @Query("From  Student Where studentCode = ?1 and isDeleted = 0 and isLeaveSchool = 0")
	Student findByStudentCode(String studentCode);

    Map<String,Integer> countListBySchoolIds(String[] schoolIds);

    Map<String, Integer> countMapByGradeIds(String[] gradeIds);


}
