/* 
 * @(#)MaintainEnum.java    Created on 2017-3-2
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.enums.mcode;


/**
 * @author xuxy
 * @version $Revision: 1.0 $, $Date: 2017-3-2 下午04:28:03 $
 */
public enum MaintainEnum {
    MAINTAIN {

        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return "可维护";
        }

    },
    SWITCH {

        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "可启停";
        }

    },
    VIEW {

        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "可查看";
        }

    };
    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (MaintainEnum type : MaintainEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
