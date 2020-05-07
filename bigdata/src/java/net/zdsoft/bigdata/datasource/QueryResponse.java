package net.zdsoft.bigdata.datasource;

/**
 * 查询结果集
 * @author shenke
 * @since 2018/11/26 下午2:33
 */
public class QueryResponse<T> {

    /**
     * 查询结果
     */
    private T queryValue;
    /**
     * 查询可能出现的异常信息
     * 若查询无错误则该值为空
     */
    private Throwable error;

    public static <T> QueryResponse<T> error(Throwable throwable) {
        return new QueryResponse(throwable);
    }

    public QueryResponse(T queryValue) {
        this.queryValue = queryValue;
        this.error = null;
    }

    public QueryResponse(Throwable error) {
        this.error = error;
    }

    public T getQueryValue() {
        return queryValue;
    }

    public Throwable getError() {
        return error;
    }

    public boolean hasError() {
        return this.error != null;
    }
}
