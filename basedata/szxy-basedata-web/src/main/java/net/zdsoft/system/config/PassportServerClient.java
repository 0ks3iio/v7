/* 
 * @(#)HttpInvokerClient.java    Created on 2017-3-11
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.stereotype.Component;

import net.zdsoft.passport.remoting.system.ServerService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.service.config.SysOptionService;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-3-11 下午12:05:22 $
 */
@Component
public class PassportServerClient {

    @Autowired
    private SysOptionService sysOptionService;

    private static HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean;

    private void initHttpInvoker() {
        if (httpInvokerProxyFactoryBean == null) {
            httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        }
    }

    public ServerService getPassportServerService() {
        // HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        initHttpInvoker();
        String passPortUrl = sysOptionService.findValueByOptionCode(Constant.PASSPORT_URL);
        httpInvokerProxyFactoryBean.setServiceUrl(passPortUrl + "/remoting/ServerService");
        httpInvokerProxyFactoryBean.setServiceInterface(ServerService.class);
        httpInvokerProxyFactoryBean.afterPropertiesSet();
        return (ServerService) httpInvokerProxyFactoryBean.getObject();
    }
}
