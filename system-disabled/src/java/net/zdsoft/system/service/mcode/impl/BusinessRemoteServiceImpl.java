package net.zdsoft.system.service.mcode.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.mcode.BusinessRemoteService;

public class BusinessRemoteServiceImpl implements BusinessRemoteService {
	@Autowired
	private SysOptionService sysOptionService;

	public String getFilePath() {
		String path = RedisUtils.get("Constant.SystemIni@" + Constant.FILE_PATH);
		if (StringUtils.isBlank(path)) {
			path = sysOptionService.findValueByOptionCode(Constant.FILE_PATH);
			if (StringUtils.isNotBlank(path))
				RedisUtils.set("Constant.SystemIni@" + Constant.FILE_PATH, path);
		}
		return path;
	}
}
