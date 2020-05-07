/**
 * FileName: ItemStyle
 * Author:   shenke
 * Date:     2018/4/26 上午11:21
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.style.itemstyle;

import java.util.function.Supplier;

/**
 * @author shenke
 * @since 2018/4/26 上午11:21
 */
public class ItemStyle {

    private Normal normal;

    public ItemStyle normal(Supplier<Normal> loader) {
        this.normal = loader.get();
        return this;
    }

    public Normal normal() {
        if (this.normal == null) {
            this.normal = new Normal();
        }
        return this.normal;
    }

    public Normal getNormal() {
        return normal;
    }

    public static class Normal {
        private Object color;

        public Normal color(Supplier<Object> loader) {
            this.color = loader.get();
            return this;
        }

        public Object getColor() {
            return color;
        }
    }
}
