package net.zdsoft.bigdata.frame.data.kafka;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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

public class KafkaProducerAdapterTest1 {
    private static final Logger logger = LogManager
            .getLogger(KafkaProducerAdapterTest1.class);

    private static KafkaProducer<String, String> kafkaProducer = null;

    private static KafkaProducerAdapterTest1 kafkaProducerAdapter = null;

    private KafkaProducerAdapterTest1() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static KafkaProducerAdapterTest1 getInstance() throws Exception {
        if (kafkaProducerAdapter == null) {
            synchronized (KafkaProducerAdapterTest1.class) {
                kafkaProducerAdapter = new KafkaProducerAdapterTest1();
                try {
                    kafkaProducer = new KafkaProducer<String, String>(
                            initProperties("172.16.10.113:9092,172.16.10.115:9092,172.16.10.116:9092"));
                } catch (Exception e) {
                    throw new Exception("init kafka producer exception:"
                            + e.getMessage());
                }
            }
        }
        return kafkaProducerAdapter;
    }

    private static Properties initProperties(String kafkaUrl) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaUrl);// 服务器
        props.put("group.id", "kle");
        props.put("acks", "all");// 所有follower都响应了才认为消息提交成功，即"committed"
        props.put("retries", 0);// retries = MAX
        // 无限重试，直到你意识到出现了问题:)
        // props.put("max.block.ms", "10");
        props.put("batch.size", 1);// 16M 1024
        // producer将试图批处理消息记录，以减少请求次数.默认的批量处理消息字节数
        // batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
        props.put("linger.ms", 1);// 延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
        props.put("buffer.memory", 33554432);// producer可以用来缓存数据的内存大小。
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void send(String topic, String message) {
        ProducerRecord<String, String> record;
        record = new ProducerRecord<String, String>(topic, message);
        kafkaProducer.send(record, new SendCallback(record, 0));
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void syncSend(String topic, String message) {
        ProducerRecord<String, String> record;
        record = new ProducerRecord<String, String>(topic, message);
        kafkaProducer.send(record);
    }

    /**
     * producer回调
     */
    static class SendCallback implements Callback {
        ProducerRecord<String, String> record;
        int sendSeq = 0;

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

    private static String getNation(int index) {
        Map<Integer, String> familyMap = new HashMap<Integer, String>();
        familyMap.put(1, "汉族");
        familyMap.put(2, "维吾尔族");
        familyMap.put(3, "回族");
        familyMap.put(4, "朝鲜族");
        familyMap.put(5, "蒙古族");
        familyMap.put(6, "哈萨克族");
        familyMap.put(7, "苗族");

        return familyMap.get(index);
    }

    private static String getSex(int index) {
        Map<Integer, String> sexMap = new HashMap<Integer, String>();
        sexMap.put(1, "男");
        sexMap.put(2, "女");
        return sexMap.get(index);
    }

    private static String getBackground(int index) {
        Map<Integer, String> interestsMap = new HashMap<Integer, String>();
        interestsMap.put(1, "中共党员");
        interestsMap.put(2, "群众");
        interestsMap.put(3, "其他");
        return interestsMap.get(index);
    }

    private static String getCountry(int index) {
        Map<Integer, String> familyMap = new HashMap<Integer, String>();
        familyMap.put(1, "中国");
        familyMap.put(2, "美国");
        familyMap.put(3, "英国");
        familyMap.put(4, "韩国");
        familyMap.put(5, "俄罗斯");

        return familyMap.get(index);
    }

    private static String getRegion(int index) {
        Map<Integer, String> regionMap = new HashMap<Integer, String>();
        regionMap.put(1, "浙江市");
        regionMap.put(2, "上海市");
        regionMap.put(3, "江苏省");
        regionMap.put(4, "山东省");
        regionMap.put(5, "安徽省");
        regionMap.put(6, "北京市");
        regionMap.put(7, "天津市");
        regionMap.put(8, "重庆市");
        regionMap.put(9, "辽宁市");
        regionMap.put(10, "吉林省");
        regionMap.put(11, "陕西省");
        regionMap.put(12, "河北省");
        regionMap.put(13, "广东省");
        regionMap.put(14, "广西省");
        regionMap.put(15, "河南省");
        regionMap.put(16, "黑龙江省");
        regionMap.put(17, "吉林省");
        regionMap.put(18, "甘肃省");
        regionMap.put(19, "江西省");
        regionMap.put(20, "青海省");
        regionMap.put(21, "云南省");
        regionMap.put(22, "贵州省");
        regionMap.put(23, "新疆维吾尔自治区");
        regionMap.put(24, "广西壮族自治区");
        regionMap.put(25, "湖南省");
        regionMap.put(26, "湖北省");
        regionMap.put(27, "海南省");
        regionMap.put(28, "宁夏回族自治区");
        regionMap.put(29, "山西省");
        regionMap.put(30, "河北省");
        regionMap.put(31, "内蒙古自治区");
        return regionMap.get(index);
    }

    private static String getTitle(int index) {
        Map<Integer, String> titleMap = new HashMap<Integer, String>();
        titleMap.put(1, "高级教师");
        titleMap.put(2, "一级教师");
        titleMap.put(3, "二级教师");
        titleMap.put(4, "三级教师");
        return titleMap.get(index);
    }

    private static String getEducation(int index) {
        Map<Integer, String> educationMap = new HashMap<Integer, String>();
        educationMap.put(1, "大专");
        educationMap.put(2, "本科");
        educationMap.put(3, "硕士");
        educationMap.put(4, "博士");
        educationMap.put(5, "其他");
        return educationMap.get(index);
    }

    private static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 10000; i++) {
            Json result = new Json();
            result.put("teacher_id", UuidUtils.generateUuid());
            result.put("teacher_name", "教师" + getRandom(1, 500));
            result.put("sex", getSex(getRandom(1, 2)));
            result.put("nation", getNation(getRandom(1, 13)));
            result.put("title", getTitle(getRandom(1, 4)));
            result.put("education", getEducation(getRandom(1, 5)));
            result.put("region", getEducation(getRandom(1, 31)));
            result.put("background", getBackground(getRandom(1, 3)));

            DateTimeFormatter dtFormatter = DateTimeFormat
                    .forPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = dtFormatter.parseDateTime(
                    DateUtils.date2String(DateUtils.addDay(new Date(), (0 - getRandom(1, 10))), "yyyy-MM-dd HH:mm:ss"))
                    .toString();
            result.put("timestamp", timestamp);
            try {
                KafkaProducerAdapterTest1 producer = KafkaProducerAdapterTest1
                        .getInstance();
                producer.send("teacher", result.toString());
                System.out.println(result.toString());
                Thread.sleep(getRandom(50, 100));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main1(String[] args) {
        System.out.print(DateUtils.date2String(new Date(), "yy-MM-dd"));
    }
}