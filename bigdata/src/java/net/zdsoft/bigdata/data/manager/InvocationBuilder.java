package net.zdsoft.bigdata.data.manager;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.manager.api.Invocation;

/**
 * @author ke_shen@126.com
 * @since 2018/4/10 下午6:10
 */
final public class InvocationBuilder implements Invocation.InvocationBuilder {

    private DataSourceType type;
    private String statement;
    private long timeout;
    private String id;
    private long updateInterval;
    private int autoRetries;

    public static InvocationBuilder getInstance() {
        return new InvocationBuilder();
    }

    private InvocationBuilder() {

    }

    @Override
    public Invocation.InvocationBuilder type(DataSourceType dataSourceType) {
        this.type = dataSourceType;
        return this;
    }

    @Override
    public Invocation.InvocationBuilder queryStatement(String statement) {
        this.statement = statement;
        return this;
    }

    @Override
    public Invocation.InvocationBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public Invocation.InvocationBuilder dataSourceId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public Invocation.InvocationBuilder autoRetries(int autoRetries) {
        this.autoRetries = autoRetries;
        return this;
    }

    @Override
    public Invocation.InvocationBuilder updateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    @Override
    public Invocation build() {
        return new IQueryInvocation(type, statement, id, updateInterval > 0,
                updateInterval, autoRetries > 0, autoRetries, timeout);
    }
}
