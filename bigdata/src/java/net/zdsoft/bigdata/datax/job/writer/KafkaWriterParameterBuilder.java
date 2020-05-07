package net.zdsoft.bigdata.datax.job.writer;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.JobContentParameter;
import net.zdsoft.bigdata.datax.entity.MetadataTransfer;
import net.zdsoft.bigdata.datax.entity.kafka.KafkaJobContentWriterParameter;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.job.CommonWriterParameterBuilder;

import java.util.Map;

/**
 * Created by wangdongdong on 2019/6/17 18:43.
 */
public class KafkaWriterParameterBuilder extends CommonWriterParameterBuilder {


    @Override
    public JobContentParameter getJobContentWriterParameter(Map<String, String> paramMap, MetadataTransfer metadata) throws BigDataBusinessException {
        KafkaJobContentWriterParameter wParameter = new KafkaJobContentWriterParameter();
        wParameter.setTopic(paramMap.get(DataxJobParamEnum.KAFKA_TOPIC.getCode()));
        wParameter.setBootstrapServers(paramMap.get(DataxJobParamEnum.KAFKA_SERVERS.getCode()));
        wParameter.setNotTopicCreate(!Boolean.valueOf(paramMap.get(DataxJobParamEnum.KAFKA_CREATETOPIC.getCode())));
        wParameter.setTopicNumPartition(Integer.valueOf(paramMap.get(DataxJobParamEnum.KAFKA_PARTITION.getCode())));
        wParameter.setTopicReplicationFactor(Integer.valueOf(paramMap.get(DataxJobParamEnum.KAFKA_REPLICATION_FACTOR.getCode())));
        wParameter.setKeys(paramMap.get(DataxJobParamEnum.WRITER_COLUMN.getCode()));
        return wParameter;
    }
}
