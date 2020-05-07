package net.zdsoft.dataimport.biz;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class BeanUtils {

    static  <O,E> Class<O> getGenericityType(Class<E> eClass, int number) {
        Type type = eClass.getGenericSuperclass();
        Type[] types = ((ParameterizedType)type).getActualTypeArguments();
        if ( ArrayUtils.isEmpty(types) ) {
            return null;
        }
        if ( number > types.length + 1 ) {
            return (Class<O>) types[types.length-1];
        }
        return (Class<O>) types[number-1];
    }

    static <O,E> Class<O> getFirstGenericityType(Class<E> eClass) {
        return getGenericityType(eClass, 1);
    }

    static <O,E> Class<O> getFirstSuperInterfaceGenericityType(Class<E> eClass) {
        Type[] types = eClass.getGenericInterfaces();
        for (Type type : types) {
            return (Class<O>) ((ParameterizedType)type).getActualTypeArguments()[0];
        }
        return null;
    }

    static <O,E> Class<O> getLastGenericityType(Class<E> eClass) {
        return getGenericityType(eClass,Integer.MAX_VALUE);
    }

    static boolean setProperty(Object obj, String filedName, Object value ) {
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(obj, filedName, value);
        } catch (IllegalAccessException e) {
            return Boolean.FALSE;
        } catch (InvocationTargetException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    static <T> T newBean(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getProperty(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    static <T> T[] toArray(List<T> os, Class<T> tClass) {
        T[] array = null;
        if ( os == null ) {
            array = (T[])Array.newInstance(tClass, 0);
            return array;
        }
        array = (T[])Array.newInstance(tClass, os.size());
        return os.toArray(array);
    }

    static <T> boolean isEmpty(T[] ts) {
        return ts == null ? Boolean.TRUE : ts.length == 0;
    }
    static <T> boolean isNotEmpty(T[] ts) {
        return ts == null ? Boolean.FALSE : ts.length > 0;
    }

    static List<Field> getAnnotationWithAnnotation(Class clazz, Class<? extends Annotation> annotation) {
        List<Field> fields = Lists.newArrayList();
        for (Field e : clazz.getDeclaredFields()) {
            if ( e.getAnnotationsByType(annotation)!=null ) {
                fields.add(e);
            }
        }

        return fields;
    }

    static Object getFiledValue( Object obj, Field field ) {
        try {
            field.setAccessible(Boolean.TRUE);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    static boolean isNumer(Class type) {
        return newBean(type) instanceof Number;
    }
}
