/* 
 * @(#)UserTypeEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.base.enums;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:23:41 $
 */
public enum UserTypeEnum {
    STUDENT {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "学生";
        }
    },
    TEACHER {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "老师";
        }
    },
    PARENT {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "家长";
        }
    };

    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (UserTypeEnum type : UserTypeEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
