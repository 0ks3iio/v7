package net.zdsoft.officework.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.officework.entity.OfficeHealthSleep;

public interface OfficeHealthSleepDao extends BaseJpaRepositoryDao<OfficeHealthSleep, String>{

	@Query("From OfficeHealthSleep where to_char(uploadTime, 'yyyy-MM-dd') in ?1 and ownerId in ?2")
	public List<OfficeHealthSleep> findByDateOwnerIds(String[] dateDays,
			String[] ownerIds);

	
}
	
