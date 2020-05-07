package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:20.
 */
public class JobContentParameterConnection {

    private List<String> table;

    private List<String> querySql;

    public List<String> getTable() {
        return table;
    }

    public JobContentParameterConnection setTable(List<String> table) {
        this.table = table;
        return this;
    }

    public List<String> getQuerySql() {
        return querySql;
    }

    public JobContentParameterConnection setQuerySql(List<String> querySql) {
        this.querySql = querySql;
        return this;
    }

}
