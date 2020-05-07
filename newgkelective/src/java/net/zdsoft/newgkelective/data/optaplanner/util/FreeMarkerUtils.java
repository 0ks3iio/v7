package net.zdsoft.newgkelective.data.optaplanner.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.context.support.ServletContextResourceLoader;

import javax.servlet.ServletContext;

public class FreeMarkerUtils {
	Configuration configuration = new Configuration();  
	Template template;

	public FreeMarkerUtils(String templateFilePath) {
        configuration.setDefaultEncoding("utf-8");  
        TemplateLoader templateLoader = new SpringTemplateLoader(new DefaultResourceLoader(FreeMarkerUtils.class.getClassLoader()),
        		"/");
        configuration.setTemplateLoader(templateLoader);  
        try {
        	template = configuration.getTemplate(templateFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public FreeMarkerUtils(ServletContext servletContext,String templateFilePath) {
        configuration.setDefaultEncoding("utf-8");
        TemplateLoader templateLoader = new SpringTemplateLoader(new ServletContextResourceLoader(servletContext),
        		"/");
        configuration.setTemplateLoader(templateLoader);
        try {
        	template = configuration.getTemplate(templateFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FreeMarkerUtils freeMarkerUtils = new FreeMarkerUtils("/businessconf/solverv3/scheduling/studentCourseSchedulingSolverConfig_full.xml");
		
		freeMarkerUtils.process(new HashMap<>(), new OutputStreamWriter(System.out));
	}
	
	public void process(Map map, Writer writer) {
		if(map == null) {
			map = new HashMap<>();
		}
		try {
			template.process(map, writer);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void process(Map map, File file) {
		if(map == null) {
			map = new HashMap<>();
		}
		Writer writer = null;
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file);
			process(map, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
