/**
 * FileName: BarData
 * Author:   shenke
 * Date:     2018/4/26 上午11:23
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.data;

import java.util.function.Supplier;

import net.zdsoft.bigdata.data.action.convert.echarts.style.itemstyle.ItemStyle;

/**
 * @author shenke
 * @since 2018/4/26 上午11:23
 */
public class BarData {

    private String name;
    private Object value;

    private ItemStyle itemStyle;

    public BarData name(String name) {
        this.name = name;
        return this;
    }

    public BarData value(Object value) {
        this.value = value;
        return this;
    }

    public BarData itemStyle(Supplier<ItemStyle> loader) {
        this.itemStyle = loader.get();
        return this;
    }

    public ItemStyle itemStyle() {
        if (this.itemStyle == null) {
            this.itemStyle = new ItemStyle();
        }
        return this.itemStyle;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public ItemStyle getItemStyle() {
        return itemStyle;
    }
}
