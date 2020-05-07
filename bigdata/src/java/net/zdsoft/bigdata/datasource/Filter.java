package net.zdsoft.bigdata.datasource;

/**
 * 在Query之前执行
 * @author shenke
 * @since 2018/11/26 下午4:23
 */
public interface Filter {

    void doFilter(QueryStatement queryStatement);
}
