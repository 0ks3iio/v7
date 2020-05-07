package net.zdsoft.bigdata.datasource.csv;

import net.zdsoft.bigdata.datasource.DataType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.datasource.AbstractQuery;
import net.zdsoft.bigdata.datasource.QueryExtractor;
import net.zdsoft.bigdata.datasource.QueryStatement;
import net.zdsoft.framework.utils.ExcelUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shenke
 * @since 2018/11/27 上午10:35
 */
@Component(CsvQuery.NAME)
public class CsvQuery extends AbstractQuery {

    public static final String NAME = "csvQuery";

    private Set<DataType> supportTypes = new HashSet<DataType>() {{
        add(DatabaseType.CSV);
    }};

    @Override
    protected <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws Throwable {
        try (InputStream inputStream = new FileInputStream(new File(queryStatement.getQueryStatement()))) {
            return extractor.extractData(ExcelUtils.readCvsFromStream(inputStream));
        }

    }

    @Override
    protected Set<DataType> getSupportDatabaseType() {
        return supportTypes;
    }
}
