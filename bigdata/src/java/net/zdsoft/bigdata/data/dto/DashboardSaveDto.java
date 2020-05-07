package net.zdsoft.bigdata.data.dto;

import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.Cockpit;
import net.zdsoft.bigdata.data.entity.CockpitChart;
import net.zdsoft.bigdata.data.entity.CockpitChartStyle;
import net.zdsoft.bigdata.data.entity.CockpitStyle;

import java.util.List;

/**
 * 保存大屏数据需要保存大屏对应的样式、cockpitChart及其样式
 * @author shenke
 * @since 2018/8/7 上午11:49
 */
public class DashboardSaveDto {

    private Cockpit cockpit;
    private CockpitStyle cockpitStyle;
    private List<CockpitChart> cockpitCharts;
    private List<CockpitChartStyle> cockpitChartStyles;
    private List<Chart> charts;
    private List<Chart> otherCharts;

    public Cockpit getCockpit() {
        return cockpit;
    }

    public void setCockpit(Cockpit cockpit) {
        this.cockpit = cockpit;
    }

    public CockpitStyle getCockpitStyle() {
        return cockpitStyle;
    }

    public void setCockpitStyle(CockpitStyle cockpitStyle) {
        this.cockpitStyle = cockpitStyle;
    }

    public List<CockpitChart> getCockpitCharts() {
        return cockpitCharts;
    }

    public void setCockpitCharts(List<CockpitChart> cockpitCharts) {
        this.cockpitCharts = cockpitCharts;
    }

    public List<CockpitChartStyle> getCockpitChartStyles() {
        return cockpitChartStyles;
    }

    public void setCockpitChartStyles(List<CockpitChartStyle> cockpitChartStyles) {
        this.cockpitChartStyles = cockpitChartStyles;
    }

    public List<Chart> getCharts() {
        return charts;
    }

    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }

    public List<Chart> getOtherCharts() {
        return otherCharts;
    }

    public void setOtherCharts(List<Chart> otherCharts) {
        this.otherCharts = otherCharts;
    }
}
