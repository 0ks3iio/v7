package net.zdsoft.newstusys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newstusys.entity.StusysColsDisplay;

/**
 * 
 * @author weixh
 * 2019年9月3日	
 */
public interface StusysColsDisplayDao extends BaseJpaRepositoryDao<StusysColsDisplay, String> {

	@Query("FROM StusysColsDisplay WHERE unitId = ?1 and colsType = ?2 ORDER BY colsOrder")
	public List<StusysColsDisplay> findByUnitIdType(String unitId, String type);
	
	@Modifying
	@Query("DELETE FROM StusysColsDisplay WHERE unitId = ?1 and colsType = ?2")
	public void deleteByUnitIdType(String unitId, String type);
}
