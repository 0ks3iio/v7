package net.zdsoft.officework.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.officework.entity.OfficeHealthDevice;

public interface OfficeHealthDeviceDao extends BaseJpaRepositoryDao<OfficeHealthDevice, String>{

	@Query("From OfficeHealthDevice Where serialNumber = ?1")
	public OfficeHealthDevice findBySerialNumber(String serialNumber);
	
	@Query("From OfficeHealthDevice Where type = ?1")
	public List<OfficeHealthDevice> findByType(String type);
	
	@Query("From OfficeHealthDevice Where unitId = ?1 and type = ?2")
	public List<OfficeHealthDevice> getByUnitAndType(String unitId,String type);
	
	
	
}
	
