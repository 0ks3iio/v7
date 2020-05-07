package net.zdsoft.bigdata.datax.job.reader;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.txtfile.TxtFilleJobContentReaderParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonReaderParameterBuilder;

import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:16.
 */
public class TextFileReaderParameterBuilder extends CommonReaderParameterBuilder {


    @Override
    public JobContentParameter getJobContentReaderParameter(Map<String, String> paramMap, AbstractDatabase database) throws BigDataBusinessException {

        TxtFilleJobContentReaderParameter parameter = new TxtFilleJobContentReaderParameter();
        parameter.setPath(Lists.newArrayList(paramMap.get(DataxJobParamEnum.TEXT_FILE_PATH.getCode())));
        parameter.setEncoding("UTF-8");
        parameter.setFieldDelimiter(paramMap.get(DataxJobParamEnum.FIELD_DELIMITER.getCode()));
        parameter.setCompress(paramMap.get(DataxJobParamEnum.TEXT_FILE_COMPRESS.getCode()));
        parameter.setColumn(this.getJobContentColumns(paramMap));
        return parameter;
    }

}
