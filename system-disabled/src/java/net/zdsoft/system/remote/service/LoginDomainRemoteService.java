package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.LoginDomain;

public interface LoginDomainRemoteService extends BaseRemoteService<LoginDomain, String>{

	String findByRegionAdmin(String regionAdmin);

	
}
