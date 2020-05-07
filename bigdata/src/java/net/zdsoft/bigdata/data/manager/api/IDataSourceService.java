package net.zdsoft.bigdata.data.manager.api;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceInitException;

/**
 * 数据源抽象接口，用于查询数据源是否可用，获取数据源等
 *
 * @author ke_shen@126.com
 * @since 2018/4/17 上午9:20
 */
public interface IDataSourceService {

    /**
     * 根据dataSourceId和数据源类型获取数据源
     * 若数据源已被创建则直接返回,若数据源不存在则根据相关信息创建数据源
     * 若创建失败，则缓存一个不可用的数据源对象并缓存，同时抛出{@link net.zdsoft.bigdata.data.manager.datasource.IDataSourceInitException}
     *
     * @see IDataSourceInitException
     */
    IDataSource getIDataSource(String dataSourceId, DataSourceType type) throws IDataSourceInitException;

    /**
     * 更新数据源信息
     * 为了提高update速度，这里采用lazy策略
     * 只会创建数据源，只有真正使用的时候才会建立连接、数据库连接池等
     * @see IDataSourceInitException
     */
    void updateIDataSource(String dataSourceId, DataSourceType type) throws IDataSourceInitException;

    /**
     * 目前仅支持数据库数据源的检查
     * 检查数据源是否可用
     * 当数据源不可用时抛出IDatSourceException异常信息
     * 可通过 IDataSourceInitException#getCause() 获取原始异常信息
     * 可通过{@code {@link net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils#createLazyIDataSource(Database)}}
     * 将Database转换为IDataSource
     *
     * @return 返回true代表数据源可用 false或者抛出异常信息代表数据源不可用
     * @see IDataSourceInitException
     * @see net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils#createLazyIDataSource(Database)
     */
    boolean checkDataSource(IDataSource dataSource) throws IDataSourceInitException;

    /**
     * 检查某一个数据源是否被使用
     * * 可通过{@code {@link net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils#createLazyIDataSource(Database)}}
     * 将Database转换为IDataSource
     *
     * @param sourceId sourceId 是ApiId或者databaseId
     * @param dataSourceType  数据源类型，一般为{@link DataSourceType#API}或者 {@link DataSourceType#DB}
     *                        不能为空，因为根据sourId无法确定是为database还是API的Id，两者是不同表的主键有可能重复
     * @return false 数据源没有被引用 true数据源已被引用
     * @see net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils#createLazyIDataSource(Database)
     * @see DataSourceType
     */
    boolean isUsed(String sourceId, DataSourceType dataSourceType);
}
