package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:20.
 */
public class JobContentReaderParameterConnection extends JobContentParameterConnection {

    private List<String> jdbcUrl;

    public List<String> getJdbcUrl() {
        return jdbcUrl;
    }

    public JobContentReaderParameterConnection setJdbcUrl(List<String> jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }
}
