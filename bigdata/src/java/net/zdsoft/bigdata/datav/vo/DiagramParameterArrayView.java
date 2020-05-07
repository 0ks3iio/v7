package net.zdsoft.bigdata.datav.vo;

import net.zdsoft.bigdata.datav.entity.DiagramParameterSelect;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 9:34
 */
public class DiagramParameterArrayView {

    private String key;
    private String name;
    private String value;
    private List<DiagramParameterSelect> selects;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DiagramParameterSelect> getSelects() {
        return selects;
    }

    public void setSelects(List<DiagramParameterSelect> selects) {
        this.selects = selects;
    }
}
