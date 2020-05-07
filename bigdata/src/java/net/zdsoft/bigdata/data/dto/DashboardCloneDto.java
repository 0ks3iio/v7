package net.zdsoft.bigdata.data.dto;

import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.Cockpit;
import net.zdsoft.bigdata.data.entity.CockpitChart;
import net.zdsoft.bigdata.data.entity.CockpitChartStyle;
import net.zdsoft.bigdata.data.entity.CockpitStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/8/22 9:46
 */
public class DashboardCloneDto {

    private Cockpit cockpit;
    private CockpitStyle cockpitStyle;
    private List<CockpitChart> cockpitCharts;
    private List<CockpitChartStyle> cockpitChartStyles;
    private List<Chart> cloneCharts;

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

    public List<Chart> getCloneCharts() {
        return cloneCharts;
    }

    public void setCloneCharts(List<Chart> cloneCharts) {
        this.cloneCharts = cloneCharts;
    }
}
