/**
 * FileName: Radar.java
 * Author:   shenke
 * Date:     2018/5/30 上午10:32
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.zdsoft.bigdata.data.action.convert.echarts.BaseComponent;
import net.zdsoft.bigdata.data.action.convert.echarts.Component;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.PolarType;

/**
 * @author shenke
 * @since 2018/5/30 上午10:32
 */
public class Radar extends BaseComponent<Radar> implements Component<Radar> {

    private PolarType shape;
    private RadarName name;
    private List<Indicator> indicator;

    public RadarName getName() {
        return name;
    }

    public void setName(RadarName name) {
        this.name = name;
    }

    public List<Indicator> getIndicator() {
        return indicator;
    }

    public List<Indicator> indicator() {
        if (this.indicator == null) {
            this.indicator = new ArrayList<>();
        }
        return this.indicator;
    }

    public Radar indicator(Indicator indicator) {
        indicator().add(indicator);
        return this;
    }

    public Indicator indicator(String name, Supplier<Indicator> supplier) {
        return indicator().stream().filter(i -> i.getName().equals(name)).findFirst()
                .orElseGet(() -> {
                    Indicator prepare = new Indicator();
                    indicator().add(prepare);
                    return prepare;
                });
    }

    public PolarType getShape() {
        return shape;
    }

    public void setShape(PolarType shape) {
        this.shape = shape;
    }

    public static class Indicator {
        private String name;
        private Integer max;
        private Integer min;
        private String color;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

        public void max(Integer prepareMax) {
            if (max != null) {
                max = Math.max(max, prepareMax);
            } else {
                max = prepareMax;
            }
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }


    }

    public static class RadarName {
        private Boolean show;
        private Object formatter;
        private Object color;
        private Object backgroundColor;
        private Object padding;

        public Boolean getShow() {
            return show;
        }

        public void setShow(Boolean show) {
            this.show = show;
        }

        public Object getFormatter() {
            return formatter;
        }

        public void setFormatter(Object formatter) {
            this.formatter = formatter;
        }

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

        public Object getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(Object backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public Object getPadding() {
            return padding;
        }

        public void setPadding(Object padding) {
            this.padding = padding;
        }
    }
}
