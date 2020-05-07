package net.zdsoft.bigdata.data.vo;

import net.zdsoft.bigdata.data.entity.BorderType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author shenke
 * @since 2018/8/7 上午9:13
 */
public class DashboardCockpitChartVo {

    private String id;
    private Integer zIndex;
    private Integer width;
    private Integer height;
    private Integer top;
    private Integer left;
    private String chartId;
    @Enumerated(EnumType.ORDINAL)
    private BorderType borderType;
    private String backgroundColor;

    private DashboardTextChartVo dashboardTextChartVo;
    private boolean text;
    private boolean other;
    private DashboardOtherChartVo dashboardOtherChartVo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public DashboardTextChartVo getDashboardTextChartVo() {
        return dashboardTextChartVo;
    }

    public void setDashboardTextChartVo(DashboardTextChartVo dashboardTextChartVo) {
        this.dashboardTextChartVo = dashboardTextChartVo;
    }

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public DashboardOtherChartVo getDashboardOtherChartVo() {
        return dashboardOtherChartVo;
    }

    public void setDashboardOtherChartVo(DashboardOtherChartVo dashboardOtherChartVo) {
        this.dashboardOtherChartVo = dashboardOtherChartVo;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
