package net.zdsoft.bigdata.data.action.convert.echarts;

import net.zdsoft.bigdata.data.action.convert.echarts.op.Option;

/**
 * 所有实现该接口的应当在初始化之后调用
 * {@code Root{@link #option(Option)}}
 * @author ke_shen@126.com
 * @since 2018/4/12 下午12:50
 */
public interface Root<T extends Root> {

    /** 方便编程 */
    Option option();

    T option(Option option);
}
