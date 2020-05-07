package net.zdsoft.bigdata.data.manager;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.api.Invocation;

/**
 * timeout不参与hashCode计算和equals比较
 *
 * @author ke_shen@126.com
 * @since 2018/4/8 上午11:53
 */
class IQueryInvocation extends Invocation {

    private DataSourceType type;
    private String queryStatement;
    private String dataSourceId;
    private boolean autoUpdate;
    private long updateInterval = -1;
    private boolean autoRetry;
    private int autoReties = -1;
    private long timeout;

    public IQueryInvocation(DataSourceType type, String queryStatement, String dataSourceId,
                            boolean autoUpdate, long updateInterval, boolean autoRetry,
                            int autoReties, long timeout) {
        this.type = type;
        this.queryStatement = queryStatement;
        this.dataSourceId = dataSourceId;
        this.autoUpdate = autoUpdate;
        this.updateInterval = updateInterval;
        this.autoRetry = autoRetry;
        this.autoReties = autoReties;
        this.timeout = timeout;
    }

    @Override
    public DataSourceType getDataSourceType() {
        return this.type;
    }

    @Override
    public String getQueryStatement() {
        return this.queryStatement;
    }

    @Override
    public long timeout() {
        return this.timeout;
    }

    @Override
    public String getDataSourceId() {
        return this.dataSourceId;
    }

    @Override
    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    @Override
    public long updateInterval() {
        return updateInterval;
    }

    @Override
    public boolean isAutoRetry() {
        return autoRetry;
    }

    @Override
    public int autoRetries() {
        return autoReties;
    }

    @Override
    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }
}
