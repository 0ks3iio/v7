package net.zdsoft.bigdata.datav.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author shenke
 * @since 2018/10/17 11:25
 */
@Entity
@Table(name = "bg_diagram_element_contrast")
public class DiagramElementContrast extends BaseEntity<String> {

    private String name;
    private Integer rootDiagramType;
    private Integer elementDiagramType;
    @Column(length = 4000)
    private String demoData;

    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public Integer getRootDiagramType() {
        return rootDiagramType;
    }

    public void setRootDiagramType(Integer rootDiagramType) {
        this.rootDiagramType = rootDiagramType;
    }

    public Integer getElementDiagramType() {
        return elementDiagramType;
    }

    public void setElementDiagramType(Integer elementDiagramType) {
        this.elementDiagramType = elementDiagramType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDemoData() {
        return demoData;
    }

    public void setDemoData(String demoData) {
        this.demoData = demoData;
    }
}
