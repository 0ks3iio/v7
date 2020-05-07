package net.zdsoft.desktop.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * 如非特殊，强烈不建议参考使用此处代码
 * @author shenke
 * @since 2017.05.18
 */
public class Utils {

    /** table is sys_option */
    private static final String GET_FROM_SYS_OPTION = "SELECT * FROM SYS_OPTION WHERE INIID = ";
    private static final String GET_VALUE_CODE = "NOWVALUE";
    private static SessionFactory sessionFactory;
    private static SystemIniRemoteService systemIniRemoteService;
    //public static String getValueFromSysOption(String inId) {
	//
    //    try {
    //        if ( systemIniRemoteService != null ) {
    //            return systemIniRemoteService.findValue(inId);
    //        }
    //        systemIniRemoteService = Evn.getBean("systemIniRemoteService");
    //        return systemIniRemoteService.findValue(inId);
    //    } catch (HibernateException e) {
    //        return null;
    //    } finally {
    //    }
    //}

    /**
     * this session is not close auto
     * return
     * @return
     */
    public static Session openSession () {
        return getSessionFactory().openSession();
    }

    public static SessionFactory getSessionFactory () {
        if (sessionFactory == null) {
            EntityManager entityManager = Evn.getApplication().getBean(EntityManager.class);
            sessionFactory = ((Session)(entityManager.getDelegate())).getSessionFactory();
        }
        return sessionFactory;
    }

    public static String getPrefix(HttpServletRequest request) {
        return request.getScheme() //当前链接使用的协议
                +"://" + request.getServerName()//服务器地址
                + ":" + request.getServerPort() //端口号
                + request.getContextPath();
    }

    /**
     * 拼接5.0 6.0 模块URL<br>
     *
     * 跳转passport会出现参数丢失的问题
     * 这里和5 6 做了特殊的约定处理模块以及子系统的URL参数问题
     * @param modelUrl  sys_model 中的URL
     * @return
     */
    public static String getFinalModelUrl(String modelUrl, String serverUrl, String version,
                                           String userName,String subSystemId) {
        if (StringUtils.startsWithAny(modelUrl, Protocol.HTTP.getValue(), Protocol.HTTPS.getValue())) {
            String mask = StringUtils.contains(modelUrl, "?") ? "&" : "?";
            return modelUrl + mask;
        }

        String protocolModelUrl = UrlUtils.ignoreLastRightSlash(serverUrl) +
                UrlUtils.SEPARATOR_SIGN + UrlUtils.ignoreFirstLeftSlash(modelUrl);
        //7.0子系统
        if ( Version._7.getVersion().equals(version) ) {
        	//7.0不是div加载加sessionId(具体值页面上给定) ckjid=sessionId
        	protocolModelUrl=protocolModelUrl.trim();
        	//由于页面session去掉 增加sessionId
      		if(protocolModelUrl.indexOf("?")>-1) {
      			protocolModelUrl=protocolModelUrl+"&ckjid="+Evn.getRequest().getRequestedSessionId();
      		}else {
      			protocolModelUrl=protocolModelUrl+"?ckjid="+Evn.getRequest().getRequestedSessionId();
      		}
      		return protocolModelUrl;
        }
           
        //6.0 5.0处理
        else {
            String paramAndProtocolUrl = null;
            if ( StringUtils.contains(protocolModelUrl, UrlUtils.QUESTION_MARK) ) {
                String modelNoParameterUrl = protocolModelUrl.substring(0, protocolModelUrl.indexOf("?"));
                Map<String, String> modelUrlParameters = UrlUtils.getParameters(protocolModelUrl);
                boolean isFirst = true;
                List<String> keyList = new ArrayList<>(modelUrlParameters.size());
                List<String> valList = new ArrayList<>(modelUrlParameters.size());
                for (Map.Entry<String, String> entry : modelUrlParameters.entrySet()) {
                    keyList.add( isFirst ? entry.getKey() : "{" + entry.getKey() + "}");
                    valList.add(entry.getValue());
                    isFirst = false;
                }
                keyList.add("{appId}");
                valList.add(subSystemId);

                paramAndProtocolUrl = UrlUtils.addQueryString(modelNoParameterUrl, keyList.toArray(new String[keyList.size()]),
                        valList.toArray(new String[valList.size()]));
            } else {
                paramAndProtocolUrl = UrlUtils.addQueryString(protocolModelUrl, "appId", subSystemId);
            }

            return UrlUtils.ignoreLastRightSlash(serverUrl) + UrlUtils.SEPARATOR_SIGN + DeskTopConstant.UNIFY_LOGIN_URL +
                    "?url=" + paramAndProtocolUrl + "&uid=" + userName + "&appId=" + subSystemId;
        }

    }
}
