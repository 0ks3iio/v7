package net.zdsoft.bigdata.datav.vo;

/**
 * @author shenke
 * @since 2018/10/12 11:19
 */
public class DiagramVo {

    private Integer updateInterval;
    private String diagramId;
    private Integer diagramType;
    private Integer width;
    private Integer height;
    private Integer x;
    private Integer y;
    private Integer level;
    private String backgroundColor;

    //分组查看
    private String toScreenId;

    public String getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(String diagramId) {
        this.diagramId = diagramId;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(Integer diagramType) {
        this.diagramType = diagramType;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getToScreenId() {
        return toScreenId;
    }

    public void setToScreenId(String toScreenId) {
        this.toScreenId = toScreenId;
    }
}
