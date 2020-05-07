package net.zdsoft.dataimport.freemarker;

import freemarker.ext.beans.HashAdapter;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;
import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Exporter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 17-8-5 下午9:53
 */
public class ViewDataFreemarkerUtils {

    public static String parseTemplate(Object object) throws TemplateModelException {
        if ( object instanceof SimpleHash ) {
            SimpleHash simpleHash = (SimpleHash)object;
            Map map = simpleHash.toMap();
            Collection values = map.values();
            StringBuilder td = new StringBuilder();
            for (Object value : values) {
                td.append("<td>").append(value.toString()).append("</td>");
            }
            return td.toString();
        } else if ( object instanceof HashAdapter ) {
            HashAdapter hashAdapter = (HashAdapter)object;
            StringBuilder td = new StringBuilder();
            for (Object o : hashAdapter.entrySet()) {
                if( ((Map.Entry)o).getKey().equals(Exporter.CHECKED) ){
                    continue;
                }
                td.append("<td>").append(((Map.Entry)o).getValue()).append("</td>");
            }
            return td.toString();
        }
        return "";
    }

    public static String parseJavaObject(Object object, Object headers) {
        StringBuffer tds = new StringBuffer();
        List<String> list = (List<String>)headers;
        for (Field e : object.getClass().getDeclaredFields()) {
            if ( list.contains(e.getAnnotation(ExcelCell.class).header()) ) {
                try {
                    e.setAccessible(Boolean.TRUE);
                    tds.append("<td>").append(e.get(object)).append("</td>");
                } catch (IllegalAccessException e1) {

                }
            }
        }
        return tds.toString();
    }
}
