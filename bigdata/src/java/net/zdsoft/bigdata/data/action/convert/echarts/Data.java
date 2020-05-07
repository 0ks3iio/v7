package net.zdsoft.bigdata.data.action.convert.echarts;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:14
 */
public interface Data<T> {

    T data(Object ...values);

}
