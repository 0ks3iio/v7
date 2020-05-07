package net.zdsoft.bigdata.datav.vo;

import java.util.List;
import java.util.Objects;

/**
 * @author shenke
 * @since 2018/10/10 17:48
 */
public class DiagramParameterCategoryVo {

    private String categoryName;
    private Boolean array;
    private List<DiagramParameterVo> diagramParameterVos;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagramParameterCategoryVo that = (DiagramParameterCategoryVo) o;
        return Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }
}
