package net.zdsoft.bigdata.datasource.resful;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * @author shenke
 * @since 2018/11/27 上午9:51
 */
final class RestQueryUtils {

    static final Integer TIME_OUT = 30 * 1000;
    static RestTemplate restTemplate;


    static String doGet(String url, Object... args) {
        if (args == null || args.length==0) {
            ResponseEntity entity = createRestTemplate().getForEntity(RestUrlChecker.check(url), Object.class);
            return JSONObject.toJSONString(entity.getBody());
        }
        return JSONObject.toJSONString(
                createRestTemplate().getForEntity(RestUrlChecker.check(url), Object.class, args)
                );
    }

    static String doPost(String url, Object request) {
        return JSONObject.toJSONString(
                createRestTemplate().postForEntity(RestUrlChecker.check(url), request, Object.class).getBody()
                );
    }

    private static RestTemplate createRestTemplate() {
        if (restTemplate != null) {
            return restTemplate;
        }
        synchronized (RestQueryUtils.class) {
            if (restTemplate != null) {
                return restTemplate;
            }
            HttpComponentsClientHttpRequestFactory componentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                    HttpClientBuilder.create().disableContentCompression().setMaxConnTotal(10).setMaxConnPerRoute(10).build()
            );
            componentsClientHttpRequestFactory.setConnectTimeout(TIME_OUT);
            restTemplate = new RestTemplate(componentsClientHttpRequestFactory);
            List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
            Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
            HttpMessageConverter<?> converter;
            while (iterator.hasNext()) {
                converter = iterator.next();
                if (StringHttpMessageConverter.class.equals(converter.getClass())) {
                    iterator.remove();
                    break;
                }
            }
            converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
            converters.add(new FastJsonHttpMessageConverter4());
            restTemplate.setMessageConverters(converters);
            return restTemplate;
        }
    }
}
