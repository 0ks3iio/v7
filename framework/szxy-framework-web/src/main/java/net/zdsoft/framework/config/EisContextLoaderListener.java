package net.zdsoft.framework.config;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.zdsoft.framework.utils.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 功能描述：Spring启动时, WMS控制加载Spring配置文件
 * 
 * @author zhangza
 * @date 2009-11-2
 */
public class EisContextLoaderListener extends ContextLoaderListener {

    private static final Logger LOGGER = Logger.getLogger(EisContextLoaderListener.class);

    private static final String FW_DUBBO_LOAD_SERVER = "fw.dubbo.load.server";
    private static final String FW_DUBBO_LOAD_REF = "fw.dubbo.load.ref";
    private static final String EXTERNAL_RESOURCE_PROPERTY_NAME_SUFFIX = ".external";
    @Override
    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
        if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
            // The application context id is still set to its original default value
            // -> assign a more useful id based on available information
            String idParam = sc.getInitParameter(CONTEXT_ID_PARAM);
            if (idParam != null) {
                wac.setId(idParam);
            }
            else {
                // Generate default id...
                if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
                    // Servlet <= 2.4: resort to name specified in web.xml, if any.
                    wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
                            + ObjectUtils.getDisplayString(sc.getServletContextName()));
                }
                else {
                    wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
                            + ObjectUtils.getDisplayString(sc.getContextPath()));
                }
            }
        }

        wac.setServletContext(sc);
        String initParameter = assemblyInitParameter(sc.getInitParameter(CONFIG_LOCATION_PARAM));
        if (initParameter != null) {
            wac.setConfigLocation(initParameter);
        }
        customizeContext(sc, wac);
        wac.refresh();
    }

    /**
     * 对Spring启动设置开关加载配置文件
     * 
     * @param webXmlConfigInitParameter
     *            web.xml配置的Spring文件
     * @return
     */
    protected String assemblyInitParameter(String webXmlConfigInitParameter) {
        String initParameter = webXmlConfigInitParameter;

        Properties properties = null;
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:conf/conf.properties");
            properties = FileUtils.readProperties(resource.getInputStream());
            String externalResourceFile = properties.getProperty(resource.getFilename()
                    + EXTERNAL_RESOURCE_PROPERTY_NAME_SUFFIX);
            if (StringUtils.isNotBlank(externalResourceFile) && new File(externalResourceFile).exists()) {
                LOGGER.info("Found external resource file: " + externalResourceFile);
                Resource externalResource = new FileSystemResource(externalResourceFile);
                if (externalResource.isReadable()) {
                    LOGGER.info("External resource file will be read: " + externalResourceFile);
                    properties = FileUtils.readProperties(externalResource.getInputStream());
                }
            }

            if (null == properties) {
                throw new RuntimeException("spring启动-》加载conf配置文件失败!");
            }

        }
        catch (IOException e) {
            throw new RuntimeException("spring启动-》加载conf配置文件失败!", e);
        }

        // ResourceBundle bundle = null;
        // try {
        // bundle = ResourceBundle.getBundle("conf/conf");
        // if (bundle == null) {
        // throw new RuntimeException("spring启动-》加载conf配置文件失败!");
        // }
        // }
        // catch (Exception e) {
        // throw new RuntimeException("spring启动-》加载conf配置文件失败!", e);
        // }

        String[] configures = StringUtils.split(initParameter, ",");
        if (ArrayUtils.isEmpty(configures)) {
            throw new RuntimeException("spring启动-》配置Spring启动文件为空!");
        }
        String dubboLoadServer = properties.getProperty(FW_DUBBO_LOAD_SERVER);
        String dubboLoadRef = properties.getProperty(FW_DUBBO_LOAD_REF);
        String[] dubboLoadsServer = null;
        String[] dubboLoadsRef = null;
        if (StringUtils.isEmpty(dubboLoadServer)) {
            LOGGER.info("spring启动-》conf配置参数" + FW_DUBBO_LOAD_SERVER + "提供dubbo服务系统为空");
        }
        else {
            LOGGER.info("spring启动-》conf配置参数" + FW_DUBBO_LOAD_SERVER + "提供dubbo服务系统:" + dubboLoadServer);
            dubboLoadsServer = dubboLoadServer.split(",");
        }
        if (StringUtils.isEmpty(dubboLoadRef)) {
            LOGGER.info("spring启动-》conf配置参数" + FW_DUBBO_LOAD_REF + "需要接收的dubbo系统为空");
        }
        else {
            LOGGER.info("spring启动-》conf配置参数" + FW_DUBBO_LOAD_REF + "需要接收的dubbo系统:" + dubboLoadRef);
            dubboLoadsRef = dubboLoadRef.split(",");
        }
        // 获取过滤之后的配置文件
        List<String> filterConfigs = Lists.newArrayList();
        for (String configure : configures) {
            filterConfigs.add(configure);
        }
        filterConfigs.add("classpath*:conf/dubbo/dubbo.xml");
        if (ArrayUtils.isNotEmpty(dubboLoadsServer)) {
            for (String configure : dubboLoadsServer) {
                filterConfigs.add("classpath*:conf/dubbo/dubbo-" + configure + "-server.xml");
            }
        }

        if (ArrayUtils.isNotEmpty(dubboLoadsRef)) {
            for (String configure : dubboLoadsRef) {
                filterConfigs.add("classpath*:conf/dubbo/dubbo-" + configure + "-ref.xml");
            }
        }
        initParameter = Joiner.on(",").join(filterConfigs);

        return initParameter;
    }

    /**
     * 开关状态值
     */
    protected enum ContextLoaderListenerStatus {
        TRUE("true"), FALSE("false");
        private final String status;

        ContextLoaderListenerStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public static ContextLoaderListenerStatus getByStatus(String statusCode) {
            if (StringUtils.isEmpty(statusCode)) {
                return null;
            }

            for (ContextLoaderListenerStatus status : ContextLoaderListenerStatus.values()) {
                if (status.getStatus().equals(statusCode)) {
                    return status;
                }
            }
            return null;
        }
    }

}
