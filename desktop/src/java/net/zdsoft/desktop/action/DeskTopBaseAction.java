package net.zdsoft.desktop.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.entity.UserSet;
import net.zdsoft.desktop.service.UserSetService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;


/**
 * @author shenke
 * @since 2016/12/27 18:39
 */
public abstract  class DeskTopBaseAction extends BaseAction{

    protected static final Logger LOG = LoggerFactory.getLogger(DeskTopBaseAction.class);

	private static String fileUrl;
	private static String fileSecondUrl;
	private Map<String, Boolean> secondUrlCache = new ConcurrentHashMap<String, Boolean>();
	
    @Autowired protected UserRemoteService userRemoteService;
    @Autowired protected SysOptionRemoteService sysOptionRemoteService;
    @Autowired protected UserSetService userSetService;
    @Autowired protected FamilyRemoteService familyRemoteService;

    protected String getUserId(){
        return getLoginInfo().getUserId();
    }

    @SuppressWarnings("unchecked")
	protected <T> T getDubboService(String serviceId){
        return (T)Evn.getBean(serviceId);
    }

    protected boolean isFunctionAreaUserSetOpen(){
        return BooleanUtils.toBoolean(StringUtils.trim(sysOptionRemoteService.findValue(DeskTopConstant.FUNCTION_AREA_USER_SET_OPEN)));
    }

    protected String getUserSetLayout(){
        if (isFunctionAreaUserSetOpen()) {
            UserSet userSet = userSetService.findByUserId(getUserId());
            return userSet != null?userSet.getLayout():UserSet.LAYOUT_TWO2ONE;
        }
        return StringUtils.trim(sysOptionRemoteService.findValue(DeskTopConstant.DESKTOP_LAYOUT));
    }

    /**
     * if integer == null return 0
     * @param integer
     * @return
     */
    protected int getIntValue(Integer integer){

        return getIntValue(integer, 0);
    }

    protected int getIntValue(Integer integer, int defaultValue) {
        if ( integer != null ) {
            return integer.intValue();
        }
        return defaultValue;
    }
    
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
