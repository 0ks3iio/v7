/**
 * FileName: OptionEx
 * Author:   shenke
 * Date:     2018/4/25 上午11:41
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import java.util.Collection;

import net.zdsoft.bigdata.data.code.ChartCategory;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author shenke
 * @since 2018/4/25 上午11:41
 */
public abstract class OptionEx<O, T extends OptionEx> implements Op<O, T> {

    /** 不属于echarts的额外扩展字段用于和页面交互 */
    private boolean success = true;
    private String message;

    private String chartId;
    private String mapName;
    private String top;
    private String parentParent;
    private String parentMap;
    private String divId;
    private Integer chartType;
    private String divName;

    private boolean autoRefresh;
    private Integer autoRefreshInterval; //s

    public boolean isAutoRefresh() {
        return autoRefresh && autoRefreshInterval>0;
    }

    protected O option;

    /**  是否是echarts图形*/
    public abstract boolean isEcharts();

    public boolean isMap() {
        return ChartCategory.basic_map.getChartType().equals(chartType) || ChartCategory.map_line.getChartType().equals(chartType);
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public Integer getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    public void setAutoRefreshInterval(Integer autoRefreshInterval) {
        this.autoRefreshInterval = autoRefreshInterval;
    }

    public T option(O option) {
        this.option = option;
        return (T) this;
    }

    public T chartId(String chartId) {
        this.chartId = chartId;
        return (T) this;
    }

    public T divId(String divId) {
        this.divId = divId;
        return (T) this;
    }

    public T mapName(String mapName) {
        this.mapName = mapName;
        return (T) this;
    }

    public T success(boolean success) {
        this.success = success;
        return (T) this;
    }

    public T message(String message) {
        this.message = message;
        return (T) this;
    }

    public String getChartId() {
        return chartId;
    }

    public String getMapName() {
        return mapName;
    }

    public String getDivId() {
        return divId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public Integer getChartType() {
        return chartType;
    }
    public void setChartType(Integer chartType) {
        this.chartType = chartType;
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this, (PropertyFilter) (owner, propertyName, value) -> {
            if (value == null ) {
                return false;
            }
            if (value instanceof String) {
                return StringUtils.isNotBlank(value.toString());
            }
            if (value instanceof Collection) {
                if (((Collection)value).isEmpty()) {
                    return false;
                }
                return ((Collection)value).stream().filter(e->e != null && StringUtils.isNotBlank(e.toString())).count() != 0;
            }
            return true;
        }, SerializerFeature.WriteEnumUsingToString);
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public void setOption(O option) {
        this.option = option;
    }

    public String getParentMap() {
        return parentMap;
    }

    public void setParentMap(String parentMap) {
        this.parentMap = parentMap;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getParentParent() {
        return parentParent;
    }

    public void setParentParent(String parentParent) {
        this.parentParent = parentParent;
    }
}
