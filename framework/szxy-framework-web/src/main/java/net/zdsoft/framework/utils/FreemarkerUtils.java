package net.zdsoft.framework.utils;

import com.google.common.collect.Lists;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.SimpleDate;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * java Type convert to Freemarker Type utils
 * Created by shenke on 2017/4/17.
 */
public class FreemarkerUtils {

    private static final SimpleHash EMPTY_SIMPLE_HASH = new SimpleHash(new HashMap());
    private static final SimpleSequence EMPTY_SIMPLE_SEQUENCE = new SimpleSequence(new ArrayList());
    private static final ObjectWrapper DEFAULT_WRAPPER = ObjectWrapper.DEFAULT_WRAPPER;

    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerUtils.class);

    public static SimpleHash convertToSimpleHash(Object object) {
        Objects.requireNonNull(object);
        if (object instanceof Map) {
            return new SimpleHash((Map) object);
        }
        return EMPTY_SIMPLE_HASH;
    }

    public static SimpleSequence convertToSequence(Object object) {
        Objects.requireNonNull(object);
        if ( object instanceof Collection) {
            return new SimpleSequence((Collection)object);
        }
        else if (object.getClass().isArray()) {
            return new SimpleSequence(convertArray(object));
        }
        return EMPTY_SIMPLE_SEQUENCE;
    }

    public static TemplateModel convertToFreemarkerType( Object obj){
        try {
            if(obj == null) {
                return TemplateModel.NOTHING;
            } else if(obj instanceof TemplateModel) {
                return (TemplateModel)obj;
            } else if(obj instanceof String) {
                return new SimpleScalar((String)obj);
            } else if(obj instanceof Number) {
                return new SimpleNumber((Number)obj);
            } else if(obj instanceof Date) {
                return obj instanceof java.sql.Date?new SimpleDate((java.sql.Date)obj):(obj instanceof Time ?new SimpleDate((Time)obj):(obj instanceof Timestamp ?new SimpleDate((Timestamp)obj):new SimpleDate((Date)obj, 0)));
            } else {
                if(obj.getClass().isArray()) {
                    obj = convertArray(obj);
                }
                return (obj instanceof Collection ?new SimpleSequence((Collection)obj, DEFAULT_WRAPPER):(obj instanceof Map ?new SimpleHash((Map)obj, DEFAULT_WRAPPER):(obj instanceof Boolean?(obj.equals(Boolean.TRUE)? TemplateBooleanModel.TRUE:TemplateBooleanModel.FALSE):(obj instanceof Iterator ?new SimpleCollection((Iterator)obj, DEFAULT_WRAPPER): ObjectWrapper.BEANS_WRAPPER.wrap(obj)))));
            }
        } catch (TemplateModelException e) {
            LOG.error("类型转换异常",e);
            return TemplateModel.NOTHING;
        }
    }

    public static List convertArray(Object array){
        int size = Array.getLength(array);
        List list = Lists.newArrayList(size);
        for(int i=0 ; i < size; i++){
            list.add(Array.get(array,i));
        }
        return list;
    }
}
