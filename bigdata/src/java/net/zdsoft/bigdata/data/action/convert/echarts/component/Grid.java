/**
 * FileName: Grid
 * Author:   shenke
 * Date:     2018/4/25 下午3:50
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.component;

import net.zdsoft.bigdata.data.action.convert.echarts.BaseComponent;

/**
 * @author shenke
 * @since 2018/4/25 下午3:50
 */
public class Grid extends BaseComponent<Grid> {

    public static Grid empty() {
        return new Grid().show(false);
    }

    private Object left;
    private Object right;
    private Object bottom;
    private Object top;
    private Boolean containLabel;

    public Grid() {
        this.containLabel = Boolean.FALSE;
    }

    public Grid left(Object left) {
        this.left = left;
        return this;
    }

    public Grid right(Object right) {
        this.right = right;
        return this;
    }

    public Grid bottom(Object bottom) {
        this.bottom = bottom;
        return this;
    }

    public Grid top(Object top) {
        this.top = top;
        return this;
    }

    public Grid containLabel(Boolean containLabel) {
        this.containLabel = containLabel;
        return this;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public Object getBottom() {
        return bottom;
    }

    public Object getTop() {
        return top;
    }

    public Boolean getContainLabel() {
        return containLabel;
    }
}
