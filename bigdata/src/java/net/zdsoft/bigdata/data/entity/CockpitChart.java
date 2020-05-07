package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ke_shen@126.com
 * @since 2018/4/9 下午6:10
 */
@Entity
@Table(name = "bg_cockpit_chart")
public class CockpitChart extends BaseEntity<String> {

    @Column(name = "cp_id")
    private String cockpitId;
    private String divId;
    private String divName;
    private String chartId;
    @Column(name = "order_id")
    private Integer order;
    //样式


    public String getCockpitId() {
        return cockpitId;
    }

    public void setCockpitId(String cockpitId) {
        this.cockpitId = cockpitId;
    }

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "CockpitChart{" +
                "cockpitId='" + cockpitId + '\'' +
                ", divId='" + divId + '\'' +
                ", divName='" + divName + '\'' +
                ", chartId='" + chartId + '\'' +
                ", order=" + order +
                '}';
    }

    @Override
    public String fetchCacheEntitName() {
        return "cockpitChart";
    }
}
