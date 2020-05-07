package net.zdsoft.bigdata.data.manager.api;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceInitException;
import net.zdsoft.bigdata.data.manager.datasource.IHttpComponentsApiDataSource;
import net.zdsoft.bigdata.data.manager.datasource.IQueryException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.sql.ResultSet;

/**
 * 自定义的数据源，封装包括数据库、
 * excel、json等相关参数在内的一切数据配置
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 下午1:14
 */
public interface IDataSource extends DisposableBean, InitializingBean {

    /**
     * 查询数据 返回ResultSet结果集
     */
    ResultSet executeResultSet(String queryStatement) throws IQueryException;

    /**
     * 查询数据 返回ResultSet结果集
     * @param queryStatement
     * @param timeout 超时时间
     * @return
     * @throws IQueryException
     */
    ResultSet executeResultSet(String queryStatement, long timeout) throws IQueryException;

    /**
     * 查询数据 返回JSON格式的数据
     */
    Object executeQuery(String queryStatement) throws IQueryException;

    /**
     * 查询数据指定超时间 ms, 对于API调用除非特殊请求否则不要指定超时时间
     *
     * @see IHttpComponentsApiDataSource#executeQuery(String, long)
     */
    Object executeQuery(String queryStatement, long timeout) throws IQueryException;

    /**
     * 执行sql 创建表
     *
     */
    Object executeCreateSql(String createSql) throws IQueryException;

    /**
     * 数据库连接需要的用户名
     */
    String getUsername();

    /**
     * 数据库连接需要的密码
     */
    String getPassword();

    /**
     * 数据源类型
     */
    DataSourceType getDataSourceType();

    /**
     * jdbc http file都要封装成URI或者类URI格式
     */
    String getUri();

    /**
     * 销毁数据源
     */
    void destroy() throws IQueryException;

    /**
     * 是否可用
     */
    boolean enable();

    /**
     * 当该数据源不可用时可得到初始化时的异常堆栈信息
     */
    String getInitStackTrace();

    @Override
    void afterPropertiesSet() throws IDataSourceInitException;
}
