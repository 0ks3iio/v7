package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.framework.entity.Pagination;


/**
 * eclasscard_class_attance 
 * @author 
 * 
 */
public interface EccClassAttenceJdbcDao {
	public List<EccClassAttence> findByClassIdsSection(String unitId,String date,
			int section ,String[] classIds,Pagination page);
	
	public List<EccClassAttence> findByIdsSort(String[] caIds,String beginDate,String endDate,Pagination page);
}
