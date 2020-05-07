package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2018/9/27 9:31
 */
@Entity
@Table(name = "bg_diagram_parameter_array")
public class DiagramParameterArray extends BaseEntity<String> {

    private Integer diagramType;
    private String groupKey;
    private String key;
    private String name;
    private String defaultValue;
    private Integer valueType;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public Integer getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(Integer diagramType) {
        this.diagramType = diagramType;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }
}
