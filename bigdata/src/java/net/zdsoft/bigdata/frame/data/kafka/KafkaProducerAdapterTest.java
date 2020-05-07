package net.zdsoft.bigdata.frame.data.kafka;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

public class KafkaProducerAdapterTest {
    private static final Logger logger = LogManager
            .getLogger(KafkaProducerAdapterTest.class);

    private static KafkaProducer<String, String> kafkaProducer = null;

    private static KafkaProducerAdapterTest kafkaProducerAdapter = null;

    private KafkaProducerAdapterTest() {
    }

    /**
     * u
     * 单例
     */
    public static KafkaProducerAdapterTest getInstance() throws Exception {
        if (kafkaProducerAdapter == null) {
            synchronized (KafkaProducerAdapterTest.class) {
                kafkaProducerAdapter = new KafkaProducerAdapterTest();
                try {
                    kafkaProducer = new KafkaProducer<>(
                            initProperties());
                } catch (Exception e) {
                    throw new Exception("init kafka producer exception:"
                            + e.getMessage());
                }
            }
        }
        return kafkaProducerAdapter;
    }

    private static Properties initProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "172.16.10.113:9092,172.16.10.115:9092,172.16.10.116:9092");// 服务器1
        props.put("group.id", "test");
        props.put("acks", "all");// 所有follower都响应了才认为消息提交成功，即"committed"
        props.put("retries", 0);// retries = MAX
        // 无限重试，直到你意识到出现了问题:)
        // props.put("max.block.ms", "10");
        props.put("batch.size", 1);// 16M 1024
        // producer将试图批处理消息记录，以减少请求次数.默认的批量处理消息字节数
        // batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
        props.put("linger.ms", 2);// 延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
        props.put("buffer.memory", 33554432);// producer可以用来缓存数据的内存大小。
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    /**
     * 发送消息
     */
    public void send(String topic, String message) {
        ProducerRecord<String, String> record;
        record = new ProducerRecord<>(topic, message);
        kafkaProducer.send(record, new SendCallback(record, 0));
    }

    /**
     * producer回调
     */
    static class SendCallback implements Callback {
        ProducerRecord<String, String> record;
        int sendSeq;

        public SendCallback(ProducerRecord<String, String> record, int sendSeq) {
            this.record = record;
            this.sendSeq = sendSeq;
        }

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            // send success
            if (null == e) {
                String meta = "topic:" + recordMetadata.topic()
                        + ", partition:" + recordMetadata.topic() + ", offset:"
                        + recordMetadata.offset();
                logger.error("send message success, record:"
                        + record.toString() + ", meta:" + meta);
                return;
            }
            // send failed
            logger.error("send message failed, seq:" + sendSeq + ", record:"
                    + record.toString() + ", errmsg:" + e.getMessage());
            e.printStackTrace();
            if (sendSeq < 1) {
                kafkaProducer.send(record, new SendCallback(record, ++sendSeq));
            }
        }
    }

    private static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }


    private static String getRandomByLength(int len) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < len; i++) result += random.nextInt(10);
        System.out.println(result);
        return result;

    }

    private static String getSex(int index) {
        Map<Integer, String> sexMap = new HashMap<>();
        sexMap.put(1, "男");
        sexMap.put(2, "女");
        return sexMap.get(index);
    }

    private static Integer getAge(int index) {
        Map<Integer, Integer> ageMap = new HashMap<>();
        ageMap.put(1, 6);
        ageMap.put(2, 7);
        ageMap.put(3, 8);
        ageMap.put(4, 9);
        ageMap.put(5, 10);
        ageMap.put(6, 11);
        ageMap.put(7, 21);
        ageMap.put(8, 13);
        ageMap.put(9, 14);
        ageMap.put(10, 15);
        ageMap.put(11, 16);
        ageMap.put(12, 17);
        ageMap.put(13, 18);
        return ageMap.get(index);
    }


    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            Json result = new Json();
//            result.put("id", UuidUtils.generateUuid());
//            result.put("name", "测试学生" + getRandom(1, 500));
//            result.put("sex", getSex(getRandom(1, 2)));
//            result.put("age", getAge(getRandom(1, 13)).toString());
//            result.put("identitycard", getRandomByLength(18));


            result.put("ID", UuidUtils.generateUuid());
//            result.put("ID", UuidUtils.generateUuid());
//            result.put("STUDENT_NAME", "测试学生" + getRandom(1, 3));
//            result.put("GRADE_NAME", "1-年级" + getRandom(1, 500));
//            result.put("CLASS_NAME", "1-班级" + getRandom(1, 500));
            result.put("dataType", getRandom(1, 4));
            result.put("isWarn", 1);
//            result.put("AGE", "1"+getAge(getRandom(1, 13)).toString());
//            result.put("SECTION", "1"+getSex(getRandom(1, 2)));

//            DateTimeFormatter dtFormatter = DateTimeFormat
//                    .forPattern("yyyy-MM-dd HH:mm:ss");
//            String timestamp = dtFormatter.parseDateTime(
//                    DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"))
//                    .toString();
            result.put("creationTime", new Date().getTime());
            // {"creationTime":1560507054667,"dataType":1,"count":65,"message":"调用接口的响应时间为：199time","type":"xhlx","ticketKey":"2c9082e76b456cb1016b457421a103b5","appIds":"2c9082e76b494738016b49cb145608cf,2c9082e76b456cb1016b457bfd9204ec","pushUrl":"/openapi/v3.0/xh","isWarn":0,"id":"402880106b555af0016b5577fa4b01be","interfaceId":"2c9082e76b456cb1016b45793a900481","time":199,"resultType":"xhjg"}
            try {
                KafkaProducerAdapterTest producer = KafkaProducerAdapterTest
                        .getInstance();
                producer.send("test123456", result.toString());
                System.out.println(result.toString());
                Thread.sleep(getRandom(50, 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}