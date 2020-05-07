package net.zdsoft.bigdata.datav.vo;

import net.zdsoft.bigdata.datav.entity.DiagramParameterSelect;

import java.util.List;
import java.util.Objects;

/**
 * @author shenke
 * @since 2018/10/10 17:48
 */
public class DiagramParameterVo {

    private String name;
    private String key;
    private String value;
    private Integer valueType;
    private List<DiagramParameterSelect> selects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public List<DiagramParameterSelect> getSelects() {
        return selects;
    }

    public void setSelects(List<DiagramParameterSelect> selects) {
        this.selects = selects;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagramParameterVo that = (DiagramParameterVo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(key, that.key) &&
                Objects.equals(valueType, that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, key, valueType);
    }
}
