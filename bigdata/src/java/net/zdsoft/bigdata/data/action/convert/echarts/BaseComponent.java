package net.zdsoft.bigdata.data.action.convert.echarts;

import net.zdsoft.bigdata.data.action.convert.echarts.op.Option;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 基础组件类
 * @author ke_shen@126.com
 * @since 2018/4/11 下午7:55
 */
@SuppressWarnings("unchecked")
public abstract class BaseComponent<T extends BaseComponent> implements Root<T> {

    /** 为了方便编程，所有的组件都持有Option对象的引用 */
    @JSONField(serialize = false)
    @javax.persistence.Transient
    @org.springframework.data.annotation.Transient
    private Option option;

    /** 控制是否显示 */
    private Boolean show;

    @Override
    public T option(Option option) {
        this.option = option;
        return (T) this;
    }

    @Override
    public Option option() {
        return this.option;
    }

    public T show(boolean show) {
        this.show = show;
        return (T) this;
    }

    public Boolean show() {
        return this.show;
    }

    public Boolean getShow() {
        return show;
    }
}
