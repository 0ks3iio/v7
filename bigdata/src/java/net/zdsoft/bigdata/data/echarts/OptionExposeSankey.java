package net.zdsoft.bigdata.data.echarts;

import com.google.common.collect.Lists;
import net.zdsoft.echarts.enu.Trigger;

import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 18-8-27 下午6:19
 */
public class OptionExposeSankey {

    private List<String> colors;
    private Boolean showTooltip;
    private String tooltipTrigger;
    private String tooltipBackgroundColor;
    private Integer tooltipBackgroundColorTransparent; //透明度，最大值是10 min 1 最终转换为小数
    private Integer tooltipBorderWidth;
    private String tooltipBorderColor;
    private Boolean tooltipConfine;
    private String tooltipFormatter;

    private OptionExposeTitle exposeTitle;

    public static OptionExposeSankey createDefault() {
        OptionExposeSankey default_oe = new OptionExposeSankey();
        default_oe.setShowTooltip(true);
        default_oe.setTooltipTrigger(Trigger.item.name());
        default_oe.setTooltipBackgroundColor("#323232");
        default_oe.setTooltipBackgroundColorTransparent(7);
        default_oe.setTooltipBorderWidth(0);
        default_oe.setTooltipBorderColor("#ccc");
        default_oe.setTooltipConfine(true);

        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
        default_oe.setColors(
                Lists.newArrayList("#1f83f5", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d")
        );
        Collections.shuffle(default_oe.getColors());
        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
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

    public Boolean getShowTooltip() {
        return showTooltip;
    }

    public void setShowTooltip(Boolean showTooltip) {
        this.showTooltip = showTooltip;
    }

    public String getTooltipTrigger() {
        return tooltipTrigger;
    }

    public void setTooltipTrigger(String tooltipTrigger) {
        this.tooltipTrigger = tooltipTrigger;
    }

    public String getTooltipBackgroundColor() {
        return tooltipBackgroundColor;
    }

    public void setTooltipBackgroundColor(String tooltipBackgroundColor) {
        this.tooltipBackgroundColor = tooltipBackgroundColor;
    }

    public Integer getTooltipBackgroundColorTransparent() {
        return tooltipBackgroundColorTransparent;
    }

    public void setTooltipBackgroundColorTransparent(Integer tooltipBackgroundColorTransparent) {
        this.tooltipBackgroundColorTransparent = tooltipBackgroundColorTransparent;
    }

    public Integer getTooltipBorderWidth() {
        return tooltipBorderWidth;
    }

    public void setTooltipBorderWidth(Integer tooltipBorderWidth) {
        this.tooltipBorderWidth = tooltipBorderWidth;
    }

    public String getTooltipBorderColor() {
        return tooltipBorderColor;
    }

    public void setTooltipBorderColor(String tooltipBorderColor) {
        this.tooltipBorderColor = tooltipBorderColor;
    }

    public Boolean getTooltipConfine() {
        return tooltipConfine;
    }

    public void setTooltipConfine(Boolean tooltipConfine) {
        this.tooltipConfine = tooltipConfine;
    }

    public String getTooltipFormatter() {
        return tooltipFormatter;
    }

    public void setTooltipFormatter(String tooltipFormatter) {
        this.tooltipFormatter = tooltipFormatter;
    }

}
