/**
 * FileName: Label
 * Author:   shenke
 * Date:     2018/4/25 上午9:41
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.style;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.Align;

/**
 * echarts 3.x版本之后Label和ItemStyle改为平级结构
 *
 * @author shenke
 * @since 2018/4/25 上午9:41
 */
public class Label {

    public static Label empty() {
        return new Label();
    }

    private Normal normal;
    private Emphasis emphasis;

    public Normal normal() {
        if (this.normal == null) {
            this.normal = new Normal();
        }
        return this.normal;
    }

    public Label normal(Normal normal) {
        this.normal = normal;
        return this;
    }

    public Emphasis emphasis() {
        if (this.emphasis == null) {
            this.emphasis = new Emphasis();
        }
        return this.emphasis;
    }

    public Label emphasis(Emphasis emphasis) {
        this.emphasis = emphasis;
        return this;
    }

    public Normal getNormal() {
        return normal;
    }

    public Emphasis getEmphasis() {
        return emphasis;
    }

    public static class Normal extends Style<Normal> {

    }

    public static class Emphasis extends Style<Emphasis> {

    }

    public static abstract class Style<T extends Style> {
        private Boolean show;
        private Object position;
        private Integer fontSize;
        private Object fontWeight;
        private Object formatter;
        private Align align;
        private Object color;

        public T show(Boolean show) {
            this.show = show;
            return (T) this;
        }

        public T position(Object position) {
            this.position = position;
            return (T) this;
        }

        public T fontSize(Integer fontSize) {
            this.fontSize = fontSize;
            return (T) this;
        }

        public T fontWeight(Object fontWeight) {
            this.fontWeight = fontWeight;
            return (T) this;
        }

        public T formatter(Object formatter) {
            this.formatter = formatter;
            return (T) this;
        }

        public T align(Align align) {
            this.align = align;
            return (T) this;
        }

        public T color(Object color) {
            this.color = color;
            return (T) this;
        }

        public Boolean getShow() {
            return show;
        }

        public Object getPosition() {
            return position;
        }

        public Integer getFontSize() {
            return fontSize;
        }

        public Object getFontWeight() {
            return fontWeight;
        }

        public Object getFormatter() {
            return formatter;
        }

        public Align getAlign() {
            return align;
        }

        public Object getColor() {
            return color;
        }
    }
}
