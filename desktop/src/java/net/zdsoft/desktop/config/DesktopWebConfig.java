package net.zdsoft.desktop.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.14
 */
@Configuration
//@EnableWebMvc
@PropertySource("classpath:/conf/dingAp-common.properties")
public class DesktopWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccountSwitchInterceptor()).addPathPatterns("/fpf/switch/account","/homepage/switch/account");
    }

	//@Override
	//public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
	//	resolvers.add(pageableHandlerMethodArgumentResolver());
	//
	//}
	//
	//@Override
	//public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	//	converters.add(utf8StringHttpMessageConverter());
	//}
	//
	//@Bean
	//public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    //	return new PageableHandlerMethodArgumentResolver();
	//}
	//
	//@Bean
	//public UTF8StringHttpMessageConverter utf8StringHttpMessageConverter() {
	//	return new UTF8StringHttpMessageConverter();
	//}

	public ClientHttpRequestFactory clientHttpRequestFactory() {
		ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				HttpClientBuilder.create()
						.disableContentCompression()
						.setMaxConnTotal(10)
						.setMaxConnPerRoute(10)
						.build()
		);
		return clientHttpRequestFactory;
	}



	@Bean
	public RestTemplate restTemplate(){
    	RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
		Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
		HttpMessageConverter<?> converter;
		while ( iterator.hasNext()) {
			converter = iterator.next();
			if (StringHttpMessageConverter.class.equals(converter.getClass())) {
				break;
			} else {
				converter = null;
			}
		}
		converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
		converters.add(new FastJsonHttpMessageConverter4());
		return restTemplate;
	}
}
