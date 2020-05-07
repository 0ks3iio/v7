/* 
 * @(#)YesNoEnum.java    Created on 2017年3月2日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.enums;


/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月2日 下午4:16:24 $
 */
public enum YesNoEnum {
    YES {

        @Override
        public int getValue() {
            return 1;
        }

    },
    NO {

        @Override
        public int getValue() {
            return 0;
        }

    };

    public abstract int getValue();
}
