package net.zdsoft.studevelop.data.constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import org.apache.tools.ant.filters.StringInputStream;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DocumentHandler {

    public static String createDocString(Map<String, Object> dataMap, String templateFile)
            throws Exception {
        return createDocString(dataMap, "/businessconf/template", templateFile);
    }
    
    public static InputStream createDocInputStream(Map<String, Object> dataMap, String templateFile)
            throws Exception {
        String s = createDocString(dataMap, templateFile);
        InputStream in = new StringInputStream(s, "UTF-8");    
        return in;
    }

    public static String createDocString(Map<String, Object> dataMap, String templatePath, String templateFile) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        Template t = null;
        configuration.setClassForTemplateLoading(DocumentHandler.class, templatePath);
        try {
            // 要装载的模板
            t = configuration.getTemplate(templateFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        // 输出文档路径及名称
        StringWriter out = new StringWriter();
        try {
            t.process(dataMap, out);
        }
        catch (TemplateException e) {
            e.printStackTrace();
            throw e;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return out.getBuffer().toString();
    }
}
