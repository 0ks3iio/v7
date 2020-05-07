package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 大屏diagram参数表
 *
 * @author shenke
 * @since 2018/9/26 10:48
 */
@Entity
@Table(name = "bg_diagram_parameter")
public class DiagramParameter extends BaseEntity<String> {

    /**
     *图表id
     */
    private String diagramId;
    /**
     * 参数key
     */
    private String key;
    /**
     * 参数值
     */
    private String value;
    /**
     * 系列名称
     */
    private String arrayName;
    /**
     * 分组key
     */
    private String groupKey;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "diagram_id", insertable = false, updatable = false)
    private Diagram diagram;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName = arrayName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public DiagramParameter clone() {
        DiagramParameter diagramParameter = new DiagramParameter();
        diagramParameter.setDiagramId(diagramId);
        diagramParameter.setValue(value);
        diagramParameter.setGroupKey(groupKey);
        diagramParameter.setKey(key);
        diagramParameter.setArrayName(arrayName);
        diagramParameter.setId(this.getId());
        return diagramParameter;
    }
}
