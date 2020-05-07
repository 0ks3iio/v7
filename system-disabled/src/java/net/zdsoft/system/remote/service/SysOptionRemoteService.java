package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.config.SysOption;

import javax.servlet.http.HttpServletRequest;

public interface SysOptionRemoteService extends BaseRemoteService<SysOption,String> {

    /**
     * 获取系统级参数值
     * 
     * @param optionCode
     * @return
     */
    public String findValue(String optionCode);

    /**
     *
     * @param currentServerName 不能为空
     * @return
     */
    boolean isSecondUrl(String currentServerName);

    /**
     * 请保证数据库内网的INDEX.URL PASSPORT.URL FILE.URL 一致 <br>
     * INDEX.SECOND.URL PASSPORT.SECOND.URL FILE.SECOND.URL一致 <br>
     * 根据ServerName判断使用FILE_URL还是FILE_SECOND_URL   <br>
     * 若数据库中secondURL为空，则取FILE.URL
     * @see net.zdsoft.system.constant.Constant#FILE_URL
     * @see net.zdsoft.system.constant.Constant#FILE_SECOND_URL
     * @param serverName 当前服务名
     */
    String getFileUrl(String serverName);

    /**
     * 根据请求服务名获取整个系统的首页地址<br>
     * indexUrl主要为passport登录页提供服务 <br>
     * 如5.0 6.0 7.0 登录页使用7.0的登录页，在 INDEX.URL 以及 INDEX.SECOND.URL 应该配置7.0的<br>
     * 如若获取当前服务的首页地址，请参见{@link net.zdsoft.framework.utils.UrlUtils#getPrefix(HttpServletRequest)}
     * @see HttpServletRequest#getServerName()
     */
    String getIndexUrl(String serverName);
}
