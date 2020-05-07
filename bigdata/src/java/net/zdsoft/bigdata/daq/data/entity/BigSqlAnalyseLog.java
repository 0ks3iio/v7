package net.zdsoft.bigdata.daq.data.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhujy
 * @since:2019/6/11 9:52
 */
public class BigSqlAnalyseLog implements Serializable {

    private String id;
    /**
     * 业务名称
     */
    String businessName;
    /**
     * 数据库类型 mysql 、es、 oracle等等
     */
    private String dbType;
    /**
     * sql语句
     */
    private String sql;
    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    /**
     * 耗时
     */
    private long duration;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
