package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.remote.service.LoginDomainRemoteService;
import net.zdsoft.system.service.LoginDomainService;

@Service("loginDomainRemoteService")
public class LoginDomainRemoteServiceImpl extends BaseRemoteServiceImpl<LoginDomain, String> implements LoginDomainRemoteService {
	
	@Autowired
	private LoginDomainService loginDomainService;

	@Override
	protected BaseService<LoginDomain, String> getBaseService() {
		return loginDomainService;
	}

	@Override
	public String findByRegionAdmin(String regionAdmin) {
		return SUtils.s(loginDomainService.findByRegionAdmin(regionAdmin));
	}


}
