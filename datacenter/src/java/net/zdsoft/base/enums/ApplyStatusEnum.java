/* 
 * @(#)ApplyStatusEnum.java    Created on 2017-2-23
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.base.enums;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-23 上午11:15:19 $ 0:所有 1:通过审核 2:审核不通过 3:审核中 4:未提及审核
 */
public enum ApplyStatusEnum {
    ALL {
        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return "所有";
        }
    },
    PASS_VERIFY {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "通过审核";
        }
    },
    UNPASS_VERIFY {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "审核不通过";
        }
    },
    IN_VERIFY {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "审核中";
        }

    },
    NOT_VERIFY {
        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public String getName() {
            return "未审核";
        }
    };
    public abstract int getValue();

    public abstract String getName();

    public static ApplyStatusEnum get(int value) {
        for (ApplyStatusEnum type : ApplyStatusEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
