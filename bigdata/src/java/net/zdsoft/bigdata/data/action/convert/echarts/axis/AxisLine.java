/**
 * FileName: AxisLine
 * Author:   shenke
 * Date:     2018/4/25 下午3:58
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.axis;

import net.zdsoft.bigdata.data.action.convert.echarts.BaseComponent;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.LineType;

/**
 * @author shenke
 * @since 2018/4/25 下午3:58
 */
public class AxisLine extends BaseComponent<AxisLine> {

    private LineStyle lineStyle;

    public static AxisLine empty() {
        return new AxisLine();
    }

    public AxisLine() {
        this.show(true);
    }

    public LineStyle lineStyle() {
        if (lineStyle == null) {
            this.lineStyle = new LineStyle();
        }
        return this.lineStyle;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public static class LineStyle {
        private Object color;
        private Integer width;
        private LineType type;
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

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public LineType getType() {
            return type;
        }

        public void setType(LineType type) {
            this.type = type;
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
