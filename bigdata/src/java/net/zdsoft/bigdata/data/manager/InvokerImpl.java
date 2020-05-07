package net.zdsoft.bigdata.data.manager;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午12:39
 */
@Component("invoker")
public class InvokerImpl implements Invoker {

    private Logger logger = LoggerFactory.getLogger(InvokerImpl.class);

    private static ThreadLocal<Integer> autoRetryCounter = new ThreadLocal<>();
    private static Map<String, QueryEntity> autoCache = new ConcurrentHashMap<>(16);

    @Resource
    private IDataSourceService iDataSourceService;
    private List<Filter> filters;

    @PostConstruct
    public void initFilter() {
        ServiceLoader<Filter> loader = ServiceLoader.load(Filter.class);
        Iterator<Filter> filterIterator = loader.iterator();
        filters = Lists.newArrayList(filterIterator);
        if (logger.isDebugEnabled()) {
            logger.debug("Invoker Filter size {}", filters.size());
        }
    }

    @Override
    public Result invoke(Invocation invocation) {
        try {
            //过滤器处理
            applyFilter(invocation);
            return _invoke(invocation);
        } finally {
            autoRetryCounter.remove();
        }

    }

    private Result _invoke(Invocation invocation) {
        try {
            if (invocation.isAutoRetry() && autoRetryCounter.get() == null) {
                autoRetryCounter.set(invocation.autoRetries());
            }
            if (logger.isDebugEnabled()) {
                logger.debug("剩余重试次数 [{}]", Optional.ofNullable(autoRetryCounter.get()).orElse(0));
            }
            IDataSource iDataSource = iDataSourceService.getIDataSource(invocation.getDataSourceId(), invocation.getDataSourceType());
            //数据源不可用，重试一次
            if (!iDataSource.enable()) {
                logger.error("数据源 dataSourceId [{} {}] 不可用", invocation.getDataSourceId(), iDataSource.getUri());
                iDataSource.afterPropertiesSet();
            }
            // 报表查询时直接返回resultSet结果集
            if (invocation instanceof IReportQueryInvocation) {
                return new IResult(iDataSource.executeResultSet(invocation.getQueryStatement(), invocation.timeout()));
            }
            Object queryResult = iDataSource.executeQuery(invocation.getQueryStatement(), invocation.timeout());
            return new IResult(queryResult, null);
        } catch (Exception e) {
            return retry(invocation, e);
        }
    }

    private Result retry(Invocation invocation, Throwable e) {
        int retryCount;
        if ((retryCount = Optional.ofNullable(autoRetryCounter.get()).orElse(0)) == 0) {
            return new IResult(Optional.empty(), e);
        }
        autoRetryCounter.set(retryCount - 1);
        return _invoke(invocation);
    }

    private String buildKey(Invocation invocation) {
        QueryKey key = new QueryKey();
        key.dataSourceId = invocation.getDataSourceId();
        key.queryStatement = invocation.getQueryStatement();
        key.dataSourceType = invocation.getDataSourceType();
        return JSON.toJSONString(key);
    }

    private void applyFilter(Invocation invocation) {
        if (filters != null) {
            for (Filter filter : filters) {
                filter.doFilter(invocation);
            }
        }
    }

    static class QueryEntity {
        private static final QueryEntity empty = new QueryEntity(null, -1, -1);
        private Object data;
        private long creationTime;
        private long updateInterval;

        public QueryEntity() {

        }

        public Object getData() {
            return data;
        }

        public QueryEntity(Object data, long creationTime, long updateInterval) {
            this.data = data;
            this.creationTime = creationTime;
            this.updateInterval = updateInterval;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public long getUpdateInterval() {
            return updateInterval;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }

        public void setUpdateInterval(long updateInterval) {
            this.updateInterval = updateInterval;
        }

        public boolean isExpired() {
            return (this.creationTime == -1 || this.updateInterval == -1)
                    || (System.currentTimeMillis() - this.creationTime > this.updateInterval);
        }
    }

    static class QueryKey implements Serializable {
        private String queryStatement;
        private String dataSourceId;
        private DataSourceType dataSourceType;

        public String getQueryStatement() {
            return queryStatement;
        }

        public String getDataSourceId() {
            return dataSourceId;
        }

        public DataSourceType getDataSourceType() {
            return dataSourceType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            QueryKey queryKey = (QueryKey) o;
            return Objects.equals(queryStatement, queryKey.queryStatement) &&
                    Objects.equals(dataSourceId, queryKey.dataSourceId) &&
                    dataSourceType == queryKey.dataSourceType;
        }

        @Override
        public int hashCode() {

            return Objects.hash(queryStatement, dataSourceId, dataSourceType);
        }
    }
}
