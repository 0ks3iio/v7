/*
 * @(#)YesNoEnum.java    Created on 2012-7-3
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.enums;

/**
 * 通用YES=1/NO=0枚举
 * 
 * @author YUYI
 * @version $Revision: 1.0 $, $Date: 2012-7-3 下午07:02:48 $
 */
public enum YesNoEnum {
    /**
     * 1.YES
     * 
     * @author YUYI
     * @version $Revision: 1.0 $, $Date: 2012-6-19 下午07:02:48 $
     */
    YES {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getNameValue() {
            return YES_STATUS;
        }
    },
    /**
     * 
     * 0.NO
     * 
     * @author YUYI
     * @version $Revision: 1.0 $, $Date: 2012-6-19 下午07:02:48 $
     */
    NO {
        @Override
        public int getValue() {
            return 0;
        }

        @Override
        public String getNameValue() {
            return NO_STATUS;
        }
    }

    ;
    /**
     * 得到类型的整数值
     * 
     * @return
     */
    public abstract int getValue();

    public abstract String getNameValue();

    /**
     * 根据值得到枚举对象
     * 
     * @param value
     * @return 如果没有得到，返回null
     */
    public static YesNoEnum get(int value) {
        for (YesNoEnum type : YesNoEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }

        return null;
    }

    /**
     * 根据值得到描述信息
     * 
     * @param value
     * @return 如果没有得，返回空字符串
     */
    public static String getNameValue(int value) {
        for (YesNoEnum type : YesNoEnum.values()) {
            if (type.getValue() == value) {
                return type.getNameValue();
            }
        }
        return "";
    }

    public static String getNameValueContrary(int value) {
        if (value == YES.getValue()) {
            return NO_STATUS;
        }
        if (value == NO.getValue()) {
            return YES_STATUS;
        }
        return "";
    }

    private static final String YES_STATUS = "是";
    private static final String NO_STATUS = "否";
}
