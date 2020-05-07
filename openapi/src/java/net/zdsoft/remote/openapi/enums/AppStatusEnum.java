/* 
 * @(#)AppStatusEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.enums;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:11:58 $
 */
public enum AppStatusEnum {

    STOP {
        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return "已停用";
        }
    },
    ONLINE {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "已上线";
        }
    },
    OFFLINE {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "未上线";
        }
    },
    UNCOMMITTED {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "未提交";
        }
    },
    NOTPASS {
        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public String getName() {
            return "未通过";
        }
    },
    AUDIT {
        @Override
        public int getValue() {
            return 5;
        }

        @Override
        public String getName() {
            return "审核中";
        }
    };

    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (AppStatusEnum type : AppStatusEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
