/**
 * FileName: Scatter
 * Author:   shenke
 * Date:     2018/4/19 上午10:43
 * Descriptor: 散点图series
 */
package net.zdsoft.bigdata.data.action.convert.echarts.series;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.BorderType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;

/**
 * @author shenke
 * @since 2018/4/19 上午10:43
 */
final public class Scatter extends Series<Scatter> {

    private ItemStyle itemStyle;

    public Scatter() {
        this.type(SeriesType.scatter);
    }

    public Scatter(String name) {
        this.type(SeriesType.scatter);
        this.name(name);
    }

    public ItemStyle getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(ItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }

    public static class ItemStyle {
        private Object color;
        private Object borderColor;
        private Object borderWidth;
        private BorderType borderType;
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

        public Object getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(Object borderColor) {
            this.borderColor = borderColor;
        }

        public Object getBorderWidth() {
            return borderWidth;
        }

        public void setBorderWidth(Object borderWidth) {
            this.borderWidth = borderWidth;
        }

        public BorderType getBorderType() {
            return borderType;
        }

        public void setBorderType(BorderType borderType) {
            this.borderType = borderType;
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
