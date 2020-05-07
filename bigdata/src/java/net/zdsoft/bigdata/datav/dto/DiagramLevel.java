package net.zdsoft.bigdata.datav.dto;

import net.zdsoft.bigdata.datav.entity.DiagramParameter;

/**
 * @author shenke
 * @since 2019/5/22 下午8:44
 */
public final class DiagramLevel  {

    private String level;
    private String diagramId;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    /**
     *
     * @param parameter parameter must be level parameter
     * @return
     */
    public static DiagramLevel convert(DiagramParameter parameter) {
        DiagramLevel level = new DiagramLevel();
        level.setDiagramId(parameter.getDiagramId());
        level.setLevel(parameter.getValue());
        return level;
    }
}
