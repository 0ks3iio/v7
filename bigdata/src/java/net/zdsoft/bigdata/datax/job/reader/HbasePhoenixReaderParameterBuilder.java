package net.zdsoft.bigdata.datax.job.reader;

import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;

import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:08.
 */
public class HbasePhoenixReaderParameterBuilder extends HbaseReaderParameterBuilder {


    @Override
    public JobContentParameter getJobContentReaderParameter(Map<String, String> paramMap, AbstractDatabase database) throws BigDataBusinessException {
        return getHbaseJobParameter(paramMap, (NosqlDatabase) database).setTable(paramMap.get(DataxJobParamEnum.PHOENIX_TABLE.getCode()));
    }
}
