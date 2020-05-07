package net.zdsoft.bigdata.data.manager.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dao.ChartDao;
import net.zdsoft.bigdata.data.dao.CockpitChartDao;
import net.zdsoft.bigdata.data.dao.DatabaseDao;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.manager.api.IDataSource;
import net.zdsoft.bigdata.data.manager.api.IDataSourceService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 查询并封装数据源
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 下午4:13
 */
@Component
public class IDataSourceServiceImpl implements IDataSourceService, DisposableBean {

    private static Map<String, IDataSource> dataSourceMap = new ConcurrentHashMap<>(16);

    private Logger logger = LoggerFactory.getLogger(IDataSourceServiceImpl.class);

    @Resource
    private DatabaseDao databaseDao;
    @Resource
    private CockpitChartDao cockpitChartDao;
    @Resource
    private ChartDao chartDao;
    @Resource(name = IHttpComponentsApiDataSource.API_DATASOURCE)
    private IDataSource apiDataSource;


    @Override
    public void destroy() throws Exception {
        if (!dataSourceMap.isEmpty()) {
            for (Map.Entry<String, IDataSource> dataSourceEntry : dataSourceMap.entrySet()) {
                dataSourceEntry.getValue().destroy();
            }
        }
    }

    private IDataSource createIDataSource(String dataSourceId, DataSourceType type, boolean lazy) throws IDataSourceInitException {
        IDataSource iDataSource = null;
        if (DataSourceType.DB.equals(type)) {
            Database database = databaseDao.findById(dataSourceId).orElse(null);
            IDataSourceImpl dataSourceImpl = (IDataSourceImpl) IDataSourceUtils.createLazyIDataSource(database);

            try {
                if (!lazy) {
                    dataSourceImpl.afterPropertiesSet();
                }
                iDataSource = dataSourceImpl;
            } catch (IDataSourceInitException e) {
                iDataSource = new IDataSourceImpl(dataSourceImpl.getDatabaseType(), false, ExceptionUtils.getStackTrace(e),
                        dataSourceImpl.getUri(), database.getUsername(), database.getPassword());
                dataSourceMap.put(dataSourceId, iDataSource);
                throw e;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("jdbcUrl {}", iDataSource.getUri());
            }
        } else if (DataSourceType.API.equals(type)) {
            return apiDataSource;
        } else if (DataSourceType.STATIC.equals(type)) {
            logger.warn("not support static dataSource like json excel or csv");
            //throw new IQueryException("未实现静态数据源查询", dataSourceId, null);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("IDataSource created id {}, type {}", dataSourceId, type.toString());
        }
        return iDataSource;
    }

    /**
     * 根据数据源ID和数据源类型封装数据源, 当数据源配置出错时，会抛出IDataSourceInitException
     */
    @Override
    public IDataSource getIDataSource(String dataSourceId, DataSourceType type) throws IDataSourceInitException {
        IDataSource iDataSource = dataSourceMap.get(dataSourceId);
        if (iDataSource == null) {
            iDataSource = createIDataSource(dataSourceId, type, false);
            dataSourceMap.put(dataSourceId, iDataSource);
        }
        return iDataSource;
    }

    /**
     * 更新数据源配置
     */
    @Override
    public void updateIDataSource(String dataSourceId, DataSourceType dataSourceType) throws IDataSourceInitException {
        if (StringUtils.isBlank(dataSourceId)) {
            logger.error("update IDataSource but dataSourceId is null");
            return;
        }
        IDataSource iDataSource = null;
        if (dataSourceMap.get(dataSourceId) == null) {
            return;
        }
        iDataSource = createIDataSource(dataSourceId, dataSourceType, true);
        dataSourceMap.put(dataSourceId, iDataSource);
    }

    @Override
    public boolean checkDataSource(IDataSource dataSource) throws IDataSourceInitException {
        //数据库
        if (DataSourceType.DB.equals(dataSource.getDataSourceType())) {
            IDataSourceImpl dataSourceImpl = (IDataSourceImpl) dataSource;
            try {
                return IDataSourceUtils.checkDatabaseConfig(dataSourceImpl.getUsername(), dataSourceImpl.getPassword(),
                        dataSourceImpl.getUri(), dataSourceImpl.getDatabaseType());
            } catch (IDataSourceInitException e) {
                logger.error("数据源配置错误", e);
                return false;
            } catch (ClassNotFoundException e) {
                throw new IDataSourceInitException("缺少数据库驱动", e, dataSourceImpl.getUri());
            }
        } else {
            throw new IllegalArgumentException("不支持的数据源类型");
        }
    }

    @Override
    public boolean isUsed(String sourceId, DataSourceType dataSourceType) {
        if (DataSourceType.DB.equals(dataSourceType)) {
            return chartDao.countByDatabaseId(sourceId) > 0;
        } else if (DataSourceType.API.equals(dataSourceType)) {
            return chartDao.countByApiId(sourceId) > 0;
        } else {
            throw new IllegalArgumentException("静态数据源不支持引用判断");
        }
    }

    static class LazyIDataSource extends IDataSourceImpl {

        private volatile boolean init;


        LazyIDataSource(String username, String password, String uri, DatabaseType databaseType) {
            super(username, password, uri, databaseType);
            this.init = false;
            this.enable = true;
        }

        public void init() {
            try {
                if (!this.init) {
                    synchronized (LazyIDataSource.class) {
                        if (!this.init) {
                            afterPropertiesSet();
                            this.init = true;
                        }
                    }
                }
            } catch (IDataSourceInitException e) {
                this.enable = false;
                this.stackTrace = ExceptionUtils.getStackTrace(e);
            }
        }

        @Override
        public Object executeQuery(String queryStatement) throws IQueryException {
            if (!this.init) {
                init();
            }
            return super.executeQuery(queryStatement);
        }

        @Override
        public Object executeQuery(String queryStatement, long timeout) throws IQueryException {
            if (!this.init) {
                init();
            }
            return super.executeQuery(queryStatement, timeout);
        }

        @Override
        public void destroy() throws IQueryException {
            if (this.init) {
                super.destroy();
            }
        }

        @Override
        public boolean enable() {
            if (!this.init) {
                init();
            }
            return super.enable();
        }
    }
}
