package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.service.config.SystemIniService;

@Service("systemIniRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SystemIniRemoteServiceImpl implements SystemIniRemoteService {

    @Autowired
    private SystemIniService systemIniService;

    @Override
    public String findValue(String code) {
        return systemIniService.findValue(code);
    }

	@Override
	public void updateNowvalue(String code, String nowValue) {
		systemIniService.updateNowvalue(nowValue,code);
	}

	@Override
	public void doRefreshCache(String... iniid) {
		systemIniService.doRefreshCache(iniid);
	}





}
