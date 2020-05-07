package net.zdsoft.desktop;

import static net.zdsoft.system.constant.Constant.FILE_SECOND_URL;
import static net.zdsoft.system.constant.Constant.FILE_URL;
import static net.zdsoft.system.constant.Constant.INDEX_SECOND_URL;
import static net.zdsoft.system.constant.Constant.INDEX_URL;
import static net.zdsoft.system.constant.Constant.PASSPORT_SECOND_URL;
import static net.zdsoft.system.constant.Constant.PASSPORT_URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * @author ke_shen@126.com
 * @since 2018/1/26 下午4:28
 */
public abstract class AbstractDesktopController extends AbstractController implements InitializingBean {

	@Autowired
	protected SysOptionRemoteService sysOptionRemoteService;

	protected String indexUrl;
	protected String indexSecondUrl;
	protected String passportUrl;
	protected String passportSecondUrl;
	protected String fileUrl;
	protected String fileSecondUrl;

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			initBean();
		} catch (Exception e) {
			logger().error("初始化baseSysOption参数(fileUrl,passportUrl,indexUrl)失败 {}", e.getMessage());
		}
	}


	protected void initBean() {
		indexUrl 			= sysOptionRemoteService.findValue(INDEX_URL);
		indexSecondUrl 		= sysOptionRemoteService.findValue(INDEX_SECOND_URL);
		fileUrl				= sysOptionRemoteService.findValue(FILE_URL);
		fileSecondUrl		= sysOptionRemoteService.findValue(FILE_SECOND_URL);
		passportUrl			= sysOptionRemoteService.findValue(PASSPORT_URL);
		passportSecondUrl	= sysOptionRemoteService.findValue(PASSPORT_SECOND_URL);
	}

	private Logger logger() {
		Logger logger = getLogger();
		return logger != null ? logger : LoggerFactory.getLogger(this.getClass());
	}

	protected abstract Logger getLogger();

	/**
	 * 判断请求是否来自内网
	 * true 内网
	 */
	protected boolean innerRequest() {
		return sysOptionRemoteService.isSecondUrl(getRequest().getServerName());
	}
}
