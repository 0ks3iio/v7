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
 * @since 2018/11/27 下午2:13
 */
@Component(ExcelQuery.NAME)
public class ExcelQuery extends AbstractQuery {

    public static final String NAME = "excelQuery";

    private Set<DataType> supportTypes = new HashSet<DataType>(1) {{add(DatabaseType.EXCEL);}};

    @Override
    protected <Q extends QueryStatement, VL> VL execute(Q queryStatement, QueryExtractor<VL> extractor) throws Throwable {
        try (InputStream inputStream = new FileInputStream(new File(queryStatement.getQueryStatement()))) {
            return extractor.extractData(ExcelUtils.readExcelFromStream(inputStream,
                    ((QueryStatementForExcel) queryStatement).getSuffix(), 0));
        }
    }

    @Override
    protected Set<DataType> getSupportDatabaseType() {
        return supportTypes;
    }
}
