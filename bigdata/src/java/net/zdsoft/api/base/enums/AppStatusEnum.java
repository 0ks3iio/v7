/* 
 * @(#)AppStatusEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.api.base.enums;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private static Map<Integer, AppStatusEnum> cache;

    static {
        cache = Arrays.stream(AppStatusEnum.values()).collect(Collectors.toMap(AppStatusEnum::getValue, Function.identity()));
    }

    public static AppStatusEnum from(Integer value) {
        Assert.notNull(value, "value can't null");
        return cache.get(value);
    }
}
