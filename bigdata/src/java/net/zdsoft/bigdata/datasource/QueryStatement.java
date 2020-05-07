package net.zdsoft.bigdata.datasource;

/**
 * 查询用的通用参数
 * @author shenke
 * @since 2018/11/26 下午1:56
 */
public class QueryStatement<A extends Adapter> extends Statement<A> {


    /**
     * 当用于不同的查询时，该参数的含义也不相同
     * <pre>
     *   1、用于jdbc查询时=> SQL
     *   2、用于reids查询时=> key 或者 lua script
     *   3、用于rest查询时=> url
     *   4、用于文件型（excel、csv）=> 文件绝对路径
     * <pre/>
     */
    private String queryStatement;

    public QueryStatement(A adapter, String queryStatement) {
        super(adapter);
        this.queryStatement = queryStatement;
    }

    public String getQueryStatement() {
        return queryStatement;
    }

    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }
}
