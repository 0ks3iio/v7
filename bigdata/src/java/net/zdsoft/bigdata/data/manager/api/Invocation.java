package net.zdsoft.bigdata.data.manager.api;

import net.zdsoft.bigdata.data.DataSourceType;

/**
 * 执行数据查询时需要的参数对象，
 * 该对象应该封装 查询类型、查询语句、url、等相关的参数
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 上午11:27
 */
public abstract class Invocation {

    /**
     * 默认超时时间为30 000 ms
     */
    public static final long QUERY_TIME_OUT = 30 * 1000;
    /**
     * 自动更新时间 30s
     */
    public static final long AUTO_UPDATE_INTERVAL = 30;

    /**
     * 查询类型
     */
    public abstract DataSourceType getDataSourceType();

    /**
     * 自适用查询语句 针对数据库为SQL语句、文件则为路径、API则为接口url
     */
    public abstract String getQueryStatement();

    public void setQueryStatement(String queryStatement) {
        throw new RuntimeException("This method should be override");
    }

    /**
     * 查询针对http和数据库设置超时时间单位是ms
     */
    public abstract long timeout();

    /**
     * 数据源Id，该id的值就是数据库中配置的数据源Id，也可能为Api Id
     */
    public abstract String getDataSourceId();

    /**
     * 是否自动更新
     */
    public abstract boolean isAutoUpdate();

    /**
     * 更新频率 s
     */
    public abstract long updateInterval();

    /**
     * 是否自动重试 ex：timeout or connection closed
     */
    public abstract boolean isAutoRetry();

    /**
     * 自动重试次数 不包括该次调用
     */
    public abstract int autoRetries();

    public interface InvocationBuilder {

        /**
         * 数据源类型
         */
        InvocationBuilder type(DataSourceType dataSourceType);

        /**
         * sql mdx api
         */
        InvocationBuilder queryStatement(String statement);

        /**
         * 超时时间 单位ms
         */
        InvocationBuilder timeout(long timeout);

        /**
         * 数据源Id或者Api Id
         */
        InvocationBuilder dataSourceId(String id);

        /**
         * 失败重试次数
         */
        InvocationBuilder autoRetries(int autoRetries);

        /**
         * 更新频率 单位秒
         */
        InvocationBuilder updateInterval(long updateInterval);

        Invocation build();
    }
}
