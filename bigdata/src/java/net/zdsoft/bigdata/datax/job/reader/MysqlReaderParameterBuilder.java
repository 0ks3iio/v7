package net.zdsoft.bigdata.datax.job.reader;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.bigdata.datax.entity.JobContentReaderParameter;
import net.zdsoft.bigdata.datax.entity.JobContentReaderParameterConnection;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonReaderParameterBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:03.
 */
public class MysqlReaderParameterBuilder extends CommonReaderParameterBuilder {

    @Override
    public JobContentReaderParameter getJobContentReaderParameter(Map<String, String> paramMap, AbstractDatabase database) throws BigDataBusinessException {
        JobContentReaderParameter parameter = new JobContentReaderParameter();
        JobContentReaderParameterConnection connection = new JobContentReaderParameterConnection();

        Database source = (Database) database;
        Pair<String, DatabaseType> pair = IDataSourceUtils.buildJDBCUrl(source);
        String querySql = paramMap.get(DataxJobParamEnum.QUERY_SQL.getCode());
        String readerColumn = paramMap.get(DataxJobParamEnum.READER_COLUMN.getCode());
        if (StringUtils.isBlank(querySql)) {
            if (StringUtils.isBlank(readerColumn)) {
                throw new BigDataBusinessException("源数据字段未维护");
            }
            parameter.setColumn(Arrays.asList(readerColumn.split(",")));
            connection.setTable(Lists.newArrayList(paramMap.get(DataxJobParamEnum.READER_TABLE.getCode())));
        } else {
            connection.setQuerySql(Lists.newArrayList(querySql));
        }

        parameter.setWhere(paramMap.get(DataxJobParamEnum.READER_WHERE.getCode()))
                .setSplitPk(paramMap.get(DataxJobParamEnum.READER_SPLIT_PK.getCode()));

        try {
            parameter.setFetchSize(Long.valueOf(paramMap.get(DataxJobParamEnum.READER_FETCH_SIZE.getCode())));
        } catch (Exception e) {
            parameter.setFetchSize(null);
        }

        connection.setJdbcUrl(Lists.newArrayList(pair.getLeft()));
        parameter.setConnection(Lists.newArrayList(connection))
                .setUsername(source.getUsername())
                .setPassword(source.getPassword());
        return parameter;
    }
}
