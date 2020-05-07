package net.zdsoft.bigdata.data.echarts;

/**
 * 雷达图的指示器配置
 * @author shenke
 * @since 2018/7/30 上午10:17
 */
public class OptionExposeIndicator {

    private String indicatorName;
    private Double indicatorMax;
    private Double indicatorMin;
    private String indicatorColor;

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public Double getIndicatorMax() {
        return indicatorMax;
    }

    public void setIndicatorMax(Double indicatorMax) {
        this.indicatorMax = indicatorMax;
    }

    public Double getIndicatorMin() {
        return indicatorMin;
    }

    public void setIndicatorMin(Double indicatorMin) {
        this.indicatorMin = indicatorMin;
    }

    public String getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(String indicatorColor) {
        this.indicatorColor = indicatorColor;
    }
}
