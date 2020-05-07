package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachClassDao extends BaseJpaRepositoryDao<TeachClass, String> {

    public static final String SQL_AFTER = " and isDeleted = 0 and isUsing='1' order by creationTime desc";
    public static final String SQL_AFTER_DELETE = " and isDeleted = 0 order by creationTime desc";

    @Query("From TeachClass where acadyear =?1 and semester = ?2 and unitId=?3" + SQL_AFTER)
    public List<TeachClass> findByAcadyearSemesterUnitId(String acadyear, String semester, String unitId,
            Pageable pageable);

    @Query("From TeachClass where acadyear =?1 and semester = ?2 and unitId=?3" + SQL_AFTER)
    public List<TeachClass> findByAcadyearSemesterUnitId(String acadyear, String semester, String unitId);

    @Query("From TeachClass where unitId = ?1 and acadyear =?2 and semester = ?3 and courseId=?4" + SQL_AFTER)
    public List<TeachClass> findTeachClassList(String unitId, String acadyear, String semester, String courseId);

    @Query("From TeachClass where unitId= ?1 and acadyear =?2 and semester = ?3 and name=?4" + SQL_AFTER_DELETE)
    public List<TeachClass> findByAcadyearAndSemesterAndName(String schoolId,String acadyear, String semester, String name);

   
    @Query("From TeachClass where id in (?1)" + SQL_AFTER)
    public List<TeachClass> findTeachClassListByIds(String... ids);
    
    @Query("From TeachClass where id in (?1)" + SQL_AFTER_DELETE)
    public List<TeachClass> findTeachClassContainNotUseByIds(String... ids);

    @Modifying
    @Query("update TeachClass set isDeleted=1,modifyTime=sysdate where id in (?1)")
    public void updateByIds(String[] ids);

    @Query("From TeachClass where courseId = ?1 and id in (?2)" + SQL_AFTER)
    public List<TeachClass> findByCourseIdAndInIds(String courseId, String[] ids);

    @Query(nativeQuery = true, value = "select c.* from base_teach_class_stu s,base_teach_class c where "
            + "s.class_id=c.id and s.student_id= ?1 and c.acadyear= ?2 and c.semester= ?3 and c.is_deleted=0 and s.is_deleted=0")
    List<TeachClass> findByStuIdAndAcadyearAndSemester(String studentId, String acadyear, String semester);
    
    @Query(nativeQuery = true, value = "select c.* from base_teach_class_stu s,base_teach_class c where "
            + "s.class_id=c.id and c.unit_id=?2 and s.student_id= ?1  and c.acadyear= ?3 and c.semester= ?4 and c.is_deleted=0 and s.is_deleted=0")
    List<TeachClass> findByStuIdAndAcadyearAndSemester(String studentId, String unitId,String acadyear, String semester);

    @Query(nativeQuery = true, value = "select c.* from base_teach_class_stu s,base_teach_class c where "
            + "s.class_id=c.id and s.student_id in (?1) and c.acadyear= ?2 and c.semester= ?3 and c.is_deleted=0 and s.is_deleted=0")
    List<TeachClass> findByStuIdsAndAcadyearAndSemester(String[] studentIds, String acadyear, String semester);
    
    @Query("From TeachClass where teacherId=?1 and acadyear= ?2 and semester= ?3 and isDeleted=0")
    List<TeachClass> findByTeaIdAndAcadyearAndSemester(String teacherId, String acadyear, String semester);

    @Query("From TeachClass where unitId= ?1 and acadyear= ?2 and semester= ?3 and teacherId in(?4)  and isDeleted=0")
    List<TeachClass> findByUnitIdAndAcadyearAndSemesterAndTeaIds(String unitId, String acadyear, String semester,String[] teacherIds);

    public List<TeachClass> findByUnitIdAndIsDeletedAndTeacherIdIn(String unitId, Integer isDeleted, String[] teacherIds);

    @Modifying
    @Query("update TeachClass set isUsing='0',modifyTime=sysdate where id in (?1)")
	public void notUsing(String[] ids);
    
    @Modifying
    @Query("update TeachClass set isUsing='1' where id in (?1)")
	public void yesUsing(String[] ids);
    
    @Query("From TeachClass where gradeId like ?1 " + SQL_AFTER)
    public List<TeachClass> findByLikeGradeId(String gradeId);

	 @Query(nativeQuery = true, value = "select c.* from base_teach_class_stu s,base_teach_class c where "
	            + "s.class_id=c.id and c.acadyear= ?1 and c.semester= ?2 and c.unit_id = ?3 and c.is_deleted=0 and s.is_deleted=0 and s.student_id in (?4) ")
	public List<TeachClass> findByStudentIds(String acadyear, String semester,
			String schoolId, String[] studentId);

	public List<TeachClass> findByParentId(String parentId);
	
	@Modifying
	@Query("update TeachClass set parentId = ?1 where id in (?2)")
	public void updateParentId(String id, String[] teachClassIdArr);
	
	@Modifying
	@Query("update TeachClass set parentId = ?1 where parentId= ?2")
	public void updateParentIdById(Object object, String oldTeachClassId);
	
	@Query("From TeachClass where isDeleted=0 and parentId in ?1 ")
	public List<TeachClass> findByParentIdIn(String[] parentIds);

	@Modifying
	@Query("update TeachClass set isDeleted=1,modifyTime=sysdate where id in (?1)")
	public void deleteByIds(String[] ids);
	@Modifying
	@Query("update TeachClass set isDeleted=1,modifyTime=sysdate where gradeId in (?1)")
	public void deleteByGradeIdIn(String... gradeIds);
	@Modifying
	@Query("update TeachClass set isDeleted=1,modifyTime=sysdate where courseId in (?1)")
	public void deleteByCourseIdIn(String... courseIds);

	@Query("From TeachClass where unitId in ?1 " )
	public List<TeachClass> findbyUnitIdIn(String... unitIds);

	@Query("From TeachClass where unitId=?1 and acadyear=?2 and semester=?3 and gradeId like ?4 and relaCourseId in (?5) " + SQL_AFTER)
	public List<TeachClass> findByRelaCourseIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds);

}
