package net.zdsoft.bigdata.datax.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdongdong on 2019/4/28 14:36.
 */
@Entity
@Table(name = "bg_datax_job_ins_rule")
public class DataxJobInsRule extends BaseEntity<String> {

    private String jobId;

    private String jobInsId;

    private String columnId;

    private String rule;

    private Integer orderId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobInsId() {
        return jobInsId;
    }

    public void setJobInsId(String jobInsId) {
        this.jobInsId = jobInsId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataxJobParam";
    }
}
