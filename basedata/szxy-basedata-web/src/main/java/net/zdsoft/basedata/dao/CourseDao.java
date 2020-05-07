package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface CourseDao extends BaseJpaRepositoryDao<Course, String>,CourseJdbcDao{
	
	public List<Course> findByIsDeletedAndUnitIdInOrderBySubjectTypeAscOrderIdAsc(int isDeleted,String[] unitIds);
	
	
	public List<Course> findByIsDeletedAndUnitIdInAndSubjectName(Integer isDeleted, String[] unitIds,
			String subjectName);
	
	@Query("From Course where isDeleted = 0 and subjectCode in (?1) ORDER BY subjectType,orderId")
	public List<Course> findBySubjectCodeIn(String[] subjectCodes);
	
	@Query(value="select max(orderId) from Course where isDeleted=0 and unitId in (?1)") 
	public int findMaxOrderId(String[] unitids);
	
	@Modifying
	@Query("update Course set isDeleted=1 where id in (?1)")
	public void updateIsDeleteds(String[] ids);


	public List<Course> findByIsDeletedAndIsUsingAndUnitIdInOrderBySubjectTypeAscOrderIdAsc(int isDeleted,int isUsing,String[] unitIds);
	

	/**done 2
	 * @param codes
	 * @return
	 */
	@Query("From Course where isDeleted = 0 and subjectCode in (?1) and isUsing = 1 ORDER BY subjectType,orderId")
	public List<Course> findByBaseCourseCodes(String... codes);

	/**
	 * @param unitId
	 * @param isUsing
	 * @param likeName
	 * @return
	 */
	@Query("From Course where isDeleted = 0 and unitId = ?1 and isUsing =?2 and subjectName like ?3 and type=1")
	public List<Course> findBySubjectUnitIdIsUsingName(String unitId, int isUsing,
			String likeName);


	/**
	 * @param unitId
	 * @param likeName
	 * @return
	 */
	@Query("From Course where isDeleted = 0 and unitId = ?1 and subjectName like ?2 and type=1")
	public List<Course> findBySubjectUnitIdName(String unitId, String likeName);


	/**
	 * @param subids
	 * @return
	 */
	@Query("From Course where id in ?1 ORDER BY subjectType,orderId ")
	public List<Course> findBySubjectIdIn(String[] subids);
	
	/**
	 * 根据Code，检索多个单位下的科目信息
	 * @param unitIds
	 * @param codes
	 * @return
	 */
	@Query("From Course where isDeleted = 0 and isUsing = 1 and unitId IN ?1 and subjectCode IN ?2 ORDER BY subjectType,orderId")
	public List<Course> findByBaseCourseCodes(String[] unitIds, String... codes);
	
	/**
	 * 检索本单位适用的科目信息（学校的话，先检索顶级单位设置的科目+学校自己设置的科目）
	 * @param unitId
	 * @param codes
	 * @return
	 */
	@Query("From Course where isDeleted = 0 and isUsing = 1 and unitId = ?1 and subjectCode IN ?2 ORDER BY subjectType,orderId ")
	public List<Course> findByUnitCourseCodes(String unitId, String... codes);

	public List<Course> findByIsDeletedAndUnitIdAndCourseTypeIdIn(
			int isDeleted, String unitId, String[] courseTypeIds);

	/**
	 * 根据单位Id 和课程码来查询课程
	 * @param unitIds
	 * @param code
	 * @return
	 */
	public List<Course> findByIsDeletedAndUnitIdInAndSubjectCode(int isDeleted, String[] unitIds,
			String code);

	@Query("From Course where isDeleted = 0 and isUsing = 1 and unitId in (?1) and subjectName in (?2) ORDER BY subjectType,orderId ")
	public List<Course>  findByUnitIdAndSubjecctNameIn(String[] unitId, String[] subNames);
	
	@Query("select id,subjectName From Course Where id in (?1) ORDER BY orderId")
	public List<Object[]> findPartCouByIds(String[] ids);
	
	@Query("From Course where isDeleted = 0 and isUsing = 1 and unitId in (?1)  ORDER BY subjectType,orderId")
	public List<Course>  findByUnitIdsOrderId(String[] unitId);
	
	@Query("From Course where isDeleted = 0 and isUsing = 1 and unitId in (?1)  and type=?2 and section like ?3 ORDER BY orderId")
	public List<Course>  findByUnitIdsAndTypeAndLikeSection(String[] unitIds, String type, String section);

	@Query("From Course where isDeleted = 0 and unitId in ?1 ")
	public List<Course> getCourseByUnitIdIn(String... unitId);
}
