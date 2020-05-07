/* 
 * @(#)FilterParamUtils.java    Created on Dec 16, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.utils;

import java.util.StringTokenizer;

import javax.servlet.FilterConfig;

/**
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Dec 16, 2010 4:43:03 PM $
 */
public class FilterParamUtils {

    /**
     * 取忽略路径参数
     * 
     * @param filterConfig
     * @param param
     * @return
     */
    public static String[] getParamValues(FilterConfig filterConfig, String param) {
        String values = filterConfig.getInitParameter(param);
        return splitByComma(values);
    }

    /**
     * 以,号分隔
     * @param values
     * @return
     */
    public static String[] splitByComma(String values){
        if (null == values)
            return null;
        
        StringTokenizer st = new StringTokenizer(values, ",");
        int count = st.countTokens();
        String[] arr = new String[count];
        for (int i = 0; i < count; i++) {
            arr[i] = st.nextToken().trim();
        }
        return arr;
    }
    
    /**
     * 是否包含路径
     * 
     * @param servletPath
     * @param paths
     * @return
     */
    public static boolean containsPath(String servletPath, String[] paths) {
        if (paths == null) {
            return false;
        }
        for (int i = 0, n = paths.length; i < n; i++) {
            if (paths[i].endsWith(".action")) {
                if (servletPath.endsWith(paths[i])) {
                    return true;
                }
            } else {
                if (servletPath.contains(paths[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
