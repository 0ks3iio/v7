package net.zdsoft.bigdata.datax.job.writer;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.MetadataTransfer;
import net.zdsoft.bigdata.datax.entity.hbase.HbaseConfig;
import net.zdsoft.bigdata.datax.entity.hbase.HbasePrimaryJobContentWriterParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonWriterParameterBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:35.
 */
public class HbaseWriterParameterBuilder extends CommonWriterParameterBuilder {


    @Override
    public JobContentParameter getJobContentWriterParameter(Map<String, String> paramMap, MetadataTransfer metadata) throws BigDataBusinessException {
        Map<String, String> hbaseParamMap = getFrameParamMap();
        String zkUrl = getZkUrl(hbaseParamMap.get("domain"), hbaseParamMap.get("port"));
        HbasePrimaryJobContentWriterParameter primaryJobContentWriterParameter = new HbasePrimaryJobContentWriterParameter();
        primaryJobContentWriterParameter.setHbaseConfig(new HbaseConfig().setZkUrl(zkUrl));
        primaryJobContentWriterParameter.setNullMode(paramMap.get(DataxJobParamEnum.NULL_MODE.getCode()));
        primaryJobContentWriterParameter.setTable(metadata.getTableName());

        String writerColumn = paramMap.get(DataxJobParamEnum.WRITER_COLUMN.getCode());
        String[] columns = writerColumn.split(",");
        List<String> column = Lists.newArrayList(columns);
        primaryJobContentWriterParameter.setColumn(column);
        return primaryJobContentWriterParameter;
    }

    protected String getZkUrl(String domain, String port) throws BigDataBusinessException {
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
