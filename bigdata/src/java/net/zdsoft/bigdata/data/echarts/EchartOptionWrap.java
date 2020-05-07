/**
 * FileName: EchartOptionWrap.java
 * Author:   shenke
 * Date:     2018/5/28 下午4:59
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import net.zdsoft.echarts.Option;

import com.alibaba.fastjson.JSON;

/**
 * @author shenke
 * @since 2018/5/28 下午4:59
 */
public class EchartOptionWrap extends OptionEx<Option, EchartOptionWrap> {

    @Override
    public Option getOption() {
        return this.option;
    }

    @Override
    public boolean isEcharts() {
        return true;
    }

    @Override
    public String getChartId() {
        return super.getChartId();
    }

    @Override
    public String getMapName() {
        return super.getMapName();
    }

    @Override
    public String getDivId() {
        return super.getDivId();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public Integer getChartType() {
        return super.getChartType();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public void setAutoRefresh(boolean autoRefresh) {
        super.setAutoRefresh(autoRefresh);
    }

    @Override
    public void setAutoRefreshInterval(Integer autoRefreshInterval) {
        super.setAutoRefreshInterval(autoRefreshInterval);
    }

    @Override
    public void setDivName(String divName) {
        super.setDivName(divName);
    }

    @Override
    public void setChartType(Integer chartType) {
        super.setChartType(chartType);
    }

    @Override
    public void setSuccess(boolean success) {
        super.setSuccess(success);
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }

    @Override
    public void setChartId(String chartId) {
        super.setChartId(chartId);
    }

    @Override
    public void setMapName(String mapName) {
        super.setMapName(mapName);
    }

    @Override
    public void setDivId(String divId) {
        super.setDivId(divId);
    }

    @Override
    public void setOption(Option option) {
        super.setOption(option);
    }
}
