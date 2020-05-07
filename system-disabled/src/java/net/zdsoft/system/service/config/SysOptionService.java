/*
 * Project: v7
 * Author : shenke
 * @(#) SysOptionService.java Created on 2016-8-25
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.system.service.config;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.config.SysOption;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 系统参数
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-25下午5:51:34
 */
public interface SysOptionService extends BaseService<SysOption, String> {

    /**
     * 数据库中获取
     *
     * @param optionCode
     * @return
     */
    SysOption findOneByOptionCode(String optionCode);

    SysOption saveOne(SysOption sysOption);

    /**
     * 缓存中获取
     *
     * @param optionCode
     * @return
     */
    String findValueByOptionCode(String optionCode);

    // 刷新缓存
    void doRefreshCache(String... optionCode);

    // 刷新缓存
    void doRefreshCacheAll();

    // List<SysOption> fandAll();

    void updateNowValueByCode(String nowValue, String code);

    // 更新nowValue值类型
    void updateValueType(int valueType, String code);

    /**
     * 是否有内外网访问
     * @param currentServerName 当前环境 不能为null
     */
    boolean isSecondUrl(String currentServerName);

    /**
     * 请保证数据库内网的INDEX.URL PASSPORT.URL FILE.URL 一致 <br>
     * INDEX.SECOND.URL PASSPORT.SECOND.URL FILE.SECOND.URL一致 <br>
     * 根据ServerName判断使用FILE_URL还是FILE_SECOND_URL   <br>
     * 若数据库中secondURL为空，则取FILE.URL
     * @see net.zdsoft.system.constant.Constant#FILE_URL
     * @see net.zdsoft.system.constant.Constant#FILE_SECOND_URL
     * * @param serverName 当前服务名
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

    List<SysOption> findSysOptionByCodes(String[] codes);
}
