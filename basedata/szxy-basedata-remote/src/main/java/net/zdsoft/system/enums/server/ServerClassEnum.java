/* 
 * @(#)ServerClassEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.enums.server;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午10:44:01 $
 */
public enum ServerClassEnum {
    INNER_PRODUCT {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "内部产品";
        }
    },
    AP_PRODUCT {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "第三方应用";
        }
    };
    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (ServerClassEnum type : ServerClassEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return "未知来源";
    }
}
