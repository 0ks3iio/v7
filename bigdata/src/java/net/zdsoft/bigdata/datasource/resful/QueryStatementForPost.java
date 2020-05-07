package net.zdsoft.bigdata.datasource.resful;

import net.zdsoft.bigdata.datasource.Adapter;
import net.zdsoft.bigdata.datasource.QueryStatement;

/**
 * @author shenke
 * @since 2018/11/27 上午10:15
 */
public final class QueryStatementForPost<A extends Adapter> extends QueryStatement<A> {

    private Object request;

    public QueryStatementForPost(A adapter, String queryStatement, Object request) {
        super(adapter, queryStatement);
        this.request = request;
    }

    public Object getRequest() {
        return request;
    }
}
