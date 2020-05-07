package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author shenke
 * @since 2018/10/15 13:41
 */
@MappedSuperclass
public abstract class AbstractDiagram extends BaseEntity<String> {
    /**
     * 组件类型
     */
    private Integer diagramType;
    /**
     * 数据源类型
     */
    private Integer datasourceType;
    /**
     * 数据源id
     */
    private String datasourceId;
    /**
     * sql 或者 json
     */
    @Column(length = 4000)
    private String datasourceValueSql;
    /**
     * sql 或者 json
     */
    @Column(length = 4000)
    private String datasourceValueJson;
    /**
     * 更新时间
     */
    private Integer updateInterval;

    public Integer getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(Integer diagramType) {
        this.diagramType = diagramType;
    }

    public Integer getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(Integer datasourceType) {
        this.datasourceType = datasourceType;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getDatasourceValueSql() {
        return datasourceValueSql;
    }

    public void setDatasourceValueSql(String datasourceValueSql) {
        this.datasourceValueSql = datasourceValueSql;
    }

    public String getDatasourceValueJson() {
        return datasourceValueJson;
    }

    public void setDatasourceValueJson(String datasourceValueJson) {
        this.datasourceValueJson = datasourceValueJson;
    }
}
