package net.zdsoft.comprehensive.dao;

import net.zdsoft.comprehensive.entity.CompreStatistics;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompreStatisticsDao extends BaseJpaRepositoryDao<CompreStatistics, String>{

	@Query("from CompreStatistics where studentId=?1 and acadyear in (?2)")
	List<CompreStatistics> findByStudentIdAcadyear(String studentId,
			String[] acadyear);
	@Query("from CompreStatistics where unitId=?1 and acadyear in (?2)")
	List<CompreStatistics> findByUnitIdAcadyear(String unitId, String[] acadyear);
	
	//分割线，以上为原有方法，方便后续删除
	@Query("from CompreStatistics where unitId=?1 and acadyear =?2 and semester=?3 and type=?4 and studentId in (?5) ")
	List<CompreStatistics> findByStudentIdType(String unitId, String acadyear,
			String semester, String type, String[] studentId);
	@Query("from CompreStatistics where unitId=?1 and acadyear =?2 and semester=?3 and studentId in (?4) ")
	List<CompreStatistics> findByStudentId(String unitId, String acadyear,
			String semester,String[] studentId);
	@Modifying
	@Query("delete from CompreStatistics where unitId=?1 and acadyear =?2 and semester=?3 and type in (?4) and studentId in (?5) ")
	public void deleteByStudentId(String unitId, String acadyear, String semester,String[] delType,
			String[] stuId);
	@Query("from CompreStatistics where studentId in (?1) and acadyear in (?2)")
	List<CompreStatistics> findByStudentIdsAcadyear(String[] studentIds, String[] acadyear);
	@Query("from CompreStatistics where studentId in (?1) ")
	List<CompreStatistics> findByStudentIds(String[] studentIds);
	
	List<CompreStatistics> findByAcadyearAndSemesterAndStudentIdIn(String acadyear, String semester, String[] studentIds);

    List<CompreStatistics> findByTypeAndAcadyearInAndStudentIdIn(String type, String[] acadyear, String[] studentIds);
}
