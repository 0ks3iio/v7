/**
 * FileName: CockpitOptionVO.java
 * Author:   shenke
 * Date:     2018/5/8 下午3:25
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import net.zdsoft.bigdata.data.echarts.OptionEx;

import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/5/8 下午3:25
 */
public class CockpitOptionVO {

    //特殊的divId，去除前面的所有的0
    private String divId;
    private OptionEx option;
    private String divName;
    private String chartId;
    private boolean autoRefresh;
    private Integer autoRefreshInterval;

    public boolean isAutoRefresh() {
        return autoRefresh;
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

    public String getDivId() {
        return divId;
    }

    public OptionEx getOption() {
        return option;
    }

    public String getDivName() {
        return divName;
    }

    public String getChartId() {
        return chartId;
    }

    public CockpitOptionVO setChartId(String chartId) {
        this.chartId = chartId;
        return this;
    }

    public static final class Builder {
        //特殊的divId，去除前面的所有的0
        private String divId;
        private OptionEx option;
        private String divName;

        private Builder() {
        }

        public static Builder aCockpitOptionVO() {
            return new Builder();
        }

        public Builder divId(String divId) {
            this.divId = divId;
            return this;
        }

        public Builder option(OptionEx option) {
            this.option = option;
            return this;
        }

        public Builder divName(String divName) {
            this.divName = divName;
            return this;
        }

        public Builder but() {
            return aCockpitOptionVO().divId(divId).option(option).divName(divName);
        }

        public CockpitOptionVO build() {
            CockpitOptionVO cockpitOptionVO = new CockpitOptionVO();
            cockpitOptionVO.divName = this.divName;
            cockpitOptionVO.option = this.option;
            cockpitOptionVO.divId = this.divId;
            return cockpitOptionVO;
        }
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
