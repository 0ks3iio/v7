package net.zdsoft.bigdata.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/11/26 下午2:40
 */
public abstract class AbstractQuery implements Query {

    private List<Filter> filters ;
    private int BREAK = 0;


    @Override
    public <Q extends QueryStatement, VL> QueryResponse<VL> query(Q queryStatement, QueryExtractor<VL> extractor) {
        applyFilter(queryStatement);
        try {
            return new QueryResponse<>(execute(queryStatement, extractor));
        } catch (Throwable throwable) {
            return QueryResponse.error(throwable);
        }
    }

    @Override
    public boolean isSupport(DataType dataType) {
        return getSupportDatabaseType().contains(dataType);
    }

    protected abstract <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws Throwable;

    protected abstract Set<DataType> getSupportDatabaseType();


    private void applyFilter(QueryStatement queryStatement) {
        if (getFilters().isEmpty()) {
            return;
        }
        int i = 0;
        for (;;) {
            if (BREAK == i) {
                break;
            }
            getFilters().get(i).doFilter(queryStatement);
            i++;
        }
    }

    private List<Filter> getFilters() {
        if (filters == null) {
            synchronized (Query.class) {
                if (filters == null) {
                    filters = new ArrayList<>();
                    ServiceLoader.load(Filter.class).iterator().forEachRemaining(filter -> filters.add(filter));
                    BREAK = filters.size();
                }
            }
        }
        return filters;
    }
}
