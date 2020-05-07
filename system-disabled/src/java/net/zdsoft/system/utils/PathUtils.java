package net.zdsoft.system.utils;

import java.io.File;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.service.config.SysOptionService;

public class PathUtils {

	public static String getFilePath() {
		String key = "Constant.SystemIni@" + Constant.FILE_PATH;
		String path = RedisUtils.get(key);
		if (StringUtils.isBlank(path)) {
			SysOptionService sysOptionService = Evn.getBean("sysOptionService");
			path = sysOptionService.findValueByOptionCode(Constant.FILE_PATH)+File.separator+"store";
			if (StringUtils.isNotBlank(path)) {
				RedisUtils.set(key, path);
			}
		}
		return StringUtils.isBlank(path) ? "/opt/data/store" : path;
	}

}
