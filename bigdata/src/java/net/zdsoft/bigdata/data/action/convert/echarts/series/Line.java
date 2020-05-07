package net.zdsoft.bigdata.data.action.convert.echarts.series;

import static net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType.line;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Origin;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:43
 */
final public class Line extends Series<Line> {

    private Boolean smooth;
    private Object areaStyle;

    public Line() {
        this.type(line);
    }

    public Line(String name) {
        super(name);
        this.type(line);
    }

    public Line smooth(Boolean smooth) {
        this.smooth = smooth;
        return this;
    }

    public AreaStyle areaStyle() {
        if (areaStyle == null) {
            areaStyle = new AreaStyle();
        }
        return (AreaStyle) areaStyle;
    }

    public Boolean smooth() {
        return this.smooth;
    }

    public Boolean getSmooth() {
        return smooth;
    }

    public Object getAreaStyle() {
        return  areaStyle;
    }

    public void setAreaStyle(Object areaStyle) {
        this.areaStyle = areaStyle;
    }

    public static class AreaStyle {
        private Object color;
        private Origin origin;
        private Integer shadowBlur;
        private Object shadowColor;
        private Integer shadowOffsetX;
        private Integer shadowOffsetY;
        private Object opacity;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

        public Origin getOrigin() {
            return origin;
        }

        public void setOrigin(Origin origin) {
            this.origin = origin;
        }

        public Integer getShadowBlur() {
            return shadowBlur;
        }

        public void setShadowBlur(Integer shadowBlur) {
            this.shadowBlur = shadowBlur;
        }

        public Object getShadowColor() {
            return shadowColor;
        }

        public void setShadowColor(Object shadowColor) {
            this.shadowColor = shadowColor;
        }

        public Integer getShadowOffsetX() {
            return shadowOffsetX;
        }

        public void setShadowOffsetX(Integer shadowOffsetX) {
            this.shadowOffsetX = shadowOffsetX;
        }

        public Integer getShadowOffsetY() {
            return shadowOffsetY;
        }

        public void setShadowOffsetY(Integer shadowOffsetY) {
            this.shadowOffsetY = shadowOffsetY;
        }

        public Object getOpacity() {
            return opacity;
        }

        public void setOpacity(Object opacity) {
            this.opacity = opacity;
        }
    }
}
