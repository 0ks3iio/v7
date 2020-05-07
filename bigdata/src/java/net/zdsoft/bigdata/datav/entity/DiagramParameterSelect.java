package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2018/9/26 16:50
 */
@Entity
@Table(name = "bg_diagram_parameter_select")
public class DiagramParameterSelect extends BaseEntity<String> {

    private Integer valueType;
    private String humanText;
    private String javaCode;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public String getHumanText() {
        return humanText;
    }

    public void setHumanText(String humanText) {
        this.humanText = humanText;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }
}
