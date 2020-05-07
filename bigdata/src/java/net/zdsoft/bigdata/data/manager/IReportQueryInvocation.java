package net.zdsoft.bigdata.data.manager;

import net.zdsoft.bigdata.data.DataSourceType;

/**
 * Created by wangdongdong on 2018/5/8.
 */
public class IReportQueryInvocation extends IQueryInvocation {

    private IReportQueryInvocation(DataSourceType type, String queryStatement, String dataSourceId, boolean autoUpdate, long updateInterval, boolean autoRetry, int autoReties, long timeout) {
        super(type, queryStatement, dataSourceId, autoUpdate, updateInterval, autoRetry, autoReties, timeout);
    }

    public static IReportQueryInvocation getInstance(DataSourceType type, String queryStatement, String dataSourceId, long updateInterval, int autoRetries, long timeout) {
        return new IReportQueryInvocation(type, queryStatement, dataSourceId, false,
                updateInterval, autoRetries > 0, autoRetries, timeout);
    }
}
