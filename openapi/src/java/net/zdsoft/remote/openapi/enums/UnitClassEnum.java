/* 
 * @(#)UnitTypeEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.enums;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:47:11 $
 */
public enum UnitClassEnum {
    EDUCATION {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "教育局";
        }
    },
    SCHOOL {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "学校";
        }
    };
    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (UnitClassEnum type : UnitClassEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
