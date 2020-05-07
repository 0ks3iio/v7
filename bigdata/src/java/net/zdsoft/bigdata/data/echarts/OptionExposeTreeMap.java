package net.zdsoft.bigdata.data.echarts;

import com.google.common.collect.Lists;
import net.zdsoft.echarts.enu.BottomEnum;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.Trigger;

import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 18-8-27 下午3:32
 */
public class OptionExposeTreeMap {

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

    private Integer leafDepth;
    private String drillDownIcon;
    private String visibleMin;
    private String childrenVisibleMin;

    private Boolean showBreadCrumb;
    private String top;
    private String left;
    private Integer height;
    private Integer borderWidth;
    private String breadColor;
    private String borderColor;


    public static OptionExposeTreeMap getDefault() {
        OptionExposeTreeMap default_oe = new OptionExposeTreeMap();
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

        default_oe.setLeafDepth(1);
        default_oe.setDrillDownIcon("∨");
        default_oe.setVisibleMin("10");
        default_oe.setChildrenVisibleMin(null);
        default_oe.setShowBreadCrumb(true);
        default_oe.setTop(BottomEnum.bottom.name());
        default_oe.setLeft(LeftEnum.center.name());
        default_oe.setHeight(22);
        default_oe.setBorderWidth(1);
        default_oe.setBorderColor("#ffffff");
        default_oe.setBreadColor("#000");
        return default_oe;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
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


    public OptionExposeTitle getExposeTitle() {
        return exposeTitle;
    }

    public void setExposeTitle(OptionExposeTitle exposeTitle) {
        this.exposeTitle = exposeTitle;
    }

    public Integer getLeafDepth() {
        return leafDepth;
    }

    public void setLeafDepth(Integer leafDepth) {
        this.leafDepth = leafDepth;
    }

    public String getDrillDownIcon() {
        return drillDownIcon;
    }

    public void setDrillDownIcon(String drillDownIcon) {
        this.drillDownIcon = drillDownIcon;
    }

    public String getVisibleMin() {
        return visibleMin;
    }

    public void setVisibleMin(String visibleMin) {
        this.visibleMin = visibleMin;
    }

    public String getChildrenVisibleMin() {
        return childrenVisibleMin;
    }

    public void setChildrenVisibleMin(String childrenVisibleMin) {
        this.childrenVisibleMin = childrenVisibleMin;
    }

    public Boolean getShowBreadCrumb() {
        return showBreadCrumb;
    }

    public void setShowBreadCrumb(Boolean showBreadCrumb) {
        this.showBreadCrumb = showBreadCrumb;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
    }

    public String getBreadColor() {
        return breadColor;
    }

    public void setBreadColor(String breadColor) {
        this.breadColor = breadColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
}
