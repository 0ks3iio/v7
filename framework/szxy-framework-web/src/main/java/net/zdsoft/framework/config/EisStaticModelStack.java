package net.zdsoft.framework.config;

import com.google.common.collect.Maps;
import freemarker.template.TemplateModel;
import net.zdsoft.framework.utils.FreemarkerUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shenke on 2017/4/6.
 */
public final class EisStaticModelStack {

    public static final String SEPERATOR = "@";


    private static final Map<String,TemplateModel> STACK_CACHE = Maps.newConcurrentMap();

    public static TemplateModel findValue(String key){

        TemplateModel templateModel = null;
        if (null != (templateModel = STACK_CACHE.get(key))){
            return templateModel;
        }

        if (StringUtils.isNotBlank(key) && key.indexOf(SEPERATOR) == 0){
            key = StringUtils.substring(key,1);
        }

        try {
            List<String> values = Arrays.asList(StringUtils.split(key,SEPERATOR));
            Iterator<String> code =  values.iterator();
            Object obj = null;
            if (CollectionUtils.isNotEmpty(values)) {
                Class<?> fullClass = EisStaticModelStack.class.getClassLoader().loadClass(values.get(0));
                while( code.hasNext() ){
                    String prop = code.next();
                    if (fullClass.getName().equals(prop)){
                        continue;
                    }
                    obj = fullClass.getField(prop).get(fullClass);
                    fullClass = obj.getClass();
                }
            }
            templateModel = FreemarkerUtils.convertToFreemarkerType(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        STACK_CACHE.put(key,templateModel);
        return templateModel;
    }
}
