/* 
 * @(#)MethodTypeEnum.java    Created on 2017-2-23
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.base.enums;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-23 上午11:05:58 $
 */
public enum MethodTypeEnum {
    GET("get"), POST("post"), PUT("put"), DELETE("delete");

    private String name;

    MethodTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
