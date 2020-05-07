/*
 * @Project: v7
 * @Author shenke
 * @(#)RequestUtils.java  Created on 2016/11/3
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 * @Date 2016/11/3 create by intellij idea and don't die in 28 years old
 */
package net.zdsoft.nbwebsite.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @date: 2016/11/3 14:44
 * create by intellij idea
 */
public class RequestUtils {

    /**
     * support name and id 暂时还不支持date类型
     * @param request
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T wrapper(HttpServletRequest request,T t){
        String title = request.getParameter("title");
        Map<String,String[]> map = request.getParameterMap();
        Enumeration<String> names = request.getParameterNames();
        if(names == null){
            return t;
        }
        while(names.hasMoreElements()){
            String name = names.nextElement();
            String value = request.getParameter(name);
            if(name.indexOf(".")>-1){
                name = name.substring(name.indexOf(".")+1);
            }
            try {
                BeanUtils.setProperty(t,name,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static void main(String[] args){
        String name = "wen.index";
        System.out.println(name.substring(name.indexOf(".")));
    }
}
