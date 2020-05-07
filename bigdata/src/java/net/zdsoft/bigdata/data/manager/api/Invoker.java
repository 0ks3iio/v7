package net.zdsoft.bigdata.data.manager.api;

/**
 * 数据管理执行器， 主要用于查询数据
 * db（mysql oracle sqlserver、nosql等） 、 api
 * 对于静态资源可以考虑直接转换为JSON字符串放在数据库或者文件中
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 上午11:16
 */
public interface Invoker {

    /**
     * 查询数据, 最终将查询的数据转换为JSONArray 字符串
     */
    Result invoke(Invocation invocation);

}
