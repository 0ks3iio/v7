package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeacherDao extends BaseJpaRepositoryDao<Teacher, String>, TeacherExtDao {

    @Query("From Teacher where isDeleted = 0 and unitId = ?1")
    List<Teacher> findByUnitId(String unitId);

    @Query("From Teacher where unitId = ?1 and isDeleted = 0")
    List<Teacher> findByUnitId(String unitId, Pageable page);

    List<Teacher> findByIdIn(String[] ids);

    @Query("From Teacher where deptId = ?1 and isDeleted = 0")
    List<Teacher> findByDeptId(String deptId);

    @Query("From Teacher where deptId = ?1 and isDeleted = 0")
    List<Teacher> findByDeptId(String deptId, Pageable page);

    @Query("select count(*) from Teacher where deptId = ?1 and isDeleted = 0")
    Long countByDeptId(String deptId);

    @Query("select count(*) from Teacher where unitId = ?1 and isDeleted = 0")
    Long countByUnitId(String unitId);

    @Query("From Teacher where isDeleted = 0 and deptId IN ?1")
    List<Teacher> findByDeptIds(String... deptIds);

    @Query("From Teacher where isDeleted = 0 and instituteId = ?1")
    List<Teacher> findByInstituteId(String instituteId);

    @Query(value = "select * from base_teacher where is_deleted = 0 and unit_id = ?1 and exists (select 1 from base_user where is_deleted = 0 and user_state = ?2 and owner_id = base_teacher.id)", nativeQuery = true)
    List<Teacher> findByUnitIdUserState(String unitId, Integer userState);

    @Query(value = "select * from base_teacher where is_deleted = 0 and dept_id = ?1 and exists (select 1 from base_user where is_deleted = 0 and user_state = ?2 and owner_id = base_teacher.id)", nativeQuery = true)
    List<Teacher> findByDeptIdUserState(String deptId, Integer userState);

    @Query("From Teacher where isDeleted = 0 and identityCard IN ?1")
    List<Teacher> findByIdentityCards(String... cardNos);

    @Modifying
    @Query("delete from Teacher where id in (?1)")
    void deleteAllByIds(String... id);

    /**
     * 部门领导ids
     *
     * @param unitId
     * @param userState
     * @return
     */
    @Query(value = "select id from base_teacher where is_deleted = 0 and dept_id=?1 and exists (select 1 from base_teacher_duty where is_deleted = 0 and duty_code<>'114' and teacher_id = base_teacher.id)", nativeQuery = true)
    List<String> findLeadersByDeptId(String detpId);

    List<Teacher> findByTeacherNameAndIdentityCard(String teacherName, String identityCard);
    
    
    @Query("From  Teacher Where  teacherName = ?1 and identityCard=?2 and isDeleted = 0 and id not in (select ownerId from User u1 where u1.isDeleted = 0 and u1.ownerType= 2)")
	List<Teacher> findByTeacherNameAndIdentityCardWithNoUser(
			String teacherName, String identityCard);
    
    @Query("From  Teacher Where  teacherName = ?1 and mobilePhone=?2 and isDeleted = 0 ")
	List<Teacher> findByTeacherNameAndMobilePhone(String realName,
			String mobilePhone);

    @Query("From  Teacher Where unitId = ?1 and cardNumber=?2 and isDeleted = 0 ")
	Teacher findByCardNumber(String unitId, String cardNumber);

    @Query("From Teacher where isDeleted = 0 and unitId in ?1")
	List<Teacher> findByUnitIdIn(String[] unitIds);

    @Query("From  Teacher Where   unitId = ?1 and teacherName like ?2 and isDeleted = 0 ")
	List<Teacher> findByTeacherNameLike(String unitId,String teacherName);

	/**
	 * @param teacherName
	 * @return
	 */
    @Query("From  Teacher Where  teacherName like ?1 and isDeleted = 0 ")
	List<Teacher> findByTeacherNameLike(String teacherName);
    
    @Query("select id,teacherName From Teacher Where id in (?1) and isDeleted = 0 ")
    List<Object[]> findPartTeachByIds(String[] ids);

    @Modifying
    @Query(
            value = "update Teacher set isDeleted=1 where unitId=?1"
    )
    void deleteTeahersByUnitId(String unitId);
    
    
    @Query("From Teacher where isDeleted = 0 and unitId in ?1")
  	List<Teacher> findByUnitIdIn(String[] unitIds,Pageable page);
}
