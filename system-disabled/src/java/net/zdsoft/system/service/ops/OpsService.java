/* 
 * @(#)OpsService.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.service.ops;

import java.util.Map;

public interface OpsService {

    void modifyLoginSetParameters(Map<String,Object> paraMap);

    /**
     * 序列号激活操作，初始化base_server 调用passport初始化sys_server 等
     * @param unitName
     * @param licenseTxt
     * @param regionCode
     */
    void activeLicense(String unitName, String licenseTxt, String regionCode);
}
