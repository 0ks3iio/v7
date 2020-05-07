package net.zdsoft.bigdata.data.echarts;

import com.google.common.collect.Lists;
import net.zdsoft.echarts.enu.Layout;
import net.zdsoft.echarts.enu.SymbolEnum;
import net.zdsoft.echarts.enu.Trigger;

import java.util.List;

/**
 * @author shenke
 * @since 18-8-28 上午9:40
 */
public class OptionExposeGraph {
    private List<String> colors;
    private Boolean showTooltip;
    private String tooltipTrigger;
    private String tooltipBackgroundColor;
    /**
     * 透明度，最大值是10 min 1 最终转换为小数
     */
    private Integer tooltipBackgroundColorTransparent;
    private Integer tooltipBorderWidth;
    private String tooltipBorderColor;
    private Boolean tooltipConfine;
    private String tooltipFormatter;

    private OptionExposeTitle exposeTitle;

    private Boolean showSLabel;

    private Integer sLabelTextFontSize;
    private Boolean sLabelTextFontBold;
    private Boolean sLabelTextFontItalic;
    private String sLabelTextFontColor;

    private String graphLayout;
    private String graphSymbol;
    private Integer graphSymbolSize;

    private List<LinkCategory> linkCategories;
    private List<DataCategory> dataCategories;
    private Force force;

    public static OptionExposeGraph createDefault() {
        OptionExposeGraph default_oe = new OptionExposeGraph();
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
        default_oe.setExposeTitle(OptionExposeTitle.getDefault());

        default_oe.setShowSLabel(true);
        default_oe.setsLabelTextFontColor("#ffffff");
        default_oe.setsLabelTextFontBold(false);
        default_oe.setsLabelTextFontItalic(false);
        default_oe.setsLabelTextFontSize(12);

        default_oe.setGraphLayout(Layout.force.name());
        default_oe.setGraphSymbol(SymbolEnum.circle.name());
        default_oe.setGraphSymbolSize(25);

        default_oe.setForce(Force.create());
        return default_oe;
    }

    public static class LinkCategory {
        private String linkCategoryName;
        private String linkCategoryLineColor;
        private Integer linkCategoryLineWidth;
        private String linkCategoryLineType;
        private Boolean linkCategoryShowLabel;
        private String linkCategoryLabelFontColor;
        private Boolean linkCategoryLabelFontItalic;
        private Boolean linkCategoryLabelFontBold;
        private Integer linkCategoryLabelFontSize;

        public String getLinkCategoryName() {
            return linkCategoryName;
        }

        public void setLinkCategoryName(String linkCategoryName) {
            this.linkCategoryName = linkCategoryName;
        }

        public String getLinkCategoryLineColor() {
            return linkCategoryLineColor;
        }

        public void setLinkCategoryLineColor(String linkCategoryLineColor) {
            this.linkCategoryLineColor = linkCategoryLineColor;
        }

        public Integer getLinkCategoryLineWidth() {
            return linkCategoryLineWidth;
        }

        public void setLinkCategoryLineWidth(Integer linkCategoryLineWidth) {
            this.linkCategoryLineWidth = linkCategoryLineWidth;
        }

        public String getLinkCategoryLineType() {
            return linkCategoryLineType;
        }

        public void setLinkCategoryLineType(String linkCategoryLineType) {
            this.linkCategoryLineType = linkCategoryLineType;
        }

        public Boolean getLinkCategoryShowLabel() {
            return linkCategoryShowLabel;
        }

        public void setLinkCategoryShowLabel(Boolean linkCategoryShowLabel) {
            this.linkCategoryShowLabel = linkCategoryShowLabel;
        }

        public String getLinkCategoryLabelFontColor() {
            return linkCategoryLabelFontColor;
        }

        public void setLinkCategoryLabelFontColor(String linkCategoryLabelFontColor) {
            this.linkCategoryLabelFontColor = linkCategoryLabelFontColor;
        }

        public Boolean getLinkCategoryLabelFontItalic() {
            return linkCategoryLabelFontItalic;
        }

        public void setLinkCategoryLabelFontItalic(Boolean linkCategoryLabelFontItalic) {
            this.linkCategoryLabelFontItalic = linkCategoryLabelFontItalic;
        }

        public Boolean getLinkCategoryLabelFontBold() {
            return linkCategoryLabelFontBold;
        }

        public void setLinkCategoryLabelFontBold(Boolean linkCategoryLabelFontBold) {
            this.linkCategoryLabelFontBold = linkCategoryLabelFontBold;
        }

