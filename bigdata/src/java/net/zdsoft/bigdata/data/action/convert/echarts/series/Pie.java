package net.zdsoft.bigdata.data.action.convert.echarts.series;

import static net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType.pie;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:42
 */
final public class Pie extends Series<Pie> {

    private LabelLine labelLine;
    private Object radius;
    /**
     * 可以是false或者 {@link net.zdsoft.bigdata.data.action.convert.echarts.enu.RoseType}
     * @see net.zdsoft.bigdata.data.action.convert.echarts.enu.RoseType
     */
    private Object roseType;


    public Pie() {
        super();
        this.type(pie);
    }

    public Pie(String name) {
        super(name);
        this.type(pie);
    }

    public LabelLine labelLine() {
        if (this.labelLine == null) {
            this.labelLine = new LabelLine();
        }
        return this.labelLine;
    }

    public LabelLine getLabelLine() {
        return labelLine;
    }

    public void setLabelLine(LabelLine labelLine) {
        this.labelLine = labelLine;
    }

    public Object getRadius() {
        return radius;
    }

    public void setRadius(Object radius) {
        this.radius = radius;
    }

    public Object getRoseType() {
        return roseType;
    }

    public void setRoseType(Object roseType) {
        this.roseType = roseType;
    }

    public static class LabelLine {

        private Normal normal;

        public Normal normal() {
            if (this.normal == null) {
                this.normal = Normal.empty();
            }
            return this.normal;
        }

        public LabelLine normal(Normal normal) {
            this.normal = normal;
            return this;
        }

        public Normal getNormal() {
            return normal;
        }

        public static class Normal {

            public static Normal empty() {
                return new Normal().show(true);
            }

            private Boolean show;

            public Normal show(Boolean show) {
                this.show = show;
                return this;
            }

            public Boolean getShow() {
                return show;
            }
        }
    }
}
