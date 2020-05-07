package net.zdsoft.cache.admin.utils;

import com.google.common.collect.Maps;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 获取tomcat相关参数
 * @author shenke
 * @since 2017.07.18
 */
public class TomcatUtils {

    private static Class serverFactoryClass;

    private static Map<Integer,String> portURIEncodingMap = Maps.newHashMap();

    public static String getURIEncoding(Integer p) {
        if ( !portURIEncodingMap.isEmpty() ) {
            return portURIEncodingMap.get(p);
        }
        Class serverFactoryClass = getServerClassFactoryClass();
        if ( serverFactoryClass == null ) {
            return null;
        }
        try {
            Object server = invokeStaticMethod(serverFactoryClass, "getServer");
            Object servicesArray = invokeMethod(server, "findServices");
            for(int i = 0, k = Array.getLength(servicesArray); i < k; i++) {
                Object service = Array.get(servicesArray, i);
                String serviceName = (String)invokeMethod(service, "getName");
                // 通过 Service 对象的 findConnectors 获得所有的 Connector
                Object connectorsArray = invokeMethod(service, "findConnectors");
                for(int j = 0, m = Array.getLength(connectorsArray); j < m; j++) {
                    Object connector = Array.get(connectorsArray, j);
                    String uriEncoding = (String)invokeMethod(connector, "getURIEncoding");
                    Integer port = (Integer)invokeMethod(connector, "getPort");
                    portURIEncodingMap.put(port,uriEncoding);
                }
            }
        } catch (Exception e){

        }
        return portURIEncodingMap.get(p);
    }

    //public static

    private static Class getServerClassFactoryClass() {
        if ( serverFactoryClass == null ) {
            serverFactoryClass = getClass("org.apache.catalina.ServerFactory");
        }
        return serverFactoryClass;
    }

    public static Class getClass(String className) {
        Thread[] threads =  getAllThread();
        Class clazz = null;
        for (Thread thread : threads) {
            try {
                ClassLoader loader = thread.getContextClassLoader();
                if ( loader == null ) continue;
                clazz = loader.loadClass(className);
                return clazz;
            } catch (Exception e){

            }
        }
        return null;
    }

    private static Thread[] getAllThread() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        ThreadGroup top = root;
        while ( (top = top.getParent()) != null ) {
            root = top;
        }
        Thread[] threadList = new Thread[root.activeCount()*2];
        int length = root.enumerate(threadList);
        Thread[] finalThreadList = new Thread[length];
        System.arraycopy(threadList, 0, finalThreadList, 0, length);
        return finalThreadList;
    }

    public static Object invokeMethod(Object obj, String methodName)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Method method = obj.getClass().getMethod(methodName, new Class[0]);
        return method.invoke(obj, new Object[0]);
    }

    @SuppressWarnings("unchecked")
    public static Object invokeStaticMethod(Class clazz, String methodName)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method method = clazz.getMethod(methodName, new Class[0]);
        return method.invoke(null, new Object[0]);
    }
}
