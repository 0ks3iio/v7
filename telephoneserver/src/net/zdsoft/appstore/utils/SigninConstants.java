/* 
 * @(#)SignConstants.java    Created on Jan 4, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.appstore.utils;

/**
 * @author zhaowj
 * @version $Revision: 1.0 $, $Date: Jan 4, 2010 4:33:10 PM $
 */
public final class SigninConstants {

    // 支持卡类型：1、同时支持10、16进制卡号；2、只支持10进制卡号；3、只支持16进制卡号。
    public static final int CARD_TYPE_DH = 1;// 同时支持10、16进制卡号

    public static final int CARD_TYPE_DEC = 2;// 只支持10进制卡号

    public static final int CARD_TYPE_HEX = 3;// 只支持16进制卡号

}
