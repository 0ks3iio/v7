package net.zdsoft.teaeaxam.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

public class TeaExamBaseAction extends BaseAction {

	private static String fileUrl;
	private static String fileSecondUrl;
	private Map<String, Boolean> secondUrlCache = new ConcurrentHashMap<String, Boolean>();

	@Autowired
	protected SysOptionRemoteService sysOptionRemoteService;

	protected String getFileURL() {
		HttpServletRequest request = Evn.getRequest();
		Boolean isSecondUrl;
		if (request != null) {
			if ((isSecondUrl = secondUrlCache.get(request.getServerName())) == null) {
				isSecondUrl = sysOptionRemoteService.isSecondUrl(request.getServerName());
				secondUrlCache.putIfAbsent(request.getServerName(), isSecondUrl);
			}
		} else {
			isSecondUrl = false;
		}
		if (isSecondUrl) {
			if (StringUtils.isBlank(fileSecondUrl)) {
				fileSecondUrl = sysOptionRemoteService.findValue(Constant.FILE_SECOND_URL);
			}
			return StringUtils.isBlank(fileSecondUrl) ? fileUrl : fileSecondUrl;
		} else {
			if (StringUtils.isBlank(fileUrl)) {
				fileUrl = sysOptionRemoteService.findValue(Constant.FILE_URL);
			}
			return fileUrl;
		}
	}
}
