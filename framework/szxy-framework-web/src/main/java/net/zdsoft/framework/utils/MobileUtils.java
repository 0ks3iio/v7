/* 
 * @(#)MobileUtils.java    Created on 2007-1-5
 * Copyright (c) 2006 ZDSoft Networks, Inc. All rights reserved.
 * $Header: /project/etoh2/src/net/zdsoft/etoh2/util/MobileUtils.java,v 1.6 2007/12/07 01:58:09 huangwj Exp $
 */
package net.zdsoft.framework.utils;

import java.util.regex.Pattern;

/**
 * 判断手机号码类型的工具类. 和keel里的<tt>Validators</tt>工具类的手机号码类型判断方法相比,<br>
 * 增加了对于模拟手机号码的判断. 模拟手机号码格式:
 * <ul>
 * <li>0001xxxxxxxx 移动</li>
 * <li>0002xxxxxxxx 联通</li>
 * <li>0003xxxxxxxx 小灵通</li>
 * </ul>
 * 其中xxxxxxxx为8位数字.
 * 
 * @author huangwj
 * @version $Revision: 12210 $, $Date: 2014-05-16 18:28:20 +0800 (周五, 2014-05-16) $
 */
public final class MobileUtils {

    public static boolean isMobile(String str, String chinaMobile, String chinaUnicom, String chinaTelcom) {
        return isChinaMobile(str, chinaMobile) || isChinaUnicom(str, chinaUnicom) || isChinaTelcom(str, chinaTelcom);
    }

    /**
     * 是否为中国移动手机号码.
     * 
     * @param str
     *            字符串
     * @return 如果是移动号码, 返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isChinaMobile(String str, String regex) {
        if (Validators.isEmpty(str) || Validators.isEmpty(regex)) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 是否为中国联通手机号码.
     * 
     * @param str
     *            字符串
     * @return 如果是联通号码, 返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isChinaUnicom(String str, String regex) {
        if (Validators.isEmpty(str) || Validators.isEmpty(regex)) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 判断是否为电信手机(Personal Access Phone System, PAS).
     * 
     * @param str
     *            字符串
     * @return 如果是电信号码, 返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isChinaTelcom(String str, String regex) {
        if (Validators.isEmpty(str) || Validators.isEmpty(regex)) {
            return false;
        }

        return Pattern.matches(regex, str);
    }

}
