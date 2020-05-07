/* 
 * @(#)SectionsTypeEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.enums.unit;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午10:37:42 $
 */
public enum SectionsTypeEnum {
    YEY {
        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return "幼儿园";
        }
    },
    XX {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "小学";
        }
    },
    CZ {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "初中";
        }
    },
    GZ {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "高中";
        }
    },
    JQGZ {
        @Override
        public int getValue() {
            return 9;
        }

        @Override
        public String getName() {
            return "剑桥高中";
        }
    };
    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (SectionsTypeEnum type : SectionsTypeEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
