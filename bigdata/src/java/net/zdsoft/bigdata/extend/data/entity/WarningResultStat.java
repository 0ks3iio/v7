package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdongdong on 2018/7/12 17:00.
 */
@Entity
@Table(name = "bg_warning_result_stat")
public class WarningResultStat extends BaseEntity<String> {

    private String unitId;

    private String projectId;

    private Long statResult;

    @Override
    public String fetchCacheEntitName() {
        return "warningResultStat";
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Long getStatResult() {
        return statResult;
    }

    public void setStatResult(Long statResult) {
        this.statResult = statResult;
    }
}
