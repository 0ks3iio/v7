package net.zdsoft.bigdata.datasource;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/11/27 下午2:36
 */
@Component(StaticJsonQuery.NAME)
public class StaticJsonQuery extends AbstractQuery {

    public static final String NAME = "staticJsonQuery";

    private Set<DataType> supportTypes = new HashSet<DataType>(1){{add(DataType.json());}};

    @Override
    protected <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws Throwable {
        return extractor.extractData(queryStatement.getQueryStatement());
    }

    @Override
    protected Set<DataType> getSupportDatabaseType() {
        return supportTypes;
    }
}
