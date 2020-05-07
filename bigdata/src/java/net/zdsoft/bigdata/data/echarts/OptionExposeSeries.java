package net.zdsoft.bigdata.data.echarts;

/**
 *
 * @author shenke
 * @since 2018/7/25 下午6:26
 */
public class OptionExposeSeries {

    /**
     * 系列的名称
     */
    private String name;
    private String color;
    /**
     * 系列类型（可选的）
     */
    private String type;
    /**
     * 线条类型
     */
    private String lineType;
    private Boolean lineArea;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public Boolean getLineArea() {
        return lineArea;
    }

    public void setLineArea(Boolean lineArea) {
        this.lineArea = lineArea;
    }
}
