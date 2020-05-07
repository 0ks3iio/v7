package net.zdsoft.bigdata.datax.job.writer;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.MetadataTransfer;
import net.zdsoft.bigdata.datax.entity.es.EsColumn;
import net.zdsoft.bigdata.datax.entity.es.EsJobContentWriterParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonWriterParameterBuilder;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2019/6/17 18:44.
 */
public class EsWriterParameterBuilder extends CommonWriterParameterBuilder {

    @Override
    public JobContentParameter getJobContentWriterParameter(Map<String, String> paramMap, MetadataTransfer metadata) throws BigDataBusinessException {

        EsJobContentWriterParameter parameter = new EsJobContentWriterParameter();
        String endpoint = "http://" + getFrameParamMap().get("elastic_host") + ":" + getFrameParamMap().get("elastic_port");
        parameter.setEndpoint(endpoint);
        parameter.setAccessId(paramMap.get(DataxJobParamEnum.ES_ACCESS_ID.getCode()));
        parameter.setAccessKey(paramMap.get(DataxJobParamEnum.ES_ACCESS_KEY.getCode()));
        parameter.setIndex(paramMap.get(DataxJobParamEnum.ES_INDEX.getCode()));
        parameter.setType(paramMap.get(DataxJobParamEnum.ES_TYPE.getCode()));
        parameter.setCleanup(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.ES_CLEANUP.getCode(), "false")));

        try {
            parameter.setBatchSize(Long.valueOf(paramMap.get(DataxJobParamEnum.BATCH_SIZE.getCode())));
        } catch (Exception e) {
            parameter.setBatchSize(null);
        }

        try {
            parameter.setTrySize(Long.valueOf(paramMap.get(DataxJobParamEnum.ES_TRY_SIZE.getCode())));
        } catch (Exception e) {
            parameter.setTrySize(null);
        }

        try {
            parameter.setTimeout(Long.valueOf(paramMap.get(DataxJobParamEnum.ES_TIMEOUT.getCode())));
        } catch (Exception e) {
            parameter.setTimeout(null);
        }

        parameter.setDiscovery(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.ES_DISCOVERY.getCode(), "false")));
        parameter.setCompression(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.FTP_COMPRESS.getCode(), "true")));
        parameter.setMultiThread(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.ES_MULTI_THREAD.getCode(), "true")));
        parameter.setIgnoreWriteError(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.ES_IGNORE_WRITE_ERROR.getCode(), "false")));
        parameter.setIgnoreParseError(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.ES_IGNORE_PARSE_ERROR.getCode(), "true")));
        parameter.setAlias(paramMap.get(DataxJobParamEnum.ES_ALIAS.getCode()));
        parameter.setAliasMode(paramMap.getOrDefault(DataxJobParamEnum.ES_ALIAS_MODE.getCode(), "append"));
        parameter.setSetting(paramMap.get(DataxJobParamEnum.ES_SETTINGS.getCode()));
        parameter.setSplitter(paramMap.get(DataxJobParamEnum.ES_SPLITTER.getCode()));
        parameter.setDynamic(Boolean.valueOf(paramMap.getOrDefault(DataxJobParamEnum.ES_DYNAMIC.getCode(), "false")));

        List<MetadataTableColumn> columnList = metadata.getColumnList();
        Map<String, MetadataTableColumn> columnMap = columnList.stream().collect(Collectors.toMap(MetadataTableColumn::getColumnName, MetadataTableColumn -> MetadataTableColumn));

        String writerColumn = paramMap.get(DataxJobParamEnum.WRITER_COLUMN.getCode());
        String[] columns = writerColumn.split(",");
        List<EsColumn> esColumns = Lists.newArrayList();

        for (String column : columns) {
            EsColumn esColumn = new EsColumn();
            esColumn.setName(column);
            MetadataTableColumn c = columnMap.get(column);
            esColumn.setType(c.getColumnType());
            if ("date".equals(c.getColumnType())) {
                esColumn.setFormat(c.getColumnFormat());
            }
            esColumns.add(esColumn);
        }
        parameter.setColumn(esColumns);
        return parameter;
    }
}
