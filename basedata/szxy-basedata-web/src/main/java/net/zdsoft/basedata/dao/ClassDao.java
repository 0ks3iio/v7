package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ClassDao extends BaseJpaRepositoryDao<Clazz, String>, ClassJdbcDao {

	public static final String SQL_AFTER=" and isDeleted = 0 and isGraduate = 0 order by classCode";
	

	public static final String SQL_CLAZZ =" order by section ASC,acadyear DESC,classCode ASC";
	

	public static final String SQL_AFTER1=" AND isDeleted = 0 and isGraduate <> 2 ORDER BY section ASC, acadyear DESC,classCode ASC";
	

	@Query("From Clazz Where gradeId = ?1"+SQL_AFTER)
	public List<Clazz> findByGradeId(String gradeId);
	@Query("From Clazz Where gradeId = ?1 and className = ?2 and isDeleted = 0 and isGraduate <> 2")
	public List<Clazz> findClassList(String gradeId, String className);
	@Modifying
	@Query("update Clazz set isDeleted=1 ,modifyTime=sysdate where id in (?1)")
	public void updateIsDelete(String... ids);
	@Query("select max(classCode) FROM Clazz where school_id = ?1 and class_code like %?2% and length(class_code) = ?3 and isDeleted = 0 and isGraduate = 0")
	public String findMaxClassCodeByPrefix(String schoolId, String classCode, int length);
	@Query("From Clazz Where teachAreaId in (?1)"+SQL_AFTER)
	public List<Clazz> findByTeachAreaIdIn(String... teachAreaIds);
	@Query("From Clazz Where schoolId = ?1 and acadyear = ?2 and section = ?3"+SQL_AFTER)
	public List<Clazz> findClassList(String schoolId, String oepnAcadyear, int section);
	/**
	 * @param schoolId
	 * @param section
	 * @param enrollyear
	 * @param artScienceType
	 * @return 
	 */
	@Query("From Clazz Where schoolId = ?1 and section=?2 and acadyear = ?3 and artScienceType = ?4 AND isDeleted = 0 and isGraduate <> 2 ORDER BY classCode ASC")
	public List<Clazz> findByIdSectionYearType(String schoolId, int section,
			String enrollyear, String artScienceType);
	/**
	 * @param schoolId
	 * @param section
	 * @param acadyear
	 * @return 
	 */

	@Query("SELECT distinct c from Clazz as c Where c.schoolId = ?1 and c.section= ?2 and c.acadyear = ?3 and c.isGraduate = 0 AND c.isDeleted = 0 ")

	public List<Clazz> findByIdSectionYear(String schoolId, int section,
			String acadyear);
	/**
	 * @param schoolId
	 * @return
	 * 
	 */
	@Query("SELECT distinct c from Clazz as c Where c.schoolId = ?1 and c.isGraduate = 0 AND c.isDeleted = 0 ORDER BY section ASC, acadyear DESC")
	public List<Clazz> findByAllSchoolId(String schoolId);
	/**
	 * @param gradeId
	 * @return
	 * 
	 */
	@Query(" From Clazz Where gradeId = ?1"+ SQL_AFTER1)
	public List<Clazz> findByGradeIdSortAll(String gradeId);
	/**
	 * @param gradeIds
	 * @return deptId IN ?1 ORDER BY section ASC,acadyear DESC,class_code ASC
	 */
	@Query(" From Clazz Where gradeId IN ?1"+SQL_AFTER1)
	public List<Clazz> findByInGradeIds(String[] gradeIds);
	/**
	 * @param schoolId
	 * @param gradeIds
	 * @return
	 */
	@Query(" From Clazz Where schoolId=?1 and  gradeId IN ?2"+ SQL_AFTER1 )
	public List<Clazz> findBySchoolIdInGradeIds(String schoolId, String[] gradeIds);
	/**
	 * @param schoolId
	 * @param teacherId
	 * @return 
	 */
	@Query(" From Clazz Where schoolId=?1 and  (teacherId=?2 or viceTeacherId=?2) ")
	public List<Clazz> findBySchoolIdTeacherIdAll(String schoolId, String teacherId);
	
	@Query("From Clazz Where (teacherId= ?1 OR viceTeacherId= ?1) and isGraduate = 0 and isDeleted = 0" + SQL_CLAZZ)
	public List<Clazz> findByTeacherId(String teacherId);
	
	@Query("From Clazz Where (teacherId= ?1 OR viceTeacherId= ?1) and (cast(substr(?2,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer)) = schoolingLength and isGraduate <> 2 " + SQL_CLAZZ)
	public List<Clazz> findByIdAcadyear(String teacherId,String graduateAcadyear);
	
	@Query("From Clazz Where (teacherId= ?1 OR viceTeacherId= ?1) and (cast(substr(?2,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer)) = schoolingLength and isGraduate = 1 and isDeleted = 0" + SQL_CLAZZ)
	public List<Clazz> findByteacherIdAcadyear(String teacheId,String graduateAcadyear);
	
	@Query("From Clazz Where schoolId = ?1 and isDeleted = 0 and isGraduate <> 2" + SQL_CLAZZ)
	public List<Clazz> findBySchoolId(String schoolId);
	
	@Query("From Clazz Where schoolId = ?1 and acadyear= ?2 and is_deleted = 0 and isGraduate <> 2 order by section ASC,classCode ASC")
	public List<Clazz> findByOpenAcadyear(String schoolId, String openAcadyear);
	
	@Query("From Clazz Where schoolId = ?1 and gradeId= ?2 and teacherId = ?3 and is_deleted = 0 and isGraduate <> 2 order by section ASC,classCode ASC")
	public List<Clazz> findByGradeId(String schoolId, String gradeId,String teacherId);
	
	@Query("From Clazz Where schoolId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(acadyear,1,4) as integer)) >0 and (cast(substr(?2,6,9) as integer) - cast(substr(acadyear,1,4) as integer)) <= schoolingLength and isGraduate= 0 and isDeleted = 0" + SQL_CLAZZ)
	public List<Clazz> findByIdCurAcadyear(String schoolId, String curAcadyear);
	
	@Query("From Clazz Where schoolId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(acadyear,1,4) as integer)) > schoolingLength and isGraduate= 0 and isDeleted = 0" + SQL_CLAZZ)
	public List<Clazz> findByOverSchoolinglen(String schoolId,String curAcadyear);
	
	@Query("From Clazz Where schoolId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(acadyear,1,4) as integer)) = schoolingLength and isDeleted = 0 and isGraduate <> 2" + SQL_CLAZZ)
	public List<Clazz> findByGraduateyear(String schoolId,String graduateAcadyear);

	@Query("From Clazz Where campusId= ?1 and isDeleted = 0 and isGraduate = 0" + SQL_CLAZZ)
	public List<Clazz> findByCampusId(String campusId);

	@Query("From Clazz Where campusId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(acadyear,1,4) as integer)) = schoolingLength and isDeleted = 0 and isGraduate <> 2" + SQL_CLAZZ)
	public List<Clazz> findByIdYear(String campusId, String graduateAcadyear);
	
	@Modifying
	@Query("update Clazz set isGraduate = ?1,graduateDate = ?2 ,modifyTime=sysdate Where id= ?3")
	public void updateGraduateSign(int sign, Date currentDate, String classId);
	/**
	 * @param schoolId
	 * @param curAcadyear
	 * @return
	 * SELECT DISTINCT school_id, section, acadyear, schooling_length,grade_id FROM base_class "
			+ "WHERE school_id=? AND (cast(substr(?,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer)) <= schooling_length and (cast(substr(?,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer))>0  AND is_deleted = 0 "
			+ "ORDER BY section ASC, acadyear DESC";
	 */
	@Query("SELECT DISTINCT c From Clazz as c Where c.schoolId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(c.acadyear,1,4) as integer)) <= c.schoolingLength and (cast(substr(?2,6,9) as integer) - cast(substr(c.acadyear,1,4) as integer)) >0 and c.isDeleted = 0 and isGraduate <> 2 ORDER BY section ASC, acadyear DESC, classCode ASC" )
	public List<Clazz> findBySchoolIdCurAcadyear(String schoolId, String curAcadyear);
	/**
	 * @param schoolId
	 * @param graduateAcadyear
	 * @return
	 * "SELECT DISTINCT school_id, section, acadyear, schooling_length,grade_id FROM base_class "
			+ "WHERE school_id=? AND (cast(substr(?,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer)) = schooling_length AND is_deleted = 0 "
			+ "ORDER BY section ASC, acadyear DESC";
	 */
	@Query("SELECT DISTINCT c From Clazz as c Where c.schoolId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(c.acadyear,1,4) as integer)) = c.schoolingLength  and c.isDeleted = 0 and isGraduate <> 2 ORDER BY section ASC, acadyear DESC, classCode ASC" )
	public List<Clazz> findBySchoolIdGraduateAcadyear(String schoolId,
			String graduateAcadyear);
	/**
	 * @param schoolId
	 * @param acadyear
	 * @return"SELECT DISTINCT school_id, section, acadyear, schooling_length,grade_id FROM base_class "
			+ "WHERE school_id=? AND (cast(substr(?,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer)) <= schooling_length and (cast(substr(?,6,9) AS integer) - cast(substr(acadyear,1,4) AS integer))>0  AND is_deleted = 0 "
			+ "ORDER BY section ASC, acadyear DESC";
	 */
	@Query("SELECT DISTINCT c From Clazz as c Where c.schoolId= ?1 and (cast(substr(?2,6,9) as integer) - cast(substr(c.acadyear,1,4) as integer)) <= c.schoolingLength  and (cast(substr(?2,6,9) as integer) - cast(substr(c.acadyear,1,4) as integer)) >0 and c.isDeleted = 0 and isGraduate <> 2 ORDER BY section ASC, acadyear DESC" )	
	public List<Clazz> findBySchoolIdAcadyear(String schoolId, String acadyear);
	/**
	 * @param schoolId
	 * @param teacherId
	 * @return SELECT DISTINCT school_id, section, acadyear, schooling_length,grade_id FROM base_class "
			+ "WHERE school_id=? AND (teacher_id=? or vice_teacher_id=?) AND is_graduate=0 AND is_deleted = 0 "
			+ "ORDER BY section ASC, acadyear DESC";
	 */
	@Query(" SELECT DISTINCT c From Clazz as c  Where c.schoolId=?1 and  (c.teacherId=?2 or c.viceTeacherId=?2) AND c.isGraduate=0 AND c.isDeleted = 0 ORDER BY c.section ASC, c.acadyear DESC")	
	public List<Clazz> findBySchoolIdTeacherId(String schoolId, String teacherId);
	/**
	 * @param schoolId
	 * @param section
	 * @param enrollyear
	 * @param sl
	 * @return 
	 */
	@Query(" From Clazz Where schoolId=?1 AND section=?2 AND acadyear=?3 AND schoolingLength like ?4  AND isGraduate=0 "+ SQL_AFTER1 )
	public List<Clazz> findClassesByGrade(String schoolId, int section,
			String enrollyear, String sl);
	/**
	 * @param schoolId
	 * @param campusId
	 * @param section
	 * @param enrollyear
	 * @param schoolingLen
	 * @return SELECT * FROM base_class "
			+ "WHERE school_id=? AND campus_id=? AND section=? AND acadyear=? AND schooling_length=? AND is_deleted = 0 AND is_graduate=0 "
			+ " ORDER BY section ASC,acadyear DESC,class_code ASC"
	 */
	@Query(" From Clazz Where schoolId=?1 AND campusId=?2 AND section=?3 AND acadyear=?4 AND schoolingLength = ?5  AND isGraduate=0 "+ SQL_AFTER1 )
	public List<Clazz> findClassesByGrade(String schoolId, String campusId,
			int section, String enrollyear, int schoolingLen);
	/**
	 * @param schoolId
	 * @param gradeId
	 * @return "SELECT * FROM base_class "
			+ "WHERE school_id=? AND grade_id=? AND is_deleted = 0 AND is_graduate=0 "
			+ " ORDER BY section ASC,acadyear DESC,class_code ASC";
	 */
	@Query(" From Clazz Where schoolId=?1 AND gradeId=?2 AND isGraduate=0 "+ SQL_AFTER1 )
	public List<Clazz> findBySchoolIdGradeId(String schoolId, String gradeId);
	/**
	 * @param schoolId
	 * @param section
	 * @param enrollyear
	 * @param schoolingLen
	 * @param artScienceType
	 * @return "SELECT * FROM base_class "
			+ "WHERE school_id=? AND section=? AND acadyear=? AND schooling_length=? AND art_science_type=? AND is_deleted = 0 "
			+ "ORDER BY section ASC,acadyear DESC,class_code ASC"
	 */
	@Query(" From Clazz Where schoolId=?1 AND section=?2 AND acadyear=?3 AND schoolingLength=?4 AND artScienceType = ?5  "+ SQL_AFTER1 )
	public List<Clazz> findClassesByKinClass(String schoolId, int section,
			String enrollyear, int schoolingLen, String artScienceType);
	
	@Query(" From Clazz Where id in (?1) "+ SQL_AFTER )
	public List<Clazz> findClassListByIds(String[] ids);

	@Query(" From Clazz Where id in (?1) "+ SQL_AFTER1 )
	public List<Clazz> findByIdsSort(String[] classIds);
	
	@Query("From Clazz Where schoolId in ?1 AND isGraduate=0 " + SQL_AFTER1 )
	public List<Clazz> findBySchoolIdIn(String[] schoolIds);

	@Modifying
	@Query(value = "update Clazz set isDeleted=1,modifyTime=sysdate where schoolId=?1")
	void deleteClazzBySchoolId(String unitId);
	/**
	 * 获取 指定场地的 行政班
	 * @param placeIds
	 * @return 
	 */
	@Query("From Clazz Where schoolId = ?1 and teachPlaceId in (?2) AND isGraduate=0 " + SQL_AFTER1 )
	public List<Clazz> findByPlaceIds(String school, String[] placeIds);
	
	@Query("From Clazz Where schoolId in ?1 ")
	public List<Clazz> findAllBySchoolIdIn(String[] schoolIds);
}
