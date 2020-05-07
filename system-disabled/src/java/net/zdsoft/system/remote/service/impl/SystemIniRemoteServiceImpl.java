package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.service.config.SystemIniService;

@Service("systemIniRemoteService")
public class SystemIniRemoteServiceImpl implements SystemIniRemoteService {

    @Autowired
    private SystemIniService systemIniService;

    @Override
    public String findValue(String code) {
        return systemIniService.findValue(code);
    }





}
