package net.zdsoft.bigdata.datax.job.writer;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.JobContentWriterParameter;
import net.zdsoft.bigdata.datax.entity.JobContentWriterParameterConnection;
import net.zdsoft.bigdata.datax.entity.MetadataTransfer;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonWriterParameterBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;

import static net.zdsoft.bigdata.data.DatabaseType.MYSQL;

/**
 * Created by wangdongdong on 2019/6/17 18:20.
 */
public class MysqlWriterParameterBuilder extends CommonWriterParameterBuilder {

    @Override
    public JobContentParameter getJobContentWriterParameter(Map<String, String> paramMap, MetadataTransfer metadata) throws BigDataBusinessException {
        JobContentWriterParameter wParameter = new JobContentWriterParameter();
        JobContentWriterParameterConnection wConnection = new JobContentWriterParameterConnection();
        Map<String, String> mysqlParamMap = getFrameParamMap();
        Database database = new Database();
        database.setDbName(mysqlParamMap.get("database"));
        database.setType(MYSQL.getType());
        database.setPort(Integer.parseInt(mysqlParamMap.get("port")));
        database.setUsername(mysqlParamMap.get("user"));
        database.setPassword(mysqlParamMap.get("password"));
        database.setDomain(mysqlParamMap.get("domain"));
        Pair<String, DatabaseType> wPair = IDataSourceUtils.buildJDBCUrl(database);
        wConnection.setJdbcUrl(wPair.getLeft());
        wConnection.setTable(Lists.newArrayList(metadata.getTableName()));

        String writerColumn = paramMap.get(DataxJobParamEnum.WRITER_COLUMN.getCode());
        if (StringUtils.isBlank(writerColumn)) {
            throw new BigDataBusinessException("目标数据字段未维护");
        }
        wParameter.setColumn(Arrays.asList(writerColumn.split(",")))
                .setConnection(Lists.newArrayList(wConnection))
                .setUsername(database.getUsername())
                .setPassword(database.getPassword());
        String presql = paramMap.get(DataxJobParamEnum.PRE_SQL.getCode());
        String postSql = paramMap.get(DataxJobParamEnum.POST_SQL.getCode());
        wParameter.setPreSql(presql == null ? null : Lists.newArrayList(presql));
        wParameter.setPostSql(postSql == null ? null : Lists.newArrayList(postSql));
        String session = paramMap.get(DataxJobParamEnum.WRITE_SESSION.getCode());
        wParameter.setSession(session == null ? null : Lists.newArrayList(session));
        try {
            wParameter.setBatchSize(Long.valueOf(paramMap.get(DataxJobParamEnum.BATCH_SIZE.getCode())));
        } catch (Exception e) {
            wParameter.setBatchSize(null);
        }
        return wParameter;
    }
}
