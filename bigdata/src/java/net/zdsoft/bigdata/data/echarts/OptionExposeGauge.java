package net.zdsoft.bigdata.data.echarts;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenke
 * @since 2018/7/30 下午2:23
 */
public class OptionExposeGauge {
    private List<String> colors;

    private List<Cr> crs;
    private List<GaugeStyle> gaugeStyles;
    private OptionExposeTitle exposeTitle;

    public static OptionExposeGauge getDefaultExpose() {
        OptionExposeGauge default_oe = new OptionExposeGauge();
        default_oe.setColors(Arrays.asList("#1f83f5","#d042a4","#1ebcd3","#9949d7","#ee913a","#3bb7f0","#cdb112","#b9396d"));
        default_oe.setExposeTitle(OptionExposeTitle.getDefault());
        return default_oe;
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

    public List<Cr> getCrs() {
        return crs;
    }

    public void setCrs(List<Cr> crs) {
        this.crs = crs;
    }

    public List<GaugeStyle> getGaugeStyles() {
        return gaugeStyles;
    }

    public void setGaugeStyles(List<GaugeStyle> gaugeStyles) {
        this.gaugeStyles = gaugeStyles;
    }

    public static class GaugeStyle {

        public static GaugeStyle create() {
            GaugeStyle style = new GaugeStyle();
            style.setShowGaugeAxisLine(true);
            style.setGaugeAxisLineWidth(30);

            style.setShowGaugeSplitLine(true);
            style.setGaugeSplitLineLength(10);
            style.setGaugeSplitLineWidth(2);

            style.setShowGaugeAxisTick(true);
            style.setGaugeAxisTickSplitNumber(5);
            style.setGaugeAxisTickLength(8);
            style.setGaugeAxisTickWidth(1);

            style.setShowGaugeAxisLabel(true);
            style.setGaugeAxisLabelDistance(25);
            style.setGaugeAxisLabelFontBold(false);
            style.setGaugeAxisLabelFontItalic(false);
            style.setGaugeAxisLabelFontSize(12);

            style.setShowGaugePointer(true);
            style.setGaugePointerLength("80%");
            style.setGaugePointerWidth(8);

            style.setShowGaugeTitle(true);
            style.setGaugeTitleOffsetCenter("[0, -40%]");
            style.setGaugeTitleFontBold(false);
            style.setGaugeTitleFontItalic(false);
            style.setGaugeTitleFontSize(15);
            style.setGaugeTitleFontColor("#ccc");

            style.setShowGaugeDetail(true);
            style.setGaugeDetailFontBold(false);
            style.setGaugeDetailFontItalic(false);
            style.setGaugeDetailFontSize(15);
            style.setGaugeDetailOffsetCenter("[0, 40%]");
            style.setGaugeDetailFormatter("{value}%");
            return style;
        }

        private String name;
        private Boolean showGaugeAxisLine;
        //private String gaugeAxisLineColor;
        private Integer gaugeAxisLineWidth;

        private Boolean showGaugeSplitLine;
        private Integer gaugeSplitLineLength;
        //private String gaugeSplitLineColor;
        private Integer gaugeSplitLineWidth;
        //private String gaugeSplitLineType;

        private Boolean showGaugeAxisTick;
        private Integer gaugeAxisTickSplitNumber;
        private Integer gaugeAxisTickLength;
        //private String gaugeAxisTickColor;
        private Integer gaugeAxisTickWidth;
        //private String gaugeAxisTickType;

        private Boolean showGaugeAxisLabel;
        private Integer gaugeAxisLabelDistance;
        private Boolean gaugeAxisLabelFontItalic;
        private Boolean gaugeAxisLabelFontBold;
        private Integer gaugeAxisLabelFontSize;

        private Boolean showGaugePointer;
        private String gaugePointerLength;
        private Integer gaugePointerWidth;

        private Boolean showGaugeTitle;
        private String gaugeTitleOffsetCenter;
        private Boolean gaugeTitleFontBold;
        private Boolean gaugeTitleFontItalic;
        private Integer gaugeTitleFontSize;
        private String gaugeTitleFontColor;

        private Boolean showGaugeDetail;
        private String gaugeDetailOffsetCenter;
        private String gaugeDetailFormatter;
        private String gaugeDetailFontColor;
        private Boolean gaugeDetailFontBold;
        private Boolean gaugeDetailFontItalic;
        private Integer gaugeDetailFontSize;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getShowGaugeAxisLine() {
            return showGaugeAxisLine;
        }

        public void setShowGaugeAxisLine(Boolean showGaugeAxisLine) {
            this.showGaugeAxisLine = showGaugeAxisLine;
        }


        public Integer getGaugeAxisLineWidth() {
            return gaugeAxisLineWidth;
        }

        public void setGaugeAxisLineWidth(Integer gaugeAxisLineWidth) {
            this.gaugeAxisLineWidth = gaugeAxisLineWidth;
        }

        public Boolean getShowGaugeSplitLine() {
            return showGaugeSplitLine;
        }

        public void setShowGaugeSplitLine(Boolean showGaugeSplitLine) {
            this.showGaugeSplitLine = showGaugeSplitLine;
        }

        public Integer getGaugeSplitLineLength() {
            return gaugeSplitLineLength;
        }

        public void setGaugeSplitLineLength(Integer gaugeSplitLineLength) {
            this.gaugeSplitLineLength = gaugeSplitLineLength;
        }


        public Integer getGaugeSplitLineWidth() {
            return gaugeSplitLineWidth;
        }

        public void setGaugeSplitLineWidth(Integer gaugeSplitLineWidth) {
            this.gaugeSplitLineWidth = gaugeSplitLineWidth;
        }


        public Boolean getShowGaugeAxisTick() {
            return showGaugeAxisTick;
        }

        public void setShowGaugeAxisTick(Boolean showGaugeAxisTick) {
            this.showGaugeAxisTick = showGaugeAxisTick;
        }

        public Integer getGaugeAxisTickSplitNumber() {
            return gaugeAxisTickSplitNumber;
        }

        public void setGaugeAxisTickSplitNumber(Integer gaugeAxisTickSplitNumber) {
            this.gaugeAxisTickSplitNumber = gaugeAxisTickSplitNumber;
        }

        public Integer getGaugeAxisTickLength() {
            return gaugeAxisTickLength;
        }

        public void setGaugeAxisTickLength(Integer gaugeAxisTickLength) {
            this.gaugeAxisTickLength = gaugeAxisTickLength;
        }


        public Integer getGaugeAxisTickWidth() {
            return gaugeAxisTickWidth;
        }

        public void setGaugeAxisTickWidth(Integer gaugeAxisTickWidth) {
            this.gaugeAxisTickWidth = gaugeAxisTickWidth;
        }


        public Boolean getShowGaugeAxisLabel() {
            return showGaugeAxisLabel;
        }

        public void setShowGaugeAxisLabel(Boolean showGaugeAxisLabel) {
            this.showGaugeAxisLabel = showGaugeAxisLabel;
        }

        public Integer getGaugeAxisLabelDistance() {
            return gaugeAxisLabelDistance;
        }

        public void setGaugeAxisLabelDistance(Integer gaugeAxisLabelDistance) {
            this.gaugeAxisLabelDistance = gaugeAxisLabelDistance;
        }

        public Boolean getGaugeAxisLabelFontItalic() {
            return gaugeAxisLabelFontItalic;
        }

        public void setGaugeAxisLabelFontItalic(Boolean gaugeAxisLabelFontItalic) {
            this.gaugeAxisLabelFontItalic = gaugeAxisLabelFontItalic;
        }

        public Boolean getGaugeAxisLabelFontBold() {
            return gaugeAxisLabelFontBold;
        }

        public void setGaugeAxisLabelFontBold(Boolean gaugeAxisLabelFontBold) {
            this.gaugeAxisLabelFontBold = gaugeAxisLabelFontBold;
        }

        public Integer getGaugeAxisLabelFontSize() {
            return gaugeAxisLabelFontSize;
        }

        public void setGaugeAxisLabelFontSize(Integer gaugeAxisLabelFontSize) {
            this.gaugeAxisLabelFontSize = gaugeAxisLabelFontSize;
        }

        public Boolean getShowGaugePointer() {
            return showGaugePointer;
        }

        public void setShowGaugePointer(Boolean showGaugePointer) {
            this.showGaugePointer = showGaugePointer;
        }

        public String getGaugePointerLength() {
            return gaugePointerLength;
        }

        public void setGaugePointerLength(String gaugePointerLength) {
            this.gaugePointerLength = gaugePointerLength;
        }

        public Integer getGaugePointerWidth() {
            return gaugePointerWidth;
        }

        public void setGaugePointerWidth(Integer gaugePointerWidth) {
            this.gaugePointerWidth = gaugePointerWidth;
        }

        public Boolean getShowGaugeTitle() {
            return showGaugeTitle;
        }

        public void setShowGaugeTitle(Boolean showGaugeTitle) {
            this.showGaugeTitle = showGaugeTitle;
        }

        public String getGaugeTitleOffsetCenter() {
            return gaugeTitleOffsetCenter;
        }

        public void setGaugeTitleOffsetCenter(String gaugeTitleOffsetCenter) {
            this.gaugeTitleOffsetCenter = gaugeTitleOffsetCenter;
        }

        public Boolean getGaugeTitleFontBold() {
            return gaugeTitleFontBold;
        }

        public void setGaugeTitleFontBold(Boolean gaugeTitleFontBold) {
            this.gaugeTitleFontBold = gaugeTitleFontBold;
        }

        public Boolean getGaugeTitleFontItalic() {
            return gaugeTitleFontItalic;
        }

        public void setGaugeTitleFontItalic(Boolean gaugeTitleFontItalic) {
            this.gaugeTitleFontItalic = gaugeTitleFontItalic;
        }

        public Integer getGaugeTitleFontSize() {
            return gaugeTitleFontSize;
        }

        public void setGaugeTitleFontSize(Integer gaugeTitleFontSize) {
            this.gaugeTitleFontSize = gaugeTitleFontSize;
        }


        public Boolean getShowGaugeDetail() {
            return showGaugeDetail;
        }

        public void setShowGaugeDetail(Boolean showGaugeDetail) {
            this.showGaugeDetail = showGaugeDetail;
        }

        public String getGaugeDetailOffsetCenter() {
            return gaugeDetailOffsetCenter;
        }

        public void setGaugeDetailOffsetCenter(String gaugeDetailOffsetCenter) {
            this.gaugeDetailOffsetCenter = gaugeDetailOffsetCenter;
        }

        public String getGaugeDetailFormatter() {
            return gaugeDetailFormatter;
        }

        public void setGaugeDetailFormatter(String gaugeDetailFormatter) {
            this.gaugeDetailFormatter = gaugeDetailFormatter;
        }


        public Boolean getGaugeDetailFontBold() {
            return gaugeDetailFontBold;
        }

        public void setGaugeDetailFontBold(Boolean gaugeDetailFontBold) {
            this.gaugeDetailFontBold = gaugeDetailFontBold;
        }

        public Boolean getGaugeDetailFontItalic() {
            return gaugeDetailFontItalic;
        }

        public void setGaugeDetailFontItalic(Boolean gaugeDetailFontItalic) {
            this.gaugeDetailFontItalic = gaugeDetailFontItalic;
        }

        public Integer getGaugeDetailFontSize() {
            return gaugeDetailFontSize;
        }

        public void setGaugeDetailFontSize(Integer gaugeDetailFontSize) {
            this.gaugeDetailFontSize = gaugeDetailFontSize;
        }

        public String getGaugeTitleFontColor() {
            return gaugeTitleFontColor;
        }

        public void setGaugeTitleFontColor(String gaugeTitleFontColor) {
            this.gaugeTitleFontColor = gaugeTitleFontColor;
        }

        public String getGaugeDetailFontColor() {
            return gaugeDetailFontColor;
        }

        public void setGaugeDetailFontColor(String gaugeDetailFontColor) {
            this.gaugeDetailFontColor = gaugeDetailFontColor;
        }
    }

    public static class Cr {
        private String name;
        private Integer gaugeStartAngle;
        private Integer gaugeEndAngle;
        private Integer gaugeSplitNumber;
        private Object gaugeMax;
        private Object gaugeMin;
        private String gaugeCenter;
        private String gaugeRadius;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getGaugeStartAngle() {
            return gaugeStartAngle;
        }

        public void setGaugeStartAngle(Integer gaugeStartAngle) {
            this.gaugeStartAngle = gaugeStartAngle;
        }

        public Integer getGaugeEndAngle() {
            return gaugeEndAngle;
        }

        public void setGaugeEndAngle(Integer gaugeEndAngle) {
            this.gaugeEndAngle = gaugeEndAngle;
        }

        public Integer getGaugeSplitNumber() {
            return gaugeSplitNumber;
        }

        public void setGaugeSplitNumber(Integer gaugeSplitNumber) {
            this.gaugeSplitNumber = gaugeSplitNumber;
        }

        public Object getGaugeMax() {
            return gaugeMax;
        }

        public void setGaugeMax(Object gaugeMax) {
            this.gaugeMax = gaugeMax;
        }

        public Object getGaugeMin() {
            return gaugeMin;
        }

        public void setGaugeMin(Object gaugeMin) {
            this.gaugeMin = gaugeMin;
        }

        public String getGaugeCenter() {
            return gaugeCenter;
        }

        public void setGaugeCenter(String gaugeCenter) {
            this.gaugeCenter = gaugeCenter;
        }

        public String getGaugeRadius() {
            return gaugeRadius;
        }

        public void setGaugeRadius(String gaugeRadius) {
            this.gaugeRadius = gaugeRadius;
        }
    }
}
