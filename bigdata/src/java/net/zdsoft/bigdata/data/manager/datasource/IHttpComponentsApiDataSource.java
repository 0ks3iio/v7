package net.zdsoft.bigdata.data.manager.datasource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.api.Invocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用的API数据源，内部利用RestTemplate封装了apache httpComponents的HttpClient实现
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 下午5:33
 */
@Component(value = IHttpComponentsApiDataSource.API_DATASOURCE)
public class IHttpComponentsApiDataSource extends AbstractDataSource {

    public static final String API_DATASOURCE = "apiDataSource";

    private Logger logger;

    private static ThreadLocal<Long> timeoutClients = ThreadLocal.withInitial(() -> Invocation.QUERY_TIME_OUT);

    private RestTemplate restTemplate;
    private boolean enable = true;
    private String stackTrace;

    public IHttpComponentsApiDataSource() {
        super(DataSourceType.API);
    }

    @Override
    public void afterPropertiesSet() throws IDataSourceInitException {
        try {
            HttpComponentsClientHttpRequestFactory componentsClientHttpRequestFactory = new IHttpComponentsClientHttpRequestFactory(
                    HttpClientBuilder.create().disableContentCompression().setMaxConnTotal(10).setMaxConnPerRoute(10).build()
            );
            componentsClientHttpRequestFactory.setConnectTimeout((int) Invocation.QUERY_TIME_OUT);
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
        } catch (Exception e) {
            enable = false;
            stackTrace = ExceptionUtils.getStackTrace(e);
            throw new IDataSourceInitException("IApi dataSource init exception", e, null);
        }
    }

    @Override
    public Object executeQuery(String queryStatement) throws IQueryException {
        return executeQuery(queryStatement, Invocation.QUERY_TIME_OUT);
    }

    /**
     * 若指定超时时间和默认超时时间不符合，会重新创建HttpClinet，可能会造成速度变慢
     */
    @Override
    public Object executeQuery(String queryStatement, long timeout) throws IQueryException {
        try {
            if (!enable) {
                throw new IQueryException("Api dataSource not enable", null, queryStatement);
            }
            Assert.notNull(queryStatement);
            if (!StringUtils.startsWithIgnoreCase(queryStatement, "http")) {
                throw new IQueryException("不合法的URL", null, queryStatement);
            }
            if (timeout > 0) {
                timeoutClients.set(timeout);
            } else {
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("http Api [{}] timeout [{}]", queryStatement, timeout);
                }
                timeoutClients.set(Invocation.QUERY_TIME_OUT);
            }
            // 去除单引号
            queryStatement = queryStatement.replaceAll("'", "");
            return JSONObject.toJSONString(restTemplate.getForObject(queryStatement, Object.class));
        } finally {
            timeoutClients.remove();
        }
    }

    @Override
    protected Logger getLogger() {
        if (this.logger != null) {
            return logger;
        }
        synchronized (restTemplate) {
            if (logger == null) {
                logger = LoggerFactory.getLogger(IHttpComponentsApiDataSource.class);
            }
        }
        return logger;
    }

    @Override
    public void destroy() throws IQueryException {
        //TODO restTemplate.getRequestFactory()
    }

    @Override
    public boolean enable() {
        return this.enable;
    }

    @Override
    public String getInitStackTrace() {
        return stackTrace;
    }

    static class IHttpComponentsClientHttpRequestFactory extends org.springframework.http.client.HttpComponentsClientHttpRequestFactory {

        public Map<Long, HttpClient> httpClientMap = new ConcurrentHashMap<>(16);

        public IHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
            super(httpClient);
        }

        @Override
        public HttpClient getHttpClient() {
            Long timeout;
            if (Invocation.QUERY_TIME_OUT == (timeout = timeoutClients.get())) {
                return super.getHttpClient();
            }
            HttpClient httpClient;
            if ((httpClient = httpClientMap.get(timeout)) != null) {
                httpClient = HttpClientBuilder.create().disableContentCompression()
                        .setMaxConnTotal(10).setMaxConnPerRoute(10).build();
                httpClientMap.put(timeout, httpClient);
                return httpClient;
            }
            return httpClient;
        }
    }
}
