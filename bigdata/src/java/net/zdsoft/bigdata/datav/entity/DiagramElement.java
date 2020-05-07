package net.zdsoft.bigdata.datav.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 大屏组件表
 * @author shenke
 * @since 2018/10/15 12:39
 */
@Entity
@Table(name = "bg_diagram_element")
public class DiagramElement extends AbstractDiagram {

    /**
     * 组件所属的图表id
     */
    private String diagramId;

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    @Override
    public DiagramElement clone()  {
        DiagramElement element = new DiagramElement();
        element.setDiagramId(diagramId);
        element.setId(getId());
        element.setDatasourceId(getDatasourceId());
        element.setDatasourceType(getDatasourceType());
        element.setDatasourceValueJson(getDatasourceValueJson());
        element.setDatasourceValueSql(getDatasourceValueSql());
        element.setDiagramType(getDiagramType());
        element.setUpdateInterval(getUpdateInterval());
        return element;
    }
}
