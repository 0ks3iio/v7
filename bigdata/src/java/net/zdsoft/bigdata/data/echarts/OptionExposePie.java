package net.zdsoft.bigdata.data.echarts;

import com.google.common.collect.Lists;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.LineType;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.Trigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shenke
 * @since 2018/7/26 下午1:53
 */
public class OptionExposePie {


    public static OptionExposePie getDefaultOptionExpose() {
        OptionExposePie default_oe = new OptionExposePie();
        default_oe.setColors(Lists.newArrayList("#1f83f5", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"));
        Collections.shuffle(default_oe.getColors());

        //default_oe.setPieSelectedMode(Boolean.FALSE.toString());
        //default_oe.setPieSelectedOffset(10);
        //default_oe.setPieClockWise(true);
        //default_oe.setPieRoseType(Boolean.FALSE.toString());
        //default_oe.setPieCenter("[50%,50%]");
        //default_oe.setPieRadius("[0,75%]");
        //default_oe.setShowPieLabelLine(true);
        ////default_oe.setPieLabelLineColor("#000");
        //default_oe.setPieLabelLineSmooth(0);
        //default_oe.setPieLabelLineWidth(1);
        //default_oe.setPieLabelLineType(LineType.solid.toString());


        //图例
        default_oe.setShowLegend(true);
        default_oe.setLegendOrient(Orient.horizontal.name());
        default_oe.setLegendLeft(LeftEnum.center.name());
        default_oe.setLegendTop(TopEnum.top.name());
        default_oe.setLegendTextFontColor("#ccc");
        default_oe.setLegendTextFontSize(12);
        default_oe.setLegendTextFontBold(false);
        default_oe.setLegendTextFontItalic(false);
        default_oe.setLegendBackgroundColorTransparent(0);

        //提示框
        default_oe.setShowTooltip(true);
        default_oe.setTooltipTrigger(Trigger.item.name());
        default_oe.setTooltipBackgroundColor("#323232");
        default_oe.setTooltipBackgroundColorTransparent(7);
        default_oe.setTooltipBorderWidth(0);
        default_oe.setTooltipBorderColor("#ccc");
        default_oe.setTooltipConfine(true);

        //数据标签
        default_oe.setShowSLabel(false);
        default_oe.setsLabelTextFontSize(12);
        default_oe.setsLabelTextFontBold(false);
        default_oe.setsLabelTextFontItalic(false);
        default_oe.setsLabelTextFontColor("#ccc");
        default_oe.setsLabelBackgroundColorTransparent(0);
        default_oe.setsLabelBorderColorTransparent(0);
        default_oe.setsLabelPosition(PositionEnum.inside.name());
        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
        return default_oe;
    }

    private List<String> colors;
    private OptionExposeTitle exposeTitle;



    /*------------------------图例配置开始----------------------*/
    /**
     * 是否显示图例
     */
    private Boolean showLegend;
    /**
     * 图例的布局方式
     */
    private String legendOrient;
    /**
     * 图例水平左偏移量
     * 可以是 'left' 'center' 'right' '20%' '10'
     */
    private String legendLeft;


    private String legendTop;

    private String legendTextFontColor;
    private Boolean legendTextFontBold;
    private Boolean legendTextFontItalic;
    private Integer legendTextFontSize;


    private String legendBackgroundColor;
    private Integer legendBorderWidth;
    private Integer legendBorderRadius;
    private String legendBorderColor;
    private Integer legendBackgroundColorTransparent;
    private Integer legendBorderColorTransparent;
    /*------------------------图例配置结束----------------------*/

    /*------------------------Tooltip----------------------*/
    private Boolean showTooltip;
    private String tooltipTrigger;
    private String tooltipBackgroundColor;
    private Integer tooltipBackgroundColorTransparent; //透明度，最大值是10 min 1 最终转换为小数
    private Integer tooltipBorderWidth;
    private String tooltipBorderColor;
    private Boolean tooltipConfine;
    private String tooltipFormatter;

    private Integer tooltipTextFontSize;
    private Boolean tooltipTextFontBold;
    private Boolean tooltipTextFontItalic;
    private String tooltipTextFontColor;
    /*------------------------Tooltip----------------------*/

    /*------------------------数据标签（显示在柱状图顶部那种）----------------------*/
    private Boolean showSLabel;

    private Integer sLabelTextFontSize;
    private Boolean sLabelTextFontBold;
    private Boolean sLabelTextFontItalic;
    private String sLabelTextFontColor;

    private String sLabelBackgroundColor;
    private Integer sLabelBackgroundColorTransparent;
    private Integer sLabelBorderWidth;
    private String sLabelBorderColor;
    private Integer sLabelBorderColorTransparent;

    private String sLabelPosition;
    /*------------------------数据标签（显示在柱状图顶部那种）----------------------*/
    private List<OptionExposeLabel> exposeLabels;
    private List<OptionExposeCR> exposeCrs;

    public OptionExposeTitle getExposeTitle() {
        return exposeTitle;
    }

    public void setExposeTitle(OptionExposeTitle exposeTitle) {
        this.exposeTitle = exposeTitle;
    }

    public Integer getLegendBackgroundColorTransparent() {
        return legendBackgroundColorTransparent;
    }

    public void setLegendBackgroundColorTransparent(Integer legendBackgroundColorTransparent) {
        this.legendBackgroundColorTransparent = legendBackgroundColorTransparent;
    }

    public Integer getLegendBorderColorTransparent() {
        return legendBorderColorTransparent;
    }

    public void setLegendBorderColorTransparent(Integer legendBorderColorTransparent) {
        this.legendBorderColorTransparent = legendBorderColorTransparent;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Boolean getShowLegend() {
        return showLegend;
    }

    public void setShowLegend(Boolean showLegend) {
        this.showLegend = showLegend;
    }

    public String getLegendOrient() {
        return legendOrient;
    }

    public void setLegendOrient(String legendOrient) {
        this.legendOrient = legendOrient;
    }

    public String getLegendLeft() {
        return legendLeft;
    }

    public void setLegendLeft(String legendLeft) {
        this.legendLeft = legendLeft;
    }

    public String getLegendTop() {
        return legendTop;
    }

    public void setLegendTop(String legendTop) {
        this.legendTop = legendTop;
    }

    public String getLegendTextFontColor() {
        return legendTextFontColor;
    }

    public void setLegendTextFontColor(String legendTextFontColor) {
        this.legendTextFontColor = legendTextFontColor;
    }

    public Boolean getLegendTextFontBold() {
        return legendTextFontBold;
    }

    public void setLegendTextFontBold(Boolean legendTextFontBold) {
        this.legendTextFontBold = legendTextFontBold;
    }

    public Boolean getLegendTextFontItalic() {
        return legendTextFontItalic;
    }

    public void setLegendTextFontItalic(Boolean legendTextFontItalic) {
        this.legendTextFontItalic = legendTextFontItalic;
    }

    public Integer getLegendTextFontSize() {
        return legendTextFontSize;
    }

    public void setLegendTextFontSize(Integer legendTextFontSize) {
        this.legendTextFontSize = legendTextFontSize;
    }

    public String getLegendBackgroundColor() {
        return legendBackgroundColor;
    }

    public void setLegendBackgroundColor(String legendBackgroundColor) {
        this.legendBackgroundColor = legendBackgroundColor;
    }

    public Integer getLegendBorderWidth() {
        return legendBorderWidth;
    }

    public void setLegendBorderWidth(Integer legendBorderWidth) {
        this.legendBorderWidth = legendBorderWidth;
    }

    public Integer getLegendBorderRadius() {
        return legendBorderRadius;
    }

    public void setLegendBorderRadius(Integer legendBorderRadius) {
        this.legendBorderRadius = legendBorderRadius;
    }

    public String getLegendBorderColor() {
        return legendBorderColor;
    }

    public void setLegendBorderColor(String legendBorderColor) {
        this.legendBorderColor = legendBorderColor;
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

    public Integer getTooltipTextFontSize() {
        return tooltipTextFontSize;
    }

    public void setTooltipTextFontSize(Integer tooltipTextFontSize) {
        this.tooltipTextFontSize = tooltipTextFontSize;
    }

    public Boolean getTooltipTextFontBold() {
        return tooltipTextFontBold;
    }

    public void setTooltipTextFontBold(Boolean tooltipTextFontBold) {
        this.tooltipTextFontBold = tooltipTextFontBold;
    }

    public Boolean getTooltipTextFontItalic() {
        return tooltipTextFontItalic;
    }

    public void setTooltipTextFontItalic(Boolean tooltipTextFontItalic) {
        this.tooltipTextFontItalic = tooltipTextFontItalic;
    }

    public String getTooltipTextFontColor() {
        return tooltipTextFontColor;
    }

    public void setTooltipTextFontColor(String tooltipTextFontColor) {
        this.tooltipTextFontColor = tooltipTextFontColor;
    }

    public List<OptionExposeLabel> getExposeLabels() {
        return exposeLabels;
    }

    public void setExposeLabels(List<OptionExposeLabel> exposeLabels) {
        this.exposeLabels = exposeLabels;
    }

    public Boolean getShowSLabel() {
        return showSLabel;
    }

    public void setShowSLabel(Boolean showSLabel) {
        this.showSLabel = showSLabel;
    }

    public Integer getsLabelTextFontSize() {
        return sLabelTextFontSize;
    }

    public void setsLabelTextFontSize(Integer sLabelTextFontSize) {
        this.sLabelTextFontSize = sLabelTextFontSize;
    }

    public Boolean getsLabelTextFontBold() {
        return sLabelTextFontBold;
    }

    public void setsLabelTextFontBold(Boolean sLabelTextFontBold) {
        this.sLabelTextFontBold = sLabelTextFontBold;
    }

    public Boolean getsLabelTextFontItalic() {
        return sLabelTextFontItalic;
    }

    public void setsLabelTextFontItalic(Boolean sLabelTextFontItalic) {
        this.sLabelTextFontItalic = sLabelTextFontItalic;
    }

    public String getsLabelTextFontColor() {
        return sLabelTextFontColor;
    }

    public void setsLabelTextFontColor(String sLabelTextFontColor) {
        this.sLabelTextFontColor = sLabelTextFontColor;
    }

    public String getsLabelBackgroundColor() {
        return sLabelBackgroundColor;
    }

    public void setsLabelBackgroundColor(String sLabelBackgroundColor) {
        this.sLabelBackgroundColor = sLabelBackgroundColor;
    }

    public Integer getsLabelBackgroundColorTransparent() {
        return sLabelBackgroundColorTransparent;
    }

    public void setsLabelBackgroundColorTransparent(Integer sLabelBackgroundColorTransparent) {
        this.sLabelBackgroundColorTransparent = sLabelBackgroundColorTransparent;
    }

    public Integer getsLabelBorderWidth() {
        return sLabelBorderWidth;
    }

    public void setsLabelBorderWidth(Integer sLabelBorderWidth) {
        this.sLabelBorderWidth = sLabelBorderWidth;
    }

    public String getsLabelBorderColor() {
        return sLabelBorderColor;
    }

    public void setsLabelBorderColor(String sLabelBorderColor) {
        this.sLabelBorderColor = sLabelBorderColor;
    }

    public Integer getsLabelBorderColorTransparent() {
        return sLabelBorderColorTransparent;
    }

    public void setsLabelBorderColorTransparent(Integer sLabelBorderColorTransparent) {
        this.sLabelBorderColorTransparent = sLabelBorderColorTransparent;
    }

    public String getsLabelPosition() {
        return sLabelPosition;
    }

    public void setsLabelPosition(String sLabelPosition) {
        this.sLabelPosition = sLabelPosition;
    }

    public List<OptionExposeCR> getExposeCrs() {
        return exposeCrs;
    }

    public void setExposeCrs(List<OptionExposeCR> exposeCrs) {
        this.exposeCrs = exposeCrs;
    }
}
