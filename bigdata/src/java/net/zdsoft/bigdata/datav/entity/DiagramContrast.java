package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Diagram参数对照表，记录每种diagram的类型、分组、分类、demo数据
 * @author shenke
 * @since 2018/9/28 14:16
 */
@Entity
@Table(name = "bg_diagram_contrast")
public class DiagramContrast extends BaseEntity<String> {

    private Integer type;
    private String name;
    private String groupName;
    private String category;
    private String demoData;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDemoData() {
        return demoData;
    }

    public void setDemoData(String demoData) {
        this.demoData = demoData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
