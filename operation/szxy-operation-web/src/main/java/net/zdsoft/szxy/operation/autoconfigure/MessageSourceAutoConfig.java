//package net.zdsoft.szxy.operation.autoconfigure;
//
//import net.zdsoft.szxy.utils.ThrowAny;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.AbstractResourceBasedMessageSource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.i18n.CookieLocaleResolver;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Enumeration;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//
///**
// * @author shenke
// * @since 2019/1/11 下午3:58
// */
//@Configuration
//public class MessageSourceAutoConfig extends MessageSourceAutoConfiguration {
//
//    private static final String szxy_i18n = "i18n/";
//    private Logger logger = LoggerFactory.getLogger(MessageSourceAutoConfig.class);
//
//    @Override
//    public MessageSource messageSource() {
//        MessageSource messageSource = super.messageSource();
//        try {
//            Set<String> basenames = getBasenames();
//            logger.info("Szxy messageSource load {}", basenames);
//            logger.warn("SpringBoot origin messageSources is abandoned {}",
//                    ((AbstractResourceBasedMessageSource) messageSource).getBasenameSet());
//            ((AbstractResourceBasedMessageSource) messageSource).setBasenames(basenames.toArray(new String[0]));
//            return messageSource;
//        } catch (IOException e) {
//            ThrowAny.throwUnchecked(e);
//        }
//        return messageSource;
//    }
//
//    private Set<String> getBasenames() throws IOException {
//        Set<String> basenames = new HashSet<>();
//        Enumeration<URL> i18nDirectories = this.getClass().getClassLoader().getResources(szxy_i18n);
//        Resource resource;
//        while (i18nDirectories.hasMoreElements()) {
//            resource = new UrlResource(i18nDirectories.nextElement());
//            try {
//                if (resource.exists() && resource.getFile().listFiles() != null) {
//                    for (File file : resource.getFile().listFiles()) {
//                        basenames.add(subBasename(file.getName()));
//                    }
//                }
//            } catch (IOException e) {
//                try {
//                    getJarBasenames(resource.getURL(), basenames);
//                } catch (IOException e1) {
//                    // if  use spring boot jar can't access
//                    // fall back
//
//                }
//            }
//        }
//        return basenames;
//    }
//
//    private void getFromSpringBootJar() {
//
//    }
//
//    private void getJarBasenames(URL jarURL, Set<String> basenames) throws IOException {
//        String jarPath = jarURL.toString().replace("jar:file:", "")
//                .replace("!/i18n/", "");
//        JarFile jarFile = new JarFile(new File(jarPath));
//        Enumeration<JarEntry> enumeration = jarFile.entries();
//        while (enumeration.hasMoreElements()) {
//            String entryName = enumeration.nextElement().getName();
//            if (entryName.startsWith(szxy_i18n) && entryName.endsWith(".properties")) {
//                basenames.add(ignoreLanguage(entryName));
//            }
//        }
//    }
//
//    private String subBasename(String name) {
//        return szxy_i18n + ignoreLanguage(name);
//    }
//
//    private String ignoreLanguage(String name) {
//        if (StringUtils.contains(name, "_")) {
//            return name.substring(0, name.indexOf("_"));
//        }
//        else {
//            return name.substring(0, name.lastIndexOf('.'));
//        }
//    }
//
//    @Bean(name = DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME)
//    public LocaleResolver szxyLocaleResolver() {
//        return new CookieLocaleResolver();
//    }
//}