        public Integer getLinkCategoryLabelFontSize() {
            return linkCategoryLabelFontSize;
        }

        public void setLinkCategoryLabelFontSize(Integer linkCategoryLabelFontSize) {
            this.linkCategoryLabelFontSize = linkCategoryLabelFontSize;
        }
    }

    public static class DataCategory {
        private String dataCategoryName;
        private String dataCategorySymbol;
        private Integer dataCategorySymbolSize;
        private String dataCategoryColor;
        private Boolean dataCategoryShowLabel;
        private String dataCategoryLabelFontColor;
        private Boolean dataCategoryLabelFontItalic;
        private Boolean dataCategoryLabelFontBold;
        private Integer dataCategoryLabelFontSize;

        public String getDataCategoryName() {
            return dataCategoryName;
        }

        public void setDataCategoryName(String dataCategoryName) {
            this.dataCategoryName = dataCategoryName;
        }

        public String getDataCategorySymbol() {
            return dataCategorySymbol;
        }

        public void setDataCategorySymbol(String dataCategorySymbol) {
            this.dataCategorySymbol = dataCategorySymbol;
        }

        public Integer getDataCategorySymbolSize() {
            return dataCategorySymbolSize;
        }

        public void setDataCategorySymbolSize(Integer dataCategorySymbolSize) {
            this.dataCategorySymbolSize = dataCategorySymbolSize;
        }

        public String getDataCategoryColor() {
            return dataCategoryColor;
        }

        public void setDataCategoryColor(String dataCategoryColor) {
            this.dataCategoryColor = dataCategoryColor;
        }

        public Boolean getDataCategoryShowLabel() {
            return dataCategoryShowLabel;
        }

        public void setDataCategoryShowLabel(Boolean dataCategoryShowLabel) {
            this.dataCategoryShowLabel = dataCategoryShowLabel;
        }

        public String getDataCategoryLabelFontColor() {
            return dataCategoryLabelFontColor;
        }

        public void setDataCategoryLabelFontColor(String dataCategoryLabelFontColor) {
            this.dataCategoryLabelFontColor = dataCategoryLabelFontColor;
        }

        public Boolean getDataCategoryLabelFontItalic() {
            return dataCategoryLabelFontItalic;
        }

        public void setDataCategoryLabelFontItalic(Boolean dataCategoryLabelFontItalic) {
            this.dataCategoryLabelFontItalic = dataCategoryLabelFontItalic;
        }

        public Boolean getDataCategoryLabelFontBold() {
            return dataCategoryLabelFontBold;
        }

        public void setDataCategoryLabelFontBold(Boolean dataCategoryLabelFontBold) {
            this.dataCategoryLabelFontBold = dataCategoryLabelFontBold;
        }

        public Integer getDataCategoryLabelFontSize() {
            return dataCategoryLabelFontSize;
        }

        public void setDataCategoryLabelFontSize(Integer dataCategoryLabelFontSize) {
            this.dataCategoryLabelFontSize = dataCategoryLabelFontSize;
        }
    }

    public static class Force {

        static Force create() {
            Force force = new Force();
            force.setEdgeLength("100");
            force.setRepulsion("200");
            return force;
        }

        private String edgeLength;
        private String repulsion;

        public String getEdgeLength() {
            return edgeLength;
        }

        public void setEdgeLength(String edgeLength) {
            this.edgeLength = edgeLength;
        }

        public String getRepulsion() {
            return repulsion;
        }

        public void setRepulsion(String repulsion) {
            this.repulsion = repulsion;
        }
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

    public String getGraphLayout() {
        return graphLayout;
    }

    public void setGraphLayout(String graphLayout) {
        this.graphLayout = graphLayout;
    }

    public String getGraphSymbol() {
        return graphSymbol;
    }

    public void setGraphSymbol(String graphSymbol) {
        this.graphSymbol = graphSymbol;
    }

    public Integer getGraphSymbolSize() {
        return graphSymbolSize;
    }

    public void setGraphSymbolSize(Integer graphSymbolSize) {
        this.graphSymbolSize = graphSymbolSize;
    }

    public List<LinkCategory> getLinkCategories() {
        return linkCategories;
    }

    public void setLinkCategories(List<LinkCategory> linkCategories) {
        this.linkCategories = linkCategories;
    }

    public List<DataCategory> getDataCategories() {
        return dataCategories;
    }

    public void setDataCategories(List<DataCategory> dataCategories) {
        this.dataCategories = dataCategories;
    }

    public Force getForce() {
        return force;
    }

    public void setForce(Force force) {
        this.force = force;
    }
}
