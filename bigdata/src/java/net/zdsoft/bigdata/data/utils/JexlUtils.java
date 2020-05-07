/**
 * FileName: JexlUtils.java
 * Author:   shenke
 * Date:     2018/5/23 下午4:10
 * Descriptor:
 */
package net.zdsoft.bigdata.data.utils;

import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.ObjectContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.springframework.util.Assert;

/**
 * @author shenke
 * @since 2018/5/23 下午4:10
 */
final public class JexlUtils {

    private static JexlEngine engine;
    private static UnifiedJEXL unifiedJEXL;

    static {
        engine = new JexlEngine();
        unifiedJEXL = new UnifiedJEXL(engine);
    }

    /**
     * 解析包含Jexl标签的字符串
     * @param express 待解析字符串
     * @param context 对应参数的map
     * @return 解析之后的字符串
     */
    public static String evaluate(String express, Map<String, Object> context) {
        Assert.hasLength(express);
        Assert.notEmpty(context);
        JexlContext jexlContext = new MapContext(context);
        return unifiedJEXL.parse(express).evaluate(JexlContextAdapter.adapter(jexlContext)).toString();
    }

    /** 利用Jexl解析 */
    public static String evaluate(String express, Supplier<Map<String, Object>> supplier) {
        Assert.hasLength(express);
        Assert.notNull(supplier);
        return evaluate(express, supplier.get());
    }

    /**
     * 利用Jexl解析字符串，当待解析字符串包含唯一特定参数是可调用此简单方法
     */
    public static String evaluate(String express, String name, Object value) {
        Assert.hasLength(express);
        Assert.hasLength(name);
        JexlContext context = new MapContext();
        context.set(name, value);
        return unifiedJEXL.parse(express).evaluate(JexlContextAdapter.adapter(context)).toString();
    }

    public static <T> String evaluate(String express, T context, boolean withQuotes) {
        if (withQuotes) {
            return evaluate(express, context);
        } else {
            Assert.hasLength(express);
            Assert.notNull(context);
            if (context instanceof JexlContext) {
                return unifiedJEXL.parse(express).evaluate((JexlContext) context).toString();
            }
            else if (context instanceof Map) {
                JexlContext mapContext = new MapContext((Map<String, Object>) context);
                return unifiedJEXL.parse(express).evaluate(mapContext).toString();
            }
            else {
                JexlContext jexlContext = new ObjectContext<T>(engine, context);
                return unifiedJEXL.parse(express).evaluate(jexlContext).toString();
            }
        }
    }

    public static <T> ObjectContext<T> createContext(T t) {
        return new ObjectContext<>(engine, t);
    }

    /**
     *
     * @param express 待解析字符串
     * @param context 可以是普通的projo对象（必须是public的）也可以是Jexl的JexlContext
     * @return 解析之后的字符串
     */
    public static <T> String evaluate(String express, T context) {
        Assert.hasLength(express);
        Assert.notNull(context);
        if (context instanceof JexlContextAdapter) {
            return unifiedJEXL.parse(express).evaluate((JexlContext) context).toString();
        }
        else if (context instanceof JexlContext) {
            return unifiedJEXL.parse(express).evaluate(JexlContextAdapter.adapter((JexlContext) context)).toString();
        }
        else if (context instanceof Map) {
            JexlContext mapContext = new MapContext((Map<String, Object>) context);
            return unifiedJEXL.parse(express).evaluate(JexlContextAdapter.adapter(mapContext)).toString();
        }
        else {
            JexlContext jexlContext = new ObjectContext<T>(engine, context);
            return unifiedJEXL.parse(express).evaluate(JexlContextAdapter.adapter(jexlContext)).toString();
        }
    }


    public final static class JexlContextAdapter implements JexlContext {

        public JexlContext context;
        public boolean needQuotes;

        private JexlContextAdapter(JexlContext context) {
            this(context, true);
        }

        private JexlContextAdapter(JexlContext context, boolean needQuotes) {
            this.context = context;
            this.needQuotes = needQuotes;
        }

        static JexlContext adapter(JexlContext jexlContext) {
            return new JexlContextAdapter(jexlContext);
        }

        public static JexlContext adapterNoQuotes(JexlContext jexlContext) {
            return new JexlContextAdapter(jexlContext, false);
        }

        @Override
        public Object get(String name) {
            if (needQuotes) {
                return "'" + context.get(name) + "'";
            } else {
                return context.get(name);
            }
        }

        @Override
        public void set(String name, Object value) {
            context.set(name, value);
        }

        @Override
        public boolean has(String name) {
            return context.has(name);
        }
    }

}
