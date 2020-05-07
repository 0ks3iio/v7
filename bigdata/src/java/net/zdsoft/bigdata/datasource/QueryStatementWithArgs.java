package net.zdsoft.bigdata.datasource;

/**
 * 带参数的查询参数
 * 一般用于rest redis
 * @author shenke
 * @since 2018/11/26 下午4:30
 */
public final class QueryStatementWithArgs<A extends Adapter> extends QueryStatement<A> {

    /**
     *
     */
    private Object[] args;

    public QueryStatementWithArgs(A adapter, String queryStatement, Object[] args) {
        super(adapter, queryStatement);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
