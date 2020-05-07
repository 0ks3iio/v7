package net.zdsoft.system.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.LoginDomain;

public interface LoginDomainService  extends BaseService<LoginDomain, String>{

	List<LoginDomain> findByUnitIdIn(String... unitIds);

	void deleteByUnitId(String unitId);

	LoginDomain findByRegionAdmin(String domainName);

}
