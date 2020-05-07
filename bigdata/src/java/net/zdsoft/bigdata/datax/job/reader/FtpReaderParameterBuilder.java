package net.zdsoft.bigdata.datax.job.reader;

import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.ftp.FtpJobContentReaderParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonReaderParameterBuilder;

import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:16.
 */
public class FtpReaderParameterBuilder extends CommonReaderParameterBuilder {


    @Override
    public JobContentParameter getJobContentReaderParameter(Map<String, String> paramMap, AbstractDatabase database) throws BigDataBusinessException {

        FtpJobContentReaderParameter parameter = new FtpJobContentReaderParameter();
        parameter.setHost(paramMap.get(DataxJobParamEnum.FTP_HOST.getCode()));
        parameter.setProtocol(paramMap.get(DataxJobParamEnum.FTP_PROTOCOL.getCode()));
        parameter.setUsername(paramMap.get(DataxJobParamEnum.FTP_USERNAME.getCode()));
        parameter.setPassword(paramMap.get(DataxJobParamEnum.FTP_PASSWORD.getCode()));
        parameter.setPath(Lists.newArrayList(paramMap.get(DataxJobParamEnum.FTP_PATH.getCode())));
        parameter.setPort(Integer.valueOf(paramMap.get(DataxJobParamEnum.FTP_PORT.getCode())));
        parameter.setCompress(paramMap.get(DataxJobParamEnum.FTP_COMPRESS.getCode()));
        parameter.setEncoding("UTF-8");
        parameter.setFieldDelimiter(paramMap.get(DataxJobParamEnum.FIELD_DELIMITER.getCode()));
        parameter.setColumn(this.getJobContentColumns(paramMap));
        return parameter;
    }
}
