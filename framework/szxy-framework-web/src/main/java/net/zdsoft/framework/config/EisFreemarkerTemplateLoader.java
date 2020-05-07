/*
* Project: v7
* Author : shenke
* @(#) EisFreemarkerTemplateLoader.java Created on 2017-3-9
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.framework.config;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PatternMatchUtils;

import com.google.common.collect.Maps;

import freemarker.cache.TemplateLoader;
import net.zdsoft.framework.utils.ArrayUtil;

/**
 * @author: shenke
 */
public class EisFreemarkerTemplateLoader implements TemplateLoader{

	private static final Logger logger = LoggerFactory.getLogger(EisFreemarkerTemplateLoader.class);
	private static final String macro = "<@w.commonWeb showFramework=showFramework desktopIndex=desktopIndex >";
	
	private Map<String,String> templateCache = Maps.newConcurrentMap();
	private Map<String,Long> templateTimeCache = Maps.newConcurrentMap();

	private List<String> excludePaths ;
	private List<String> templateLoaderPaths ;
	private List<String> includeSubSystems;

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		//TODO
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		long start = System.currentTimeMillis();
		try { 
			@SuppressWarnings("deprecation")
			String filePath = Evn.getRequest().getRealPath("/"+name);
			File file = new File(filePath);
			if(file.exists()){
				long last = System.currentTimeMillis();
				String source = templateCache.get(name);
				Long lastModifitied = templateTimeCache.get(name);
				if (!templateTimeCache.containsKey(name) || !templateCache.containsKey(name)){
					templateCache.put(name, source = readFile(file, "UTF-8", isInclude(name)));
					templateTimeCache.put(name, file.lastModified());
				}else if(templateTimeCache != null && lastModifitied.longValue() < file.lastModified()){
					templateCache.put(name, source = readFile(file, "UTF-8", isInclude(name)));
					templateTimeCache.put(name, last = file.lastModified());
				}
				EisTemplateSource eisTemplateSource = new EisTemplateSource(name, source, last);
				//FileUtils.writeStringToFile(new File("E:\\a.ftl"), source);
				file = null;
				return eisTemplateSource;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			logger.info("解析模板["+name+"]用时:"+(System.currentTimeMillis() - start)/1000+"s");
		}
		return null;
	}

	@Override
	public long getLastModified(Object templateSource) {
		if(templateSource instanceof EisTemplateSource){
			return ((EisTemplateSource)templateSource).getLastModified();
		}
		return System.currentTimeMillis();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		if(templateSource instanceof EisTemplateSource){
			return new StringReader(((EisTemplateSource)templateSource).getSource());
		}
		return null;
	}

	private String readFile(File file, String encoding) throws IOException{
		return FileUtils.readFileToString(file, encoding);
	}
	private String readFile(File file, String encoding, boolean addMacro) throws IOException{
	    String template = readFile(file, encoding);
	    if(addMacro){
            if (StringUtils.contains(template, macro)){
                return template;
            }
            else{
            	String importMacro = "";
            	String webMacro = "<#import  \"/fw/macro/webmacro.ftl\" as w />";
            	if(StringUtils.contains(template, webMacro)){
            		template  = StringUtils.replace(template, webMacro, "");
            	}
            	importMacro += webMacro + " \n";
            	if(!StringUtils.contains(template, "/popupMacro.ftl")){
            		importMacro += "<#import \"/fw/macro/popupMacro.ftl\" as p />  \n";
            	}
            	String uri = Evn.getRequest().getRequestURI();
            	if(StringUtils.startsWith(uri, "/dc/report/") && !StringUtils.contains(template, "/dcmacro.ftl")){
            		importMacro += "<#import \"/fw/macro/dcmacro.ftl\" as dcm />  \n";
            	}
            	template = importMacro + "<@w.commonWeb showFramework=showFramework desktopIndex=desktopIndex > \n" + template + "\n</@w.commonWeb>";
            }
        }  
        return template;
    }
    private boolean isInclude(String name) {
//	    boolean include = true;
//        if(CollectionUtils.isNotEmpty(includeSubSystems)) {
//            for (String subSystem : includeSubSystems) {
//                if (name.startsWith(subSystem)) {
//                    include = true;
//                    break;
//                }
//            }
//        }
//        include = PatternMatchUtils.simpleMatch(ArrayUtil.toArray(templateLoaderPaths),name)||include;
        return !PatternMatchUtils.simpleMatch(ArrayUtil.toArray(excludePaths),name);
    }

    public void setExcludePaths(String... excludePaths){
        this.excludePaths = Arrays.asList(excludePaths);
    }
    public void setTemplateLoaderPaths(String... templateLoaderPaths){
        this.templateLoaderPaths = Arrays.asList(templateLoaderPaths);
    }
    public void setIncludeSubSystems(String... includeSubSystems){
        this.includeSubSystems = Arrays.asList(includeSubSystems);
    }
}
class EisTemplateSource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1845579289646234903L;
	
	private String name;
	private String source;
	private long lastModified;
	
	
	public EisTemplateSource(String name, String source, long lastModified) {
		super();
		this.name = name;
		this.source = source;
		this.lastModified = lastModified;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof EisTemplateSource && this.name.equals(((EisTemplateSource)obj).getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
}
