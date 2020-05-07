package net.zdsoft.bigdata.datasource.resful;

import net.zdsoft.bigdata.datasource.CheckResponse;
import net.zdsoft.bigdata.datasource.DataType;
import net.zdsoft.bigdata.datasource.AbstractQuery;
import net.zdsoft.bigdata.datasource.QueryStatement;
import net.zdsoft.bigdata.datasource.QueryStatementWithArgs;
import net.zdsoft.bigdata.datasource.QueryExtractor;
import net.zdsoft.bigdata.datasource.Statement;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/11/27 上午9:48
 */
@Component(RestQuery.NAME)
public class RestQuery extends AbstractQuery {

    public static final String NAME = "restQuery";
    private Set<DataType> supportTypes = new HashSet<DataType>(1){{add(DataType.api());}};

    @Override
    protected <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws Throwable {
        Object response = null;
        if (queryStatement instanceof QueryStatementForPost) {
            response = RestQueryUtils.doPost(queryStatement.getQueryStatement(), ((QueryStatementForPost) queryStatement).getRequest());
        }
        else if (queryStatement instanceof QueryStatementWithArgs) {
            response = RestQueryUtils.doGet(queryStatement.getQueryStatement(), ((QueryStatementWithArgs)queryStatement).getArgs());
        }
        else {
            response = RestQueryUtils.doGet(queryStatement.getQueryStatement(), null);
        }
        return extractor.extractData(response);
    }

    @Override
    protected Set<DataType> getSupportDatabaseType() {
        return supportTypes;
    }

    @Override
    public CheckResponse check(Statement statement) {
        throw new RuntimeException("Not support rest url check");
    }
}
