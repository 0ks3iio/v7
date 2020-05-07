package net.zdsoft.bigdata.datav.vo;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/10 17:47
 */
public class DiagramParameterGroupVo {

    private String groupName;
    private String groupKey;
    private Boolean array;
    private List<DiagramParameterCategoryVo> diagramParameterCategoryVos;
    private List<DiagramParameterVo> diagramParameterVos;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<DiagramParameterCategoryVo> getDiagramParameterCategoryVos() {
        return diagramParameterCategoryVos;
    }

    public void setDiagramParameterCategoryVos(List<DiagramParameterCategoryVo> diagramParameterCategoryVos) {
        this.diagramParameterCategoryVos = diagramParameterCategoryVos;
    }

    public List<DiagramParameterVo> getDiagramParameterVos() {
        return diagramParameterVos;
    }

    public void setDiagramParameterVos(List<DiagramParameterVo> diagramParameterVos) {
        this.diagramParameterVos = diagramParameterVos;
    }

    public Boolean getArray() {
        return array;
    }

    public void setArray(Boolean array) {
        this.array = array;
    }
}
