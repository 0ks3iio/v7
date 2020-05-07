/* 
 * @(#)ObjectUtils.java    Created on 2004-10-13
 * Copyright (c) 2004 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ObjectUtils.java,v 1.11 2008/07/31 11:28:21 huangwj Exp $
 */
package net.zdsoft.newstusys.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.fileupload.FileItem;

import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Validators;

/**
 * 对象工具类
 * 
 * @author liangxiao
 * @version $Revision: 1.11 $, $Date: 2008/07/31 11:28:21 $
 */
public final class ObjectUtils {

    private ObjectUtils() {
    }

    /**
     * 取得对象指定名称对应的属性的值
     * 
     * @param object
     *            对象
     * @param name
     *            属性名称
     * @return 属性的值
     */
    public static Object getProperty(Object object, String name) {
        try {
            // return BeanUtils.getProperty(model, propertyName);
            return new PropertyUtilsBean().getProperty(object, name);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not getProperty[" + name + "]", e);
        }
    }

    /**
     * 取得对象指定嵌套的名称对应的属性的值，中间用.隔开， 可以类似school.name，表示取得school属性的name属性的值
     * 
     * @param object
     *            对象
     * @param nestedName
     *            嵌套的属性名称
     * @return 属性的值
     */
    public static Object getNestedProperty(Object object, String nestedName) {
        if (nestedName.indexOf(".") == -1) {
            return getProperty(object, nestedName);
        }

        String[] names = nestedName.split("\\.");
        for (int i = 0; i < names.length; i++) {
            object = getProperty(object, names[i]);
        }
        return object;
    }

    /**
     * 按照参数Map设置对象的属性的值
     * 
     * @param bean
     *            对象
     * @param parameters
     *            参数
     */
    public static void setProperties(Object bean, Map<String, Object> parameters) {
        // try {
        // BeanUtils.populate(bean, parameters);
        // }
        // catch (Exception e) {
        // throw new RuntimeException("Could not setProperties", e);
        // }

        // Do nothing unless both arguments have been specified
        if ((bean == null) || (parameters == null)) {
            return;
        }

        // Loop through the property name/value pairs to be set
        Iterator<Map.Entry<String, Object>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();

            // Identify the property name and value(s) to be assigned
            String name = (String) entry.getKey();
            if (name == null) {
                continue;
            }
            Object value = entry.getValue();

            // Perform the assignment for this property
            boolean isSuccess = true;
            try {
                BeanUtils.setProperty(bean, name, value);
            }
            catch (Exception e) {
                isSuccess = false;
            }

            if (!isSuccess) {
                if (value instanceof String) {// 字符串补充处理
                    String string = ((String) value).trim();
                    Date date = null;

                    // 需要判断日期、时间是否符合格式
                    if (Validators.isDate(string)) {
                        date = DateUtils.string2Date(string);
                    }
                    else if (Validators.isDateTime(string)) {
                        date = DateUtils.string2DateTime(string);
                    }

                    if (date == null) {
                        continue;
                    }

                    try {
                        BeanUtils.setProperty(bean, name, date);
                        isSuccess = true;
                    }
                    catch (Exception e) {
                    }

                    if (!isSuccess) {
                        try {
                            BeanUtils.setProperty(bean, name,
                                    new String[] { (String) value });
                        }
                        catch (Exception e) {
                        }
                    }
                }
                else {// 文件补充处理
                    try {
                        BeanUtils.setProperty(bean, name,
                                new FileItem[] { (FileItem) value });
                    }
                    catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 设置对象指定名称的属性的值
     * 
     * @param object
     *            对象
     * @param name
     *            参数名称
     * @param value
     *            参数的值
     */
    public static void setProperty(Object object, String name, Object value) {
        try {
            BeanUtils.setProperty(object, name, value);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not setProperty[" + name + "]", e);
        }
    }

    /**
     * 根据类创建对象
     * 
     * @param clazz
     *            类
     * @return 对象
     */
    public static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Could not create instance", e);
        }
    }

    /**
     * 根据类名创建对象
     * 
     * @param className
     *            类名
     * @return 对象
     */
    public static Object createInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Could not create instance[" + className
                    + "]", e);
        }
    }

    public static void main(String[] args) {
    }

}
