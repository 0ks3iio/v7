package net.zdsoft.bigdata.data.manager.api;

/**
 * 查询数据的结果集
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 上午11:27
 */
public interface Result {

    /**
     * 最终的结果集，当出现异常时，结果集为optional
     */
    Object getValue();

    /**
     * 查询数据过程中可能出现的异常信息
     */
    Throwable getException();

    /**
     * 是否存在异常信息
     */
    boolean hasError();

}
