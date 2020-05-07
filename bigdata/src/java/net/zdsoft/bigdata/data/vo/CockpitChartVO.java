/**
 * FileName: CockpitChartVO
 * Author:   shenke
 * Date:     2018/4/24 下午6:01
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import java.util.List;

import net.zdsoft.bigdata.data.echarts.OptionEx;

/**
 * 一个CockpitChartVO对应着一个div
 * @author shenke
 * @since 2018/4/24 下午6:01
 */
public class CockpitChartVO {


    private String divId;
    private String divName;
    private Integer order;
    /** 该div所有的图表展示信息 */
    private List<OptionEx> options;

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<OptionEx> getOptions() {
        return options;
    }

    public void setOptions(List<OptionEx> options) {
        this.options = options;
    }


    public static final class Builder {
        private CockpitChartVO cockpitChartVO;

        private Builder() {
            cockpitChartVO = new CockpitChartVO();
        }

        public static Builder aCockpitChartVO() {
            return new Builder();
        }

        public Builder divId(String divId) {
            cockpitChartVO.setDivId(divId);
            return this;
        }

        public Builder divName(String divName) {
            cockpitChartVO.setDivName(divName);
            return this;
        }

        public Builder order(Integer order) {
            cockpitChartVO.setOrder(order);
            return this;
        }

        public Builder options(List<OptionEx> options) {
            cockpitChartVO.setOptions(options);
            return this;
        }

        public Builder but() {
            return aCockpitChartVO().divId(cockpitChartVO.getDivId()).divName(cockpitChartVO.getDivName()).order(cockpitChartVO.getOrder()).options(cockpitChartVO.getOptions());
        }

        public CockpitChartVO build() {
            return cockpitChartVO;
        }
    }

    @Override
    public String toString() {
        return "CockpitChartVO{" +
                "divId='" + divId + '\'' +
                ", divName='" + divName + '\'' +
                ", order=" + order +
                ", options=" + options +
                '}';
    }
}
