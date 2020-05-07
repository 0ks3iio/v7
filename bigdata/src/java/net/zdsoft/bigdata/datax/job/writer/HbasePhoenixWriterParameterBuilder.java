package net.zdsoft.bigdata.datax.job.writer;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.MetadataTransfer;
import net.zdsoft.bigdata.datax.entity.hbase.*;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:38.
 */
public class HbasePhoenixWriterParameterBuilder extends HbaseWriterParameterBuilder {


    @Override
    public JobContentParameter getJobContentWriterParameter(Map<String, String> paramMap, MetadataTransfer metadata) throws BigDataBusinessException {

        Map<String, String> hbaseParamMap = getFrameParamMap();
        String zkUrl = getZkUrl(hbaseParamMap.get("domain"), hbaseParamMap.get("port"));

        HbasePhoenixJobContentWriterParameter phoenixJobContentWriterParameter = new HbasePhoenixJobContentWriterParameter();
        phoenixJobContentWriterParameter.setEncoding("UTF-8");

        phoenixJobContentWriterParameter.setHbaseConfig(new HbaseConfig().setZkUrl(zkUrl));
        phoenixJobContentWriterParameter.setMode("normal");
        phoenixJobContentWriterParameter.setNullMode(paramMap.get(DataxJobParamEnum.NULL_MODE.getCode()));
        phoenixJobContentWriterParameter.setWalFlag(Boolean.valueOf(paramMap.get(DataxJobParamEnum.WAL_FLAG.getCode())));
        phoenixJobContentWriterParameter.setTable(metadata.getTableName());
        phoenixJobContentWriterParameter.setVersionColumn(new HbaseVersionColumn().setIndex(-1).setValue("123456789"));

        List<HbaseColumn> column = Lists.newArrayList();
        String writerColumn = paramMap.get(DataxJobParamEnum.WRITER_COLUMN.getCode());
        String[] columns = writerColumn.split(",");
        int i =0;
        for (String c : columns) {
            HbaseColumn hc = new HbaseColumn();
            hc.setIndex(i++);
            hc.setName(c);
            hc.setType("string");
            column.add(hc);
        }
        phoenixJobContentWriterParameter.setColumn(column);

        List<HbaseRowkeyColumn> rowkeyColumn = Lists.newArrayList(new HbaseRowkeyColumn().setIndex(0).setType("string"));
        phoenixJobContentWriterParameter.setRowkeyColumn(rowkeyColumn);
        return phoenixJobContentWriterParameter;
    }
}
