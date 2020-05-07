package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;

public interface DyStuHealthProjectItemDao extends BaseJpaRepositoryDao<DyStuHealthProjectItem, String>{
	String SQL_AFTER=" Order By orderId asc";
	
	
	@Query("From DyStuHealthProjectItem where unitId = ?1 and itemId = ?2")
	public List<DyStuHealthProjectItem> findByItemId(String unitId,String itemId);
	
	@Query("From DyStuHealthProjectItem where unitId = ?1 and acadyear = ?2 and semester = ?3 "+SQL_AFTER)
	public List<DyStuHealthProjectItem> findBySemester(String unitId,String acadyear,String semester);
	
	@Transactional
	@Modifying
	@Query("Delete From DyStuHealthProjectItem where unitId = ?1 and acadyear = ?2 and semester = ?3")
	public void deleteBySemester(String unitId,String acadyear,String semester);
	/**
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	@Query("From DyStuHealthProjectItem  Where unitId = ?1 and acadyear = ?2 and semester = ?3 "+SQL_AFTER)
	List<DyStuHealthProjectItem> findByUIdAndSemester(String unitId, String acadyear, String semester);




}
