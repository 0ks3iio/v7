package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.bigdata.data.action.convert.echarts.component.Grid;

import java.util.List;

/**
 * @author shenke
 * @since 2018/7/26 上午11:29
 */
public class OptionExposeCartesian2dBase {

    private List<String> colors;
    private Boolean smooth;

    private Boolean exchangeXY;

    private String gridLeft = "10%";
    private String gridRight = "10%";
    private String gridTop = "15%";
    private String gridBottom = "13%";
    /*------------------------X轴设置开始----------------------*/
    private Boolean showX;
    /**
     * x轴标题
     */
    private String xTitle;
    /**
     * x轴标题字体
     */
    private Integer xTitleFontSize;
    /**
     * x轴标题字体是否加粗
     */
    private Boolean xTitleFontBold;
    /**
     * x轴标题字体是否倾斜
     */
    private Boolean xTitleFontItalic;
    /**
     * x轴标题字体颜色
     */
    private String xTitleFontColor;
    /**
     * x轴标题的位置
     * 可选
     * * 'start'
     * * 'middle' 或者 'center'
     * * 'end'
     */
    private String xTitleLocation;

    /**
     * 是否显示x轴线
     */
    private Boolean showXLine;

    /**
     * x轴线的颜色
     */
    private String xLineColor;

    /**
     * 是否显示x轴刻度
     */
    private Boolean showXTick;

    /**
     * x轴刻度的颜色
     */
    private String xTickColor;

    /**
     * x轴刻度间隔
     * 默认是 'auto'
     */
    private Integer xTickInterval;

    /**
     * 是否显示x轴标签
     */
    private Boolean showXLabel;
    private Boolean xLabelInside;
    private Integer xLabelRotate;
    private Integer xLabelMargin;
    private Integer xLabelInterval;
    private Integer xZ;

    /**
     * x轴标签的颜色
     */
    private String xLabelColor;
    /**
     * 是否显示x轴在坐标系内的分隔线
     */
    private Boolean showXSplitLine;

    private String xSplitLineColor;

    /**
     * 是否反向坐标轴
     */
    private Boolean xInverse;
    ///**
    // * x轴的最大值，可以不指定
    // */
    //private Object xMax;
    ///**
    // * x轴的最小值，可以不指定
    // */
    //private Object xMin;
    /**
     * x轴的位置
     * 可选：
     * 'top'
     * 'bottom'
     */
    private String xPosition;
    /*------------------------X轴设置结束----------------------*/

    /*------------------------y轴设置开始----------------------*/
    private Boolean showY;
    /**
     * y轴标题
     */
    private String yTitle;
    /**
     * y轴标题字体
     */
    private Integer yTitleFontSize;
    /**
     * y轴标题字体是否加粗
     */
    private Boolean yTitleFontBold;
    /**
     * y轴标题字体是否倾斜
     */
    private Boolean yTitleFontItalic;
    /**
     * y轴标题字体颜色
     * 可选
     * 'start'
     * 'middle' 或者 'center'
     * 'end'
     */
    private String yTitleFontColor;
    /**
     * y轴标题的位置
     */
    private String yTitleLocation;

    /**
     * 是否显示y轴线
     */
    private Boolean showYLine;
    private String yLineColor;
    /**
     * 是否显示y轴刻度
     */
    private Boolean showYTick;
    /**
     *
     */
    private String yTickColor;
    /**
     * y轴刻度间隔
     * 默认是 'auto'
     */
    private Integer yTickInterval;

    /**
     * 是否显示y轴标签
     */
    private Boolean showYLabel;

    private Boolean yLabelInside;
    private Integer yLabelRotate;
    private Integer yLabelMargin;
    private Integer yLabelInterval;
    private Integer yZ;

    /**
     * y轴标签的颜色
     */
    private String yLabelColor;

    /**
     * 是否显示y轴在坐标系内的分隔线
     */
    private Boolean showYSplitLine;

    private String ySplitLineColor;

    /**
     * 是否反向坐标轴
     */
    private Boolean yInverse;
    ///**
    // * y轴的最大值，可以不指定
    // */
    //private Object yMax;
    ///**
    // * y轴的最小值，可以不指定
    // */
    //private Object yMin;
    /**
     * y轴的位置
     * 可选：
     * 'left'
     * 'right'
     */
    private String yPosition;
    /*------------------------y轴设置结束----------------------*/

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
    private Integer legendBackgroundColorTransparent;
    private Integer legendBorderColorTransparent;
    private Integer legendBorderWidth;
    private Integer legendBorderRadius;
    private String legendBorderColor;
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
    private OptionExposeTitle exposeTitle;

