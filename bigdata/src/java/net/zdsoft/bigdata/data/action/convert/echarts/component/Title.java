package net.zdsoft.bigdata.data.action.convert.echarts.component;

import net.zdsoft.bigdata.data.action.convert.echarts.BaseComponent;
import net.zdsoft.bigdata.data.action.convert.echarts.Component;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午7:54
 */
public class Title extends BaseComponent<Title> implements Component {

    /**
     * 主标题文本 '\n' 可换行
     */
    private String text;
    private String link;
    private String target;
    private String subtext;
    private String sublink;
    private String subtarget;

    /**
     * 一般情况下[ default: 'auto' ]
     * 组件离容器左侧的距离
     * <p>
     * left 的值可以是像 20 这样的具体像素值，可以是像 '20%' 这样相对于容器高宽的百分比，也可以是 'left', 'center', 'right'。
     * <p>
     * 如果 left 的值为'left', 'center', 'right'，组件会根据相应的位置自动对齐
     */
    private Object left = "auto";
    /**
     * 一般情况下[ default: 'auto' ]
     * grid 组件离容器上侧的距离。
     * <p>
     * top 的值可以是像 20 这样的具体像素值，可以是像 '20%' 这样相对于容器高宽的百分比，也可以是 'top', 'middle', 'bottom'。
     * <p>
     * 如果 top 的值为'top', 'middle', 'bottom'，组件会根据相应的位置自动对齐
     */
    private Object top = "auto";
    /**
     * 一般情况下[ default: 'auto' ]
     * 组件离容器右侧的距离。
     * <p>
     * right 的值可以是像 20 这样的具体像素值，可以是像 '20%' 这样相对于容器高宽的百分比。
     */
    private Object right = "auto";
    /**
     * 一般情况下[ default: 'auto' ]
     * 组件离容器右侧的距离。
     * <p>
     * right 的值可以是像 20 这样的具体像素值，可以是像 '20%' 这样相对于容器高宽的百分比。
     */
    private Object bottom = "auto";


    public Title text(String text) {
        this.text = text;
        return this;
    }

    public Title link(String link) {
        this.link = link;
        return this;
    }

    public Title target(String target) {
        this.target = target;
        return this;
    }

    public Title subtext(String subtext) {
        this.subtext = subtext;
        return this;
    }

    public Title sublink(String sublink) {
        this.sublink = sublink;
        return this;
    }

    public Title subtarget(String subtarget) {
        this.subtarget = subtarget;
        return this;
    }

    public Title left(Object left) {
        this.left = left;
        return this;
    }

    public Title right(Object right) {
        this.right = right;
        return this;
    }

    public Title top(Object top) {
        this.top = top;
        return this;
    }

    public Title bottom(Object bottom) {
        this.bottom = bottom;
        return this;
    }

    public String text() {
        return this.text;
    }

    public String link() {
        return this.link;
    }

    public String target() {
        return this.target;
    }

    public String subtext() {
        return this.subtext;
    }

    public String sublink() {
        return this.sublink;
    }

    public String subtarget() {
        return this.subtarget;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public String getTarget() {
        return target;
    }

    public String getSubtext() {
        return subtext;
    }

    public String getSublink() {
        return sublink;
    }

    public String getSubtarget() {
        return subtarget;
    }

    public Object getLeft() {
        return left;
    }

    public Object getTop() {
        return top;
    }

    public Object getRight() {
        return right;
    }

    public Object getBottom() {
        return bottom;
    }
}
