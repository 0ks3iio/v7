package net.zdsoft.bigdata.frame.data.kafka;

import java.util.List;

public interface KafkaProduceService {

    /**
     * 提交数据至kafka
     * @param topicId 主题id
     * @param jsonData json格式的数据
     */
    public void submitData2Kafka(String topicId,String jsonData);


    /**
     * 提交数据至kafka
     * @param topicId 主题id
     * @param jsonDatas json格式的数据
     */
    public void submitDatas2Kafka(String topicId, List<String> jsonDatas);
}
