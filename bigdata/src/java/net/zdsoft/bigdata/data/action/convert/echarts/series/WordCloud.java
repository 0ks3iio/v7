/**
 * FileName: WordCloud.java
 * Author:   shenke
 * Date:     2018/6/12 下午3:14
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.series;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.Align;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;
import net.zdsoft.bigdata.data.action.convert.echarts.style.Label;

/**
 * https://github.com/ecomfe/echarts-wordcloud
 * 词云属于Echarts扩展部分
 * @author shenke
 * @since 2018/6/12 下午3:14
 */
public class WordCloud extends Series<WordCloud> {

    private Object shape;
    private Object left;
    private Object top;
    private Object width;
    private Object height;
    private Object right;
    private Object bottom;
    private Object sizeRange;
    private Object rotationRange;
    private Object rotationStep;
    private Integer gridSize;
    private Boolean drawOutofBound;

    public WordCloud() {
        this.type(SeriesType.wordCloud);
    }

    public Object getShape() {
        return shape;
    }

    public void setShape(Object shape) {
        this.shape = shape;
    }

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        this.left = left;
    }

    public Object getTop() {
        return top;
    }

    public void setTop(Object top) {
        this.top = top;
    }

    public Object getWidth() {
        return width;
    }

    public void setWidth(Object width) {
        this.width = width;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        this.right = right;
    }

    public Object getBottom() {
        return bottom;
    }

    public void setBottom(Object bottom) {
        this.bottom = bottom;
    }

    public Object getSizeRange() {
        return sizeRange;
    }

    public void setSizeRange(Object sizeRange) {
        this.sizeRange = sizeRange;
    }

    public Object getRotationRange() {
        return rotationRange;
    }

    public void setRotationRange(Object rotationRange) {
        this.rotationRange = rotationRange;
    }

    public Object getRotationStep() {
        return rotationStep;
    }

    public void setRotationStep(Object rotationStep) {
        this.rotationStep = rotationStep;
    }

    public Integer getGridSize() {
        return gridSize;
    }

    public void setGridSize(Integer gridSize) {
        this.gridSize = gridSize;
    }

    public Boolean getDrawOutofBound() {
        return drawOutofBound;
    }

    public void setDrawOutofBound(Boolean drawOutofBound) {
        this.drawOutofBound = drawOutofBound;
    }

    public static class TextStyle {
        private Normal normal;
        private Emphasis emphasis;

        public Normal getNormal() {
            return normal;
        }

        public void setNormal(Normal normal) {
            this.normal = normal;
        }

        public Emphasis getEmphasis() {
            return emphasis;
        }

        public void setEmphasis(Emphasis emphasis) {
            this.emphasis = emphasis;
        }
    }

    public static class Normal extends Label.Style<Normal> {

    }

    public static class Emphasis extends Label.Style<Emphasis> {

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
