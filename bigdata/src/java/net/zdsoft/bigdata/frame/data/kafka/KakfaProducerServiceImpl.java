package net.zdsoft.bigdata.frame.data.kafka;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.framework.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service("kafkaProduceService")
public class KakfaProducerServiceImpl implements KafkaProduceService {
    private static final Logger logger = LoggerFactory
            .getLogger(KakfaProducerServiceImpl.class);

    private static final String TOPIC_API_2_KAFKA = "topic_api_monitor";

    public void submitData2Kafka(String topicId, String jsonData) {
        try {
            if (TOPIC_API_2_KAFKA.equals(topicId)) {
                JSONObject result = JSONObject.parseObject(jsonData);
                Long creationTime = result.getLongValue("creationTime");
                String utcDate = localLongDate2Utc(creationTime, null);
                result.put("timestamp", utcDate);
                jsonData = result.toJSONString();
            }
            KafkaProducerAdapter producer = KafkaProducerAdapter
                    .getInstance();
            producer.send(topicId, jsonData);
        } catch (Exception e) {
            logger.error("kafka发送消息失败:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void submitDatas2Kafka(String topicId, List<String> jsonDatas) {
        try {
            KafkaProducerAdapter producer = KafkaProducerAdapter
                    .getInstance();
            for (String jsonData : jsonDatas) {
                if (TOPIC_API_2_KAFKA.equals(topicId)) {
                    JSONObject result = JSONObject.parseObject(jsonData);
                    Long creationTime = result.getLongValue("creationTime");
                    String utcDate = localLongDate2Utc(creationTime, null);
                    result.put("timestamp", utcDate);
                    jsonData = result.toJSONString();
                }

                producer.send(topicId, jsonData);
            }
        } catch (Exception e) {
            logger.error("kafka发送消息失败:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String localStrDate2Utc(String localStrDate, String dtFormat) {
        Date localDate = DateUtils
                .string2Date(localStrDate,
                        dtFormat);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // CST(北京时间)在东8区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println(sdf.format(localDate));
        return sdf.format(localDate);
    }

    private String localLongDate2Utc(Long localLongDate, String dtFormat) {
        Date localDate = new Date(localLongDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        // CST(北京时间)在东8区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println(sdf.format(localDate));
        return sdf.format(localDate);
    }

}
