package net.zdsoft.bigdata.datax.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdongdong on 2019/4/28 14:36.
 */
@Entity
@Table(name = "bg_datax_job_ins_param")
public class DataxJobInsParam extends BaseEntity<String> {

    private String jobId;

    private String jobInsId;

    private String paramKey;

    private String paramValue;

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

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataxJobParam";
    }
}