    public Integer getxZ() {
        return xZ;
    }

    public void setxZ(Integer xZ) {
        this.xZ = xZ;
    }

    public Integer getyZ() {
        return yZ;
    }

    public void setyZ(Integer yZ) {
        this.yZ = yZ;
    }

    public OptionExposeTitle getExposeTitle() {
        return exposeTitle;
    }

    public void setExposeTitle(OptionExposeTitle exposeTitle) {
        this.exposeTitle = exposeTitle;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public Boolean getExchangeXY() {
        return exchangeXY;
    }

    public void setExchangeXY(Boolean exchangeXY) {
        this.exchangeXY = exchangeXY;
    }

    public Boolean getShowX() {
        return showX;
    }

    public void setShowX(Boolean showX) {
        this.showX = showX;
    }

    public String getxTitle() {
        return xTitle;
    }

    public void setxTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public Integer getxTitleFontSize() {
        return xTitleFontSize;
    }

    public void setxTitleFontSize(Integer xTitleFontSize) {
        this.xTitleFontSize = xTitleFontSize;
    }

    public Boolean getxTitleFontBold() {
        return xTitleFontBold;
    }

    public void setxTitleFontBold(Boolean xTitleFontBold) {
        this.xTitleFontBold = xTitleFontBold;
    }

    public Boolean getxTitleFontItalic() {
        return xTitleFontItalic;
    }

    public void setxTitleFontItalic(Boolean xTitleFontItalic) {
        this.xTitleFontItalic = xTitleFontItalic;
    }

    public String getxTitleFontColor() {
        return xTitleFontColor;
    }

    public void setxTitleFontColor(String xTitleFontColor) {
        this.xTitleFontColor = xTitleFontColor;
    }

    public String getxTitleLocation() {
        return xTitleLocation;
    }

    public void setxTitleLocation(String xTitleLocation) {
        this.xTitleLocation = xTitleLocation;
    }

    public Boolean getShowXLine() {
        return showXLine;
    }

    public void setShowXLine(Boolean showXLine) {
        this.showXLine = showXLine;
    }

    public String getxLineColor() {
        return xLineColor;
    }

    public void setxLineColor(String xLineColor) {
        this.xLineColor = xLineColor;
    }

    public Boolean getShowXTick() {
        return showXTick;
    }

    public void setShowXTick(Boolean showXTick) {
        this.showXTick = showXTick;
    }

    public String getxTickColor() {
        return xTickColor;
    }

    public void setxTickColor(String xTickColor) {
        this.xTickColor = xTickColor;
    }

    public Integer getxTickInterval() {
        return xTickInterval;
    }

    public void setxTickInterval(Integer xTickInterval) {
        this.xTickInterval = xTickInterval;
    }

    public Boolean getShowXLabel() {
        return showXLabel;
    }

    public void setShowXLabel(Boolean showXLabel) {
        this.showXLabel = showXLabel;
    }

    public String getxLabelColor() {
        return xLabelColor;
    }

    public void setxLabelColor(String xLabelColor) {
        this.xLabelColor = xLabelColor;
    }

    public Boolean getShowXSplitLine() {
        return showXSplitLine;
    }

    public void setShowXSplitLine(Boolean showXSplitLine) {
        this.showXSplitLine = showXSplitLine;
    }

    public Boolean getxInverse() {
        return xInverse;
    }

    public void setxInverse(Boolean xInverse) {
        this.xInverse = xInverse;
    }

    public String getxPosition() {
        return xPosition;
    }

    public void setxPosition(String xPosition) {
        this.xPosition = xPosition;
    }

    public Boolean getShowY() {
        return showY;
    }

    public void setShowY(Boolean showY) {
        this.showY = showY;
    }

    public String getyTitle() {
        return yTitle;
    }

    public void setyTitle(String yTitle) {
        this.yTitle = yTitle;
    }

    public Integer getyTitleFontSize() {
        return yTitleFontSize;
    }

    public void setyTitleFontSize(Integer yTitleFontSize) {
        this.yTitleFontSize = yTitleFontSize;
    }

    public Boolean getyTitleFontBold() {
        return yTitleFontBold;
    }

    public void setyTitleFontBold(Boolean yTitleFontBold) {
        this.yTitleFontBold = yTitleFontBold;
    }

    public Boolean getyTitleFontItalic() {
        return yTitleFontItalic;
    }

    public void setyTitleFontItalic(Boolean yTitleFontItalic) {
        this.yTitleFontItalic = yTitleFontItalic;
    }

    public String getyTitleFontColor() {
        return yTitleFontColor;
    }

    public void setyTitleFontColor(String yTitleFontColor) {
        this.yTitleFontColor = yTitleFontColor;
    }

    public String getyTitleLocation() {
        return yTitleLocation;
    }

    public void setyTitleLocation(String yTitleLocation) {
        this.yTitleLocation = yTitleLocation;
    }

    public Boolean getShowYLine() {
        return showYLine;
    }

    public void setShowYLine(Boolean showYLine) {
        this.showYLine = showYLine;
    }

    public String getyLineColor() {
        return yLineColor;
    }

    public void setyLineColor(String yLineColor) {
        this.yLineColor = yLineColor;
    }

    public Boolean getShowYTick() {
        return showYTick;
    }

    public void setShowYTick(Boolean showYTick) {
        this.showYTick = showYTick;
    }

    public String getyTickColor() {
        return yTickColor;
    }

    public void setyTickColor(String yTickColor) {
        this.yTickColor = yTickColor;
    }

    public Integer getyTickInterval() {
        return yTickInterval;
    }

    public void setyTickInterval(Integer yTickInterval) {
        this.yTickInterval = yTickInterval;
    }

    public Boolean getShowYLabel() {
        return showYLabel;
    }

    public void setShowYLabel(Boolean showYLabel) {
        this.showYLabel = showYLabel;
    }

    public String getyLabelColor() {
        return yLabelColor;
    }

    public void setyLabelColor(String yLabelColor) {
        this.yLabelColor = yLabelColor;
    }

    public Boolean getShowYSplitLine() {
        return showYSplitLine;
    }

    public void setShowYSplitLine(Boolean showYSplitLine) {
        this.showYSplitLine = showYSplitLine;
    }

    public Boolean getyInverse() {
        return yInverse;
    }

    public void setyInverse(Boolean yInverse) {
        this.yInverse = yInverse;
    }

    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
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

    public String getxSplitLineColor() {
        return xSplitLineColor;
    }

    public void setxSplitLineColor(String xSplitLineColor) {
        this.xSplitLineColor = xSplitLineColor;
    }

    public String getySplitLineColor() {
        return ySplitLineColor;
    }

    public void setySplitLineColor(String ySplitLineColor) {
        this.ySplitLineColor = ySplitLineColor;
    }

    public Boolean getSmooth() {
        return smooth;
    }

    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
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

    public Boolean getxLabelInside() {
        return xLabelInside;
    }

    public void setxLabelInside(Boolean xLabelInside) {
        this.xLabelInside = xLabelInside;
    }

    public Integer getxLabelRotate() {
        return xLabelRotate;
    }

    public void setxLabelRotate(Integer xLabelRotate) {
        this.xLabelRotate = xLabelRotate;
    }

    public Integer getxLabelMargin() {
        return xLabelMargin;
    }

    public void setxLabelMargin(Integer xLabelMargin) {
        this.xLabelMargin = xLabelMargin;
    }

    public Integer getxLabelInterval() {
        return xLabelInterval;
    }

    public void setxLabelInterval(Integer xLabelInterval) {
        this.xLabelInterval = xLabelInterval;
    }

    public Boolean getyLabelInside() {
        return yLabelInside;
    }

    public void setyLabelInside(Boolean yLabelInside) {
        this.yLabelInside = yLabelInside;
    }

    public Integer getyLabelRotate() {
        return yLabelRotate;
    }

    public void setyLabelRotate(Integer yLabelRotate) {
        this.yLabelRotate = yLabelRotate;
    }

    public Integer getyLabelMargin() {
        return yLabelMargin;
    }

    public void setyLabelMargin(Integer yLabelMargin) {
        this.yLabelMargin = yLabelMargin;
    }

    public Integer getyLabelInterval() {
        return yLabelInterval;
    }

    public void setyLabelInterval(Integer yLabelInterval) {
        this.yLabelInterval = yLabelInterval;
    }

    public String getGridLeft() {
        return gridLeft;
    }

    public void setGridLeft(String gridLeft) {
        this.gridLeft = gridLeft;
    }

    public String getGridRight() {
        return gridRight;
    }

    public void setGridRight(String gridRight) {
        this.gridRight = gridRight;
    }

    public String getGridTop() {
        return gridTop;
    }

    public void setGridTop(String gridTop) {
        this.gridTop = gridTop;
    }

    public String getGridBottom() {
        return gridBottom;
    }

    public void setGridBottom(String gridBottom) {
        this.gridBottom = gridBottom;
    }
}
