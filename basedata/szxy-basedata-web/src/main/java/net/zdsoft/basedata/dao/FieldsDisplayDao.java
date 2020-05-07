package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.FieldsDisplay;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FieldsDisplayDao extends BaseJpaRepositoryDao<FieldsDisplay, String>{

	@Query("from FieldsDisplay where unitId = ?1 and colsType = ?2 and colsUse = ?3 order by parentId desc,colsOrder")
	List<FieldsDisplay> findByColsDisplays(String unitId, String type,Integer colsUse);
	
	@Query("from FieldsDisplay where unitId = ?1 and colsType = ?2 and colsUse <> -1 order by parentId,colsOrder")
	List<FieldsDisplay> findByColsDisplays(String unitId, String type);

	@Query("from FieldsDisplay where parentId = ?1 and colsUse = ?2 order by colsOrder")
	List<FieldsDisplay> findByColsDisplays(String parentId, Integer colsUse);
	
}
