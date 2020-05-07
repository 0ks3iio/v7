package net.zdsoft.bigdata.datax.entity.kafka;

import net.zdsoft.bigdata.datax.entity.JobContentWriterParameter;

/**
 * Created by wangdongdong on 2019/6/4 16:24.
 */
public class KafkaJobContentWriterParameter extends JobContentWriterParameter {


    private String topic;

    private String bootstrapServers;

    private String keys;

    private boolean notTopicCreate;

    private Integer topicNumPartition;

    private Integer topicReplicationFactor;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public boolean isNotTopicCreate() {
        return notTopicCreate;
    }

    public void setNotTopicCreate(boolean notTopicCreate) {
        this.notTopicCreate = notTopicCreate;
    }

    public Integer getTopicNumPartition() {
        return topicNumPartition;
    }

    public void setTopicNumPartition(Integer topicNumPartition) {
        this.topicNumPartition = topicNumPartition;
    }

    public Integer getTopicReplicationFactor() {
        return topicReplicationFactor;
    }

    public void setTopicReplicationFactor(Integer topicReplicationFactor) {
        this.topicReplicationFactor = topicReplicationFactor;
    }
}
