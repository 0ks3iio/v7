/* 
 * @(#)Tools.java    Created on Jan 4, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.appstore.utils;

import org.apache.log4j.Logger;

/**
 * @author zhaowj
 * @version $Revision: 1.0 $, $Date: Jan 4, 2010 4:39:45 PM $
 */
public class Tools {

    private static Logger logger = Logger.getLogger(Tools.class);

    // 是否为公司的公话协议服务器，如果是，则不进行卡号根据话机转换的操作。
    private static boolean isZdsoftServer = false;

    /*
     * 根据卡类型，得到转换后的卡号（终端 -> 数据库）
     */
    public static String getCardNumberFromDevice(String cardId, int cardType) {
        if (isZdsoftServer) {
            // 如果是公司自己的话机服务器，则不进行卡号转换，因为在协议部分已经做了转换。
            return cardId;
        }
        String cardNumber = null;
        if (SigninConstants.CARD_TYPE_DH == cardType) {
            if (cardId.length() <= 8) {
                cardNumber = Long.valueOf(cardId, 16).toString();
            }
            else {
                cardNumber = cardId;
            }
        }
        else if (SigninConstants.CARD_TYPE_DEC == cardType) {
            cardNumber = cardId;
        }
        else if (SigninConstants.CARD_TYPE_HEX == cardType) {
            cardNumber = Long.valueOf(cardId, 16).toString();
        }
        else {
            cardNumber = cardId;
        }
        return cardNumber;
    }

    /*
     * 根据卡类型，得到转换后的卡号（数据库 ->终端）
     */
    public static String getCardNumberFromDb(String cardNumber, int cardType) {
        if (isZdsoftServer) {
            // 如果是公司自己的话机服务器，则不进行卡号转换，因为在协议部分已经做了转换。
            return cardNumber;
        }
        String cardId = cardNumber;
        // 十进制
        if (cardType == 1) {
            // cardId = getCardNumberFromDb(cardNumber);
            cardId = getCardNumberTen2Hex(cardNumber);
        }
        // 十六进制
        else if (cardType == 2) {
            cardId = cardNumber;
        }
        // 十进制或十六进制
        else if (cardType == 3) {
            cardId = getCardNumber2Hex(cardNumber);
            // cardId = Long.toHexString(Long.parseLong(cardNumber));
        }
        // else {
        // cardId = getCardNumberFromDb(cardNumber);
        // }
        // 最终返回十六进制
        return cardId;
    }

    public static String getCardNumber2Hex(String cardNumber) {
        if (cardNumber.length() > 18) {
            logger.info("卡号非法" + cardNumber);
            return "FFFFFFFF";
        }
        if (cardNumber.length() >= 8) {
            return cardNumber;
        }
        if (cardNumber.length() <= 8) {
            cardNumber = Long.toHexString(Long.parseLong(cardNumber));
        }

        // cardNumber = Long.toHexString(Long.parseLong(cardNumber));
        return cardNumber;
    }

    public static String getCardNumberTen2Hex(String cardNumber) {
        if (cardNumber.length() > 18) {
            logger.info("卡号非法" + cardNumber);
            return "FFFFFFFF";
        }
        cardNumber = Long.toHexString(Long.parseLong(cardNumber));
        return cardNumber;
    }

    public static String getCardNumberFromDb(String cardNumber) {
        if (isZdsoftServer) {
            // 如果是公司自己的话机服务器，则不进行卡号转换，因为在协议部分已经做了转换。
            return cardNumber;
        }
        // String cardId = Long.toHexString(Long.parseLong(cardNumber));
        // if (cardId.length() > 8) {
        // cardId = cardNumber;
        // }

        if (cardNumber.length() <= 8) {
            cardNumber = Long.valueOf(cardNumber, 16).toString();
        }
        return cardNumber;
        // return cardId;
    }

    public static void main(String[] args) {
        String s = "999996";
    }

    public static boolean isZdsoftServer() {
        return isZdsoftServer;
    }

    public static void setZdsoftServer(boolean isZdsoftServer) {
        Tools.isZdsoftServer = isZdsoftServer;
    }

    // 转化为十六进制编码
    public static String toHexString(byte[] obj) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < obj.length; i++) {
            int ch = obj[i];
            if (ch < 0) {
                ch = ch + 256;
            }
            String str = Integer.toString(ch, 16);
            if (str.length() == 1) {
                sb.append("0");
            }
            sb.append(str);
        }
        return sb.toString().toUpperCase();
    }

    // 转化为十六进制编码，中间有空格分隔，主要用于日志显示
    public static String toDisplayHexString(byte[] obj) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < obj.length; i++) {
            int ch = obj[i];
            if (ch < 0) {
                ch = ch + 256;
            }
            String str = Integer.toString(ch, 16);
            if (str.length() == 1) {
                sb.append("0");
            }
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString().toUpperCase();
    }

}
