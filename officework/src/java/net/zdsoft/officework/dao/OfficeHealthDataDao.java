package net.zdsoft.officework.dao;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.officework.entity.OfficeHealthData;

public interface OfficeHealthDataDao extends BaseJpaRepositoryDao<OfficeHealthData, String>{

	@Transactional
	@Modifying
    @Query("delete from OfficeHealthData Where to_char(uploadTime,'yyyy-MM-dd') <= ?1 and type in ('50','51','01')")
	public void deleteOldData(String dateTime);
	
	
	
}
	
