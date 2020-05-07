package net.zdsoft.bigdata.datasource.csv;

import net.zdsoft.bigdata.datasource.Adapter;
import net.zdsoft.bigdata.datasource.QueryStatement;

/**
 * @author shenke
 * @since 2018/11/27 下午2:33
 */
public class QueryStatementForExcel<A extends Adapter> extends QueryStatement<A> {

    private String suffix;

    public QueryStatementForExcel(A adapter, String queryStatement, String suffix) {
        super(adapter, queryStatement);
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
