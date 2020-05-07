package net.zdsoft.framework.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by shenke on 2017/4/11.
 */
public class Objects {


    public static boolean equals(Object a, Object b) {
        return ((a != null && a.equals(b) ) || a == b);
    }

    public static boolean deepEquals(Object a, Object b) {
        if (a == b)
            return true;
        else if (a == null || b == null)
            return false;
        else
            return arrayDeepEquals(a,b);
    }

    protected static boolean arrayDeepEquals(Object e1, Object e2){
        assert e1 != null;
        boolean eq;
        if (e1 instanceof Object[] && e2 instanceof Object[])
            eq = deepEquals ((Object[]) e1, (Object[]) e2);
        else if (e1 instanceof byte[] && e2 instanceof byte[])
            eq = equals((byte[]) e1, (byte[]) e2);
        else if (e1 instanceof short[] && e2 instanceof short[])
            eq = equals((short[]) e1, (short[]) e2);
        else if (e1 instanceof int[] && e2 instanceof int[])
            eq = equals((int[]) e1, (int[]) e2);
        else if (e1 instanceof long[] && e2 instanceof long[])
            eq = equals((long[]) e1, (long[]) e2);
        else if (e1 instanceof char[] && e2 instanceof char[])
            eq = equals((char[]) e1, (char[]) e2);
        else if (e1 instanceof float[] && e2 instanceof float[])
            eq = equals((float[]) e1, (float[]) e2);
        else if (e1 instanceof double[] && e2 instanceof double[])
            eq = equals((double[]) e1, (double[]) e2);
        else if (e1 instanceof boolean[] && e2 instanceof boolean[])
            eq = equals((boolean[]) e1, (boolean[]) e2);
        else
            eq = e1.equals(e2);
        return eq;
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }


    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }


    public static String toString(Object o) {
        return String.valueOf(o);
    }


    public static String toString(Object o, String nullDefault) {
        return (o != null) ? o.toString() : nullDefault;
    }


    public static <T> int compare(T a, T b, Comparator<? super T> c) {
        return (a == b) ? 0 :  c.compare(a, b);
    }


    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }


    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }


    public static boolean isNull(Object obj) {
        return obj == null;
    }


    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static String getVaule(Number number, String defaultVal) {
        if ( number != null ){
            return number.toString();
        }
        return defaultVal;
    }

    public static String getValue(Number number){
        return getVaule(number,"0");
    }

    /**
     * if object == null return 0
     * @param object
     * @return
     */
    public static int size(Object object) {
        return object == null?0: CollectionUtils.size(object);
    }
}
