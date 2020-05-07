package net.zdsoft.bigdata.datax.job.reader;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.*;
import net.zdsoft.bigdata.datax.entity.hbase.HbaseColumn;
import net.zdsoft.bigdata.datax.entity.hbase.HbaseConfig;
import net.zdsoft.bigdata.datax.entity.hbase.HbaseJobContentReaderParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonReaderParameterBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:03.
 */
public class HbaseReaderParameterBuilder extends CommonReaderParameterBuilder {


    @Override
    public JobContentParameter getJobContentReaderParameter(Map<String, String> paramMap, AbstractDatabase database) throws BigDataBusinessException {
        return getHbaseJobParameter(paramMap, (NosqlDatabase) database);
    }

    protected HbaseJobContentReaderParameter getHbaseJobParameter(Map<String, String> paramMap, NosqlDatabase database) throws BigDataBusinessException {
        NosqlDatabase source = database;
        HbaseJobContentReaderParameter parameter = new HbaseJobContentReaderParameter();

        String zkUrl = getZkUrl(source.getDomain(), String.valueOf(source.getPort()));
        parameter.setHbaseConfig(new HbaseConfig().setZkUrl(zkUrl));
        parameter.setMode(paramMap.get(DataxJobParamEnum.HBASE_READ_MODE.getCode()));
        if ("multiVersionFixedColumn".equals(parameter.getMode())) {
            String maxVersion = paramMap.get(DataxJobParamEnum.HBASE_MAXVERSION.getCode());
            parameter.setMaxVersion(StringUtils.isNotBlank(maxVersion) ? maxVersion : "-1");
        }

        parameter.setTable(paramMap.get(DataxJobParamEnum.HBASE_TABLE.getCode()));

        List<HbaseColumn> column = Lists.newArrayList();
        String readerColumn = paramMap.get(DataxJobParamEnum.READER_COLUMN.getCode());
        String[] columns = readerColumn.split(",");
        int i =0;
        for (String c : columns) {
            HbaseColumn hc = new HbaseColumn();
            hc.setIndex(i++);
            hc.setName(c);
            hc.setType("string");
            column.add(hc);
        }
        parameter.setColumn(column);
        return parameter;
    }

    private String getZkUrl(String domain, String port) throws BigDataBusinessException {
        // master:2181,slave01:2181,slave02
        String[] hosts = domain.split(",");
        if (hosts.length < 1) {
            throw new BigDataBusinessException("hbase域名配置有误!");
        }
        StringBuilder sb = new StringBuilder();
        for (String host : hosts) {
            sb.append(host).append(":").append(port).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
