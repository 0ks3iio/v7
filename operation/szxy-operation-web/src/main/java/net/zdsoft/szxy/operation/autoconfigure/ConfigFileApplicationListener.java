package net.zdsoft.szxy.operation.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2019/1/28 上午9:16
 */
public class ConfigFileApplicationListener extends org.springframework.boot.context.config.ConfigFileApplicationListener {

    private DeferredLog logger = new DeferredLog();

    private List<PropertySourceLoader> loaders;


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        super.onApplicationEvent(event);
        if (event instanceof ApplicationPreparedEvent) {
            logger.replayTo(ConfigFileApplicationListener.class);
        }
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        super.postProcessEnvironment(environment, application);
        FileSystemResource resource = getExtensionConfigResource();
        logger.info("ExtensionConfig is " + resource.getPath());
        if (!resource.exists()) {
            logger.warn("ExtensionConfig not exists load default config");
            return;
        }
        try {
            loadExtension(environment, resource);
        } catch (IOException e) {
            throw new IllegalStateException("Szxy extension config load error", e);
        }
    }

    private FileSystemResource getExtensionConfigResource() {
        String applicationName = System.getProperty("spring.application.name", "operation-web");
        String extensionConfigPath = System.getProperty("spring.application.config.location", "/opt/server_data/" + applicationName);
        boolean separator = StringUtils.endsWith(extensionConfigPath, "/");
        return new FileSystemResource(extensionConfigPath + (separator ? "" : "/") + "application.properties");
    }

    private void loadExtension(ConfigurableEnvironment environment, Resource extensionConfigFile) throws IOException {
        if (!extensionConfigFile.exists()) {
            return;
        }

        List<PropertySource> propertySourceList = new ArrayList<>();
        for (PropertySourceLoader loader : getLoaders()) {
            for (String fileExtension : loader.getFileExtensions()) {
                if (extensionConfigFile.getFilename().contains(fileExtension)) {
                    propertySourceList.addAll(loader.load(extensionConfigFile.getFilename(), extensionConfigFile));
                }
            }
        }
        for (PropertySource propertySource : propertySourceList) {
            environment.getPropertySources().addFirst(propertySource);
        }
    }

    private List<PropertySourceLoader> getLoaders() {
        if (loaders == null) {
            loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
        }
        return loaders;
    }
}
