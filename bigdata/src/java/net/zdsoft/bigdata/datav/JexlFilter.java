package net.zdsoft.bigdata.datav;

import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.bigdata.data.utils.JexlUtils;
import net.zdsoft.bigdata.datasource.Adapter;
import net.zdsoft.bigdata.datasource.DataType;
import net.zdsoft.bigdata.datasource.Filter;
import net.zdsoft.bigdata.datasource.QueryStatement;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author shenke
 * @since 2018/11/27 下午5:43
 */
public class JexlFilter implements Filter {

    @Override
    public void doFilter(QueryStatement queryStatement) {
        Adapter adapter = queryStatement.getAdapter();
        if (needFilter(adapter)) {
            String statement = queryStatement.getQueryStatement();
            if (StringUtils.isBlank(statement)) {
                return;
            }
            Object context = null;
            if ((context = JexlContextHolder.getJexlContext()) == null) {
                return;
            }

            JexlContext adapterContext = null;
            if (context instanceof JexlContext) {
                adapterContext = JexlUtils.JexlContextAdapter.adapterNoQuotes((JexlContext) context);
            }
            else if (context instanceof Map) {
                JexlContext mapContext = new MapContext((Map<String, Object>) context);
                adapterContext = JexlUtils.JexlContextAdapter.adapterNoQuotes(mapContext);
            }
            else {
                JexlContext jexlContext = JexlUtils.createContext(context);
                adapterContext = JexlUtils.JexlContextAdapter.adapterNoQuotes(jexlContext);
            }

            String wrapped = JexlUtils.evaluate(statement, adapterContext);
            wrapped = wrapped.replaceAll("\n", " ");
            queryStatement.setQueryStatement(wrapped);
        }
    }

    private boolean needFilter(Adapter adapter) {
        if (DataType.api().equals(adapter.getDataType())) {
            return true;
        }
        if (adapter instanceof JdbcDatabaseAdapter) {
            return true;
        }
        return false;
    }
}
