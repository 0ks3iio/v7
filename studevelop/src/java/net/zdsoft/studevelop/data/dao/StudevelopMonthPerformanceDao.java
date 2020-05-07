package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopMonthPerformance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 * @author weixh
 * @since 2017-7-20 下午5:18:49
 */
public interface StudevelopMonthPerformanceDao extends
		BaseJpaRepositoryDao<StudevelopMonthPerformance, String> {

		@Modifying
		@Query(nativeQuery = true,value = "Delete from studevelop_month_performance where unit_Id = ?1 and acadyear = ?2 and semester = ?3 and  perform_month = ?4  and item_id =?5 and student_Id in ?6 ")
		public void deleteByStudentIds(String unitId,String acadyear ,int semester, int performanceMouth ,String itemId ,String[] studentIds );

		@Modifying
		@Query(nativeQuery = true , value = "Delete from studevelop_month_performance where unit_Id = ?1 and  result_id = ?2")
		public void deleteByItemCodeId(String unitId,String itemcodeId);
		@Modifying
		@Query(nativeQuery = true , value = "Delete from studevelop_month_performance where unit_Id = ?1 and  item_id = ?2")
		public void deleteByItemId(String unitId ,String itemId);
		@Query(nativeQuery = true,value = "select * from studevelop_month_performance where unit_Id = ?1 and acadyear = ?2 and semester = ?3 and  perform_month = ?4 and student_id = ?5 and item_id = ?6  ")
		public List<StudevelopMonthPerformance> getMonthPermanceByStuId(String unitId,String acadyear ,int semester, int performanceMouth ,String studentId  ,String itemId);

	  @Query(nativeQuery = true,value = "select * from studevelop_month_performance where unit_Id = ?1 and acadyear = ?2 and semester = ?3  and student_id = ?4  ")
	 public List<StudevelopMonthPerformance>  getMonthPermanceListByStuId(String unitId,String acadyear ,int semester, String studentId  );

	    @Query(nativeQuery = true,value = "select * from studevelop_month_performance where unit_Id = ?1 and acadyear = ?2 and semester = ?3 and  perform_month = ?4 and item_id = ?5 and student_id in ?6  ")
	    public List<StudevelopMonthPerformance> getMonthPermanceListByStuIds(String unitId, String acadyear , int semester, int performanceMouth  , String itemId , String[] studentIds);
	@Query(nativeQuery = true,value = "select * from studevelop_month_performance where unit_Id = ?1 and acadyear = ?2 and semester = ?3 and  perform_month = ?4 and item_id in ?5 ")
	    public List<StudevelopMonthPerformance> getStudevelopMonthPerformanceByItemIds(String unitId, String acadyear , int semester, int performanceMouth  , String[] itemIds );
}
