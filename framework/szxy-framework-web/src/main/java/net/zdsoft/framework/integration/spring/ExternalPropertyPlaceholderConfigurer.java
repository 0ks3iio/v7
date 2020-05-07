/* 
 * @(#)ExternalPropertyPlaceholderConfigurer.java    Created on 2011-8-25
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.integration.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.framework.utils.Validators;

/**
 * 继承于 Spring Framework 的 <code>org.springframework.beans.factory.config.PropertyPlaceholderConfigurer</code> 的工具类。
 * 
 * <p>
 * 主要包含了两个扩展功能：
 * <ul>
 * <li>通过在配置文件中添加一个外部文件路径的配置，可以读取指定的外部配置文件，例如：jdbc.properties.external=/opt/jdbc.properties。</li>
 * <li>可以读取加密后的属性值，加强安全性，属性名必须添加 “.encrypt”
 * 后缀，例如：jdbc.password.encrypt=44NNSVYVHNWE3U23249L35KWVP76AWRP8P3Z5T9YC8NGTNETKB2W9GMTWURN3UF7。</li>
 * </ul>
 * 
 * 注意：目前加密算法只支持 {@link SecurityUtils#encodeBySelf(String)}。
 * 
 * <p>
 * Spring 文件配置样例：
 * 
 * <pre>
 * &lt;bean class="net.zdsoft.keel.integration.spring.ExternalPropertyPlaceholderConfigurer"&gt;
 *     &lt;property name="resourceFiles"&gt;
 *         &lt;value&gt;classpath:jdbc.properties&lt;/value&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * jdbc.properties 中的配置：
 * 
 * <pre>
 * <strong>jdbc.properties.external=/opt/jdbc/jdbc_passport.properties</strong>
 * jdbc.driverClassName=oracle.jdbc.OracleDriver
 * jdbc.url=jdbc:oracle:thin:@server.database.passport:1521:center
 * jdbc.username=passport
 * jdbc.password=zdsoft
 * </pre>
 * 
 * @author huangwj
 * @version $Revision$, $Date$
 * @since 0.5.2
 */
public class ExternalPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static final Logger logger = Logger.getLogger(ExternalPropertyPlaceholderConfigurer.class);

    private static final String ENCRYPTED_PROPERTY_NAME_SUFFIX = ".encrypt";
    private static final String EXTERNAL_RESOURCE_PROPERTY_NAME_SUFFIX = ".external";

    private String[] resourceFiles;
    private boolean propertyValueDecryptDisabled; // 是否对属性值关闭解密操作，默认开启

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Assert.notEmpty(resourceFiles);

        List<Resource> locations = new ArrayList<Resource>();
        for (String resourceFile : resourceFiles) {
            logger.debug("resourceFile: " + resourceFile);

            try {
//                ResourceLoader resourceLoader = new DefaultResourceLoader();
            	//用通配符的形式，加载配置文件
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    			//将加载多个绝对匹配的所有Resource
    			//将首先通过ClassLoader.getResource("META-INF")加载非模式路径部分
    			//然后进行遍历模式匹配
    			Resource[] resources = resolver.getResources(resourceFile); 
    			for(Resource resource : resources) {
	                Properties properties = FileUtils.readProperties(resource.getInputStream());
	                String externalResourceFile = properties.getProperty(resource.getFilename()
	                        + EXTERNAL_RESOURCE_PROPERTY_NAME_SUFFIX);
	                if (!Validators.isEmpty(externalResourceFile)) {
	                    logger.debug("Found external resource file: " + externalResourceFile);
	                    Resource externalResource = new FileSystemResource(externalResourceFile);
	                    if (externalResource.isReadable()) {
	                        logger.info("External resource file will be read: " + externalResourceFile);
	                        locations.add(externalResource);
	                    }
	                    else {
	                        logger.info("External resource file can't be read: " + externalResourceFile);
	                        locations.add(resource);
	                    }
	                }
	                else {
	                    locations.add(resource);
	                }
    			}
            }
            catch (IOException e) {
                logger.error("Read properties from resource(" + resourceFile + ") error", e);
            }
        }

        Assert.notEmpty(locations);
        setLocations(locations.toArray(new Resource[locations.size()]));

        super.postProcessBeanFactory(beanFactory);
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        Map<Object, Object> modifiedProperties = new HashMap<Object, Object>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String propertyName = (String) entry.getKey();
            Object propertyValue = props.get(propertyName);

            if (propertyName.endsWith(ENCRYPTED_PROPERTY_NAME_SUFFIX)) {
                String newPropertyName = propertyName.substring(0, propertyName.length()
                        - ENCRYPTED_PROPERTY_NAME_SUFFIX.length());
                modifiedProperties.put(newPropertyName, propertyValue);
            }
        }
        props.putAll(modifiedProperties);

        super.processProperties(beanFactoryToProcess, props);
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (propertyValueDecryptDisabled || !propertyName.endsWith(ENCRYPTED_PROPERTY_NAME_SUFFIX)) {
            return super.convertProperty(propertyName, propertyValue);
        }

        // 采用简单的自定义算法，现有情况下已经够用了
        return SecurityUtils.decodeBySelf(propertyValue);
    }

    /**
     * 设置资源文件路径，支持 classpath:、file: 前缀。
     * 
     * @param resourceFiles
     *            资源文件数组
     */
    public void setResourceFiles(String[] resourceFiles) {
        this.resourceFiles = resourceFiles;
    }

    /**
     * 是否对属性值关闭解密操作，默认开启。
     * 
     * @return true/false
     */
    public boolean isPropertyValueDecryptDisabled() {
        return propertyValueDecryptDisabled;
    }

    /**
     * 设置是否对属性值关闭解密操作，默认开启。
     * 
     * @param decryptDisabled
     *            是否对属性值关闭解密操作
     */
    public void setPropertyValueDecryptDisabled(boolean propertyValueDecryptDisabled) {
        this.propertyValueDecryptDisabled = propertyValueDecryptDisabled;
    }

    public static void main(String[] args) {
        // Resource externalResource = new FileSystemResource("I:\\opt\\server_data\\v7\\conf.properties");
        Resource externalResource = new FileSystemResource("/opt/server_data/v7/conf.properties");
        System.out.println(externalResource.isReadable());
    }

}
