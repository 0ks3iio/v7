package net.zdsoft.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.LoginDomain;

public interface LoginDomainDao extends BaseJpaRepositoryDao<LoginDomain, String>{
    
	public static final String SQL_AFTER=" and isDeleted = 0 ";
	
	@Query("From LoginDomain Where unitId in ?1" + SQL_AFTER)
	List<LoginDomain> findByUnitIdIn(String... unitIds);

	@Modifying
	@Query("update LoginDomain set isDeleted = 1  where unitId = ?1")
	void deleteByUnitId(String unitId);

	@Query("From LoginDomain Where regionAdmin = ?1" + SQL_AFTER)
	LoginDomain findByRegionAdmin(String domainName);

}
