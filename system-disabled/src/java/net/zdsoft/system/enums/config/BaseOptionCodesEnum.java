/* 
 * @(#)BaseOptionCodesEnum.java    Created on 2017-3-15
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.enums.config;

/**
 * 基本信息设置页面的参数灵活配置
 * 
 * @author xuxy
 * @version $Revision: 1.0 $, $Date: 2017-3-15 上午11:13:16 $
 */
public enum BaseOptionCodesEnum {
    PLAT_NAME {

        @Override
        public String getCode() {
            return "plat.name";
        }

        @Override
        public String getName() {
            return "平台名称";
        }

        @Override
        public String getHtmlType() {
            // TODO Auto-generated method stub
            return "input";
        }

    },
    PLAT_ABBREVIATION {

        @Override
        public String getCode() {
            return "plat.abbreviation";
        }

        @Override
        public String getName() {
            return "平台简称";
        }

        @Override
        public String getHtmlType() {
            return "input";
        }

    },
    PLAT_LOGO_PATH {

        @Override
        public String getCode() {
            return "plat.logo.path";
        }

        @Override
        public String getName() {
            return "平台logo";
        }

        @Override
        public String getHtmlType() {
            // TODO Auto-generated method stub
            return "file";
        }

    },
    LPAT_BOTTOM {

        @Override
        public String getCode() {
            return "plat.bottom";
        }

        @Override
        public String getName() {
            return "底部参数配置";
        }

        @Override
        public String getHtmlType() {
            return "textarea";
        }

    };

    public abstract String getCode();

    public abstract String getName();

    public abstract String getHtmlType();

    public static String getName(String code) {
        for (BaseOptionCodesEnum type : BaseOptionCodesEnum.values()) {
            if (type.getCode() == code) {
                return type.getName();
            }
        }
        return null;
    }
}
