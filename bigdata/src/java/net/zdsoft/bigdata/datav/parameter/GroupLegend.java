package net.zdsoft.bigdata.datav.parameter;

/**
 * @author shenke
 * @since 2018/9/27 15:48
 */
public class GroupLegend {

    private Boolean legendShow;
    private String legendColor;
    private String legendOrient;
    private String legendPositionTransverse;
    private String legendPositionPortrait;

    public Boolean getLegendShow() {
        return legendShow;
    }

    public void setLegendShow(Boolean legendShow) {
        this.legendShow = legendShow;
    }

    public String getLegendColor() {
        return legendColor;
    }

    public void setLegendColor(String legendColor) {
        this.legendColor = legendColor;
    }

    public String getLegendOrient() {
        return legendOrient;
    }

    public void setLegendOrient(String legendOrient) {
        this.legendOrient = legendOrient;
    }

    public String getLegendPositionTransverse() {
        return legendPositionTransverse;
    }

    public void setLegendPositionTransverse(String legendPositionTransverse) {
        this.legendPositionTransverse = legendPositionTransverse;
    }

    public String getLegendPositionPortrait() {
        return legendPositionPortrait;
    }

    public void setLegendPositionPortrait(String legendPositionPortrait) {
        this.legendPositionPortrait = legendPositionPortrait;
    }
}
