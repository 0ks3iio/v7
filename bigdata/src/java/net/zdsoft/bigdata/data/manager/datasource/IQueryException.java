package net.zdsoft.bigdata.data.manager.datasource;

/**
 * 执行查询的时候可能出现的异常封装，一般为IO和SQL异常较多
 *
 * @author ke_shen@126.com
 * @since 2018/4/9 上午10:01
 */
public class IQueryException extends Exception {

    private String uri;
    private String queryStatement;

    public IQueryException(String message, String uri, String queryStatement) {
        super(message);
        this.uri = uri;
        this.queryStatement = queryStatement;
    }

    public IQueryException(String message, Throwable cause, String uri, String queryStatement) {
        super(message, cause);
        this.uri = uri;
        this.queryStatement = queryStatement;
    }

    public String getUri() {
        return uri;
    }

    public String getQueryStatement() {
        return queryStatement;
    }
}
