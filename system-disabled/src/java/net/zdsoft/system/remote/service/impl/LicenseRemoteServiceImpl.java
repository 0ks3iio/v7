package net.zdsoft.system.remote.service.impl;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.license.service.LicenseService;
import net.zdsoft.system.remote.service.LicenseRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke date 2017/11/13下午5:35
 */
@Service
public class LicenseRemoteServiceImpl implements LicenseRemoteService {
	@Autowired
	private LicenseService licenseService;
	@Override
	public String getLicenseInfo() {
		return SUtils.s(licenseService.getLicense());
	}
}
