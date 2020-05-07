package net.zdsoft.bigdata.datax.entity;

/**
 * Created by wangdongdong on 2019/4/26 14:20.
 */
public class JobContentWriterParameterConnection extends JobContentParameterConnection {

    private String jdbcUrl;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public JobContentWriterParameterConnection setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }
}
