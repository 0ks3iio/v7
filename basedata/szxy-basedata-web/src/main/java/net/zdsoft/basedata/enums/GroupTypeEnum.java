/*
 * @(#)AppStatusEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.enums;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:11:58 $
 */
public enum GroupTypeEnum {
    // 1:公文本单位用户组,2:公文单位组,3:办公用户组,4:办公联系人组,5:通讯单位组,6:归档文件夹,7:公文其他单位用户组
    ARCHIVESUSER {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "公文本单位用户组";
        }
    },
    ARCHIVESUNIT {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "公文单位组";
        }
    },
    OFFICEUSER {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "办公用户组";
        }
    },
    OFFICELINK {
        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public String getName() {
            return "办公联系人组";
        }
    },
    ARRESSSUNIT {
        @Override
        public int getValue() {
            return 5;
        }

        @Override
        public String getName() {
            return "通讯单位组";
        }
    },
    DOCUMENTDIR {
        @Override
        public int getValue() {
            return 6;
        }

        @Override
        public String getName() {
            return "归档文件夹";
        }
    },
    ARCHIVESOTHERUNIT {
        @Override
        public int getValue() {
            return 7;
        }

        @Override
        public String getName() {
            return "公文其他单位用户组";
        }
    };

    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (GroupTypeEnum type : GroupTypeEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
