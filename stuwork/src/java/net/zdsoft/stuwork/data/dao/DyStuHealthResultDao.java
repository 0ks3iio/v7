package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;

public interface DyStuHealthResultDao extends BaseJpaRepositoryDao<DyStuHealthResult, String>{
	String SQL_AFTER=" Order By orderId asc";
	/**
	 * @param unitId
	 * @param studentId
	 * @return
	 */
	@Query("from DyStuHealthResult where unitId = ?1 and  studentId = ?2 "+SQL_AFTER)
	List<DyStuHealthResult> findByUnitIdAndStuId(String unitId, String studentId);

	@Query("From DyStuHealthResult  where unitId = ?1 and acadyear = ?2 and semester =?3")
	public List<DyStuHealthResult> findByUidAndSe(String unitId,String acadyear,String semester);
	
	@Query("From DyStuHealthResult  where unitId = ?1 and acadyear = ?2 and semester =?3 and studentId = ?4")
	public List<DyStuHealthResult> findOneByStudnetId(String unitId, String acadyear, String semester, String studentId);
	
	@Query("From DyStuHealthResult  where unitId = ?1 and acadyear = ?2 and semester =?3 and studentId in (?4)")
	public List<DyStuHealthResult> findListByStudentIds(String unitId, String acadyear, String semester, String[] studentIds);
}
