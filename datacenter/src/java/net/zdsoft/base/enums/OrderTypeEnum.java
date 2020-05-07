/* 
 * @(#)UnitTypeEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.base.enums;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:47:11 $
 */
public enum OrderTypeEnum {
    SYSTEM {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "系统订阅";
        }
    },
    SYSTEM_NO_AUTH {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "系统订阅个人免费";
        }
    },
    UNIT_PERSONAL_AUTH {
        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public String getName() {
            return "单位订阅个人需授权";
        }
    },
    UNIT_PERSONAL_NO_AUTH {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "单位订阅个人免费";
        }
    };
    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
