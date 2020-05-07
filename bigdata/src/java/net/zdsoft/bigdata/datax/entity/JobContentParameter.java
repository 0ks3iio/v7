package net.zdsoft.bigdata.datax.entity;

import java.util.List;

/**
 * Created by wangdongdong on 2019/4/26 14:58.
 */
public class JobContentParameter {

    /**
     * 数据库连接用户名
     */
    private String username;

    /**
     * 数据库连接密码
     */
    private String password;

    private List<JobContentParameterConnection> connection;

    public String getUsername() {
        return username;
    }

    public JobContentParameter setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public JobContentParameter setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<JobContentParameterConnection> getConnection() {
        return connection;
    }

    public JobContentParameter setConnection(List<JobContentParameterConnection> connection) {
        this.connection = connection;
        return this;
    }
}
