package net.zdsoft.system.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.LoginDomainDao;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.service.LoginDomainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginDomainService")
public class LoginDomainServiceImpl extends BaseServiceImpl<LoginDomain, String> implements LoginDomainService {
	
	@Autowired
    private LoginDomainDao loginDomainDao;

	@Override
	protected BaseJpaRepositoryDao<LoginDomain, String> getJpaDao() {
		return loginDomainDao;
	}

	@Override
	protected Class<LoginDomain> getEntityClass() {
		return LoginDomain.class;
	}

	@Override
	public List<LoginDomain> findByUnitIdIn(String... unitIds) {
		return loginDomainDao.findByUnitIdIn(unitIds);
	}

	@Override
	public void deleteByUnitId(String unitId) {
		loginDomainDao.deleteByUnitId(unitId);
	}

	@Override
	public LoginDomain findByRegionAdmin(String domainName) {
		return loginDomainDao.findByRegionAdmin(domainName);
	}

}
