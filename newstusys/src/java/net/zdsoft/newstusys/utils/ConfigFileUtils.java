/* 
 * @(#)FilePathUtil.java    Created on Aug 31, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 配置文件路径
 * 
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Aug 31, 2010 2:27:18 PM $
 */
public class ConfigFileUtils {
    private static final String BUSINESS_CONF = "businessconf/";
    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();
    private static final ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

    /**
     * 取相对于classes下的配置文件资源
     * 
     * @param parseFile
     * @return
     */
    private static Resource getResource(String parseFile) {
        Resource res = RESOURCE_LOADER.getResource(ResourceLoader.CLASSPATH_URL_PREFIX
                + File.separator + parseFile);
        return res;
    }

    /**
     * 取相对于classes下的配置文件流
     * 
     * @param parseFile
     * @return
     */
    public static InputStream getFileStream(String parseFile) {
        Resource res = getResource(parseFile);
        InputStream in = null;
        try {
            in = res.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
    
    /**
     * 取配置文件资源
     * 
     * @param parseFile
     * @return
     */
    private static Resource getConfigResource(String parseFile) {
        Resource res = RESOURCE_LOADER.getResource(ResourceLoader.CLASSPATH_URL_PREFIX
                + BUSINESS_CONF + parseFile);
        return res;
    }
    
    /**
     * 
     * @param parseFile
     * @return
     */
    public static File getConfigFile(String parseFile){
    	Resource res = getConfigResource(parseFile);
    	try {
			return res.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 取配置文件流
     * 
     * @param parseFile
     * @return
     */
    public static InputStream getConfigFileStream(String parseFile) {
        Resource res = getConfigResource(parseFile);
        InputStream in = null;
        try {
            in = res.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * 取配置url
     * 
     * @param parseFile
     * @return
     */
    public static URL getConfigFileUrl(String parseFile) {
        Resource res = getConfigResource(parseFile);
        URL url = null;
        try {
            url = res.getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 取配置文件资源
     * 
     * @param parseFile 含有通配符
     * @return
     */
    private static Resource[] getConfigResources(String parseFile) {
        Resource[] res = null;
        try {
            res = RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + BUSINESS_CONF + parseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * 取配置url
     * 
     * @param parseFile
     * @return
     */
    public static List<URL> getConfigFileUrls(String parseFile) {
        List<URL> list = new ArrayList<URL>();

        Resource[] reses = getConfigResources(parseFile);
        if (null != reses) {
            for (Resource res : reses) {
                try {
                    URL url = res.getURL();
                    list.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    
    /**
     * 取配置url
     * 
     * @param parseFile
     * @return
     */
    public static List<InputStream> getConfigFileStreams(String parseFile) {
        List<InputStream> list = new ArrayList<InputStream>();

        Resource[] reses = getConfigResources(parseFile);
        if (null != reses) {
            for (Resource res : reses) {
                try {
                    InputStream is = res.getInputStream();
                    list.add(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
