package net.zdsoft.bigdata.data.echarts;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenke
 * @since 2018/8/2 上午9:29
 */
public class OptionExposeMap {

    //通用设置

    private List<String> colors;
    private Boolean showScatter;
    private String scatterColor;
    private Integer scatterSize;
    private Boolean showGeoLabel;

    private OptionExposeTitle exposeTitle;



    public static OptionExposeMap getDefaultExpose() {
        OptionExposeMap default_oe = new OptionExposeMap();
        default_oe.setColors(Arrays.asList("#1f83f5","#d042a4","#1ebcd3","#9949d7","#ee913a","#3bb7f0","#cdb112","#b9396d"));
        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
        default_oe.setScatterSize(40);
        default_oe.setShowGeoLabel(true);
        default_oe.setShowScatter(false);
        return default_oe;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public OptionExposeTitle getExposeTitle() {
        return exposeTitle;
    }

    public void setExposeTitle(OptionExposeTitle exposeTitle) {
        this.exposeTitle = exposeTitle;
    }

    public Boolean getShowScatter() {
        return showScatter;
    }

    public void setShowScatter(Boolean showScatter) {
        this.showScatter = showScatter;
    }

    public String getScatterColor() {
        return scatterColor;
    }

    public void setScatterColor(String scatterColor) {
        this.scatterColor = scatterColor;
    }

    public Integer getScatterSize() {
        return scatterSize;
    }

    public void setScatterSize(Integer scatterSize) {
        this.scatterSize = scatterSize;
    }

    public Boolean getShowGeoLabel() {
        return showGeoLabel;
    }

    public void setShowGeoLabel(Boolean showGeoLabel) {
        this.showGeoLabel = showGeoLabel;
    }
}
