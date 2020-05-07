package net.zdsoft.system.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.config.SysOption;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.service.config.SysOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysOptionRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SysOptionRemoteServiceImpl extends BaseRemoteServiceImpl<SysOption, String> implements
        SysOptionRemoteService {

    @Autowired
    private SysOptionService sysOptionService;
    
    
    @Override
	public SysOption findValue(String optionCode, SysOption defaultOption) {
		return sysOptionService.findOneByOptionCode(optionCode, defaultOption);
	}

	@Override
    public String findValue(String optionCode) {
        return sysOptionService.findValueByOptionCode(optionCode);
    }

    @Override
    public boolean isSecondUrl(String currentServerName) {
        return sysOptionService.isSecondUrl(currentServerName);
    }

    @Override
    public String getFileUrl(String serverName) {
        return sysOptionService.getFileUrl(serverName);
    }

    @Override
    public String getIndexUrl(String serverName) {
        return sysOptionService.getIndexUrl(serverName);
    }

    @Override
    protected BaseService<SysOption, String> getBaseService() {
        return sysOptionService;
    }

}
