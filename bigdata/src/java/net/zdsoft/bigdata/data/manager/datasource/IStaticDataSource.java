package net.zdsoft.bigdata.data.manager.datasource;

import org.slf4j.Logger;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午5:47
 * @deprecated 静态数据源
 */
public class IStaticDataSource extends AbstractDataSource {

    @Override
    protected Logger getLogger() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws IDataSourceInitException {

    }

    @Override
    public Object executeQuery(String queryStatement) throws IQueryException {
        return null;
    }

    @Override
    public Object executeQuery(String queryStatement, long timeout) throws IQueryException {
        return null;
    }

    @Override
    public void destroy() throws IQueryException {

    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public String getInitStackTrace() {
        return null;
    }
}
