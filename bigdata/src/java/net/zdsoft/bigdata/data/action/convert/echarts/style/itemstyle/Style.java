/**
 * FileName: Style
 * Author:   shenke
 * Date:     2018/4/25 上午9:43
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.style.itemstyle;

import java.util.function.Supplier;

/**
 * @author shenke
 * @since 2018/4/25 上午9:43
 */
public abstract class Style<T extends Style> {

    private Object color;

    public T color(Object color) {
        this.color = color;
        return (T) this;
    }

    public T color(Supplier<Object> color) {
        this.color = color.get();
        return (T) this;
    }

    public Object getColor() {
        return color;
    }
}
