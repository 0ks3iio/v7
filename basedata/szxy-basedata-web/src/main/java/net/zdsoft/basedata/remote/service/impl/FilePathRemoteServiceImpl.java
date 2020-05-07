package net.zdsoft.basedata.remote.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.service.config.SysOptionService;

@Service("filePathRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class FilePathRemoteServiceImpl implements FilePathRemoteService {

	@Autowired
	private SysOptionService sysOptionService;

	@Override
	public String getFilePath() {
		String key = "Constant.SystemIni@" + Constant.FILE_PATH;
		String path = RedisUtils.get(key);
		if (StringUtils.isBlank(path)) {
			path = sysOptionService.findValueByOptionCode(Constant.FILE_PATH) + File.separator + "store";
			if (StringUtils.isNotBlank(path)) {
				RedisUtils.set(key, path);
			}
		}
		return StringUtils.isBlank(path) ? "/opt/data/store" : path;
	}

}
