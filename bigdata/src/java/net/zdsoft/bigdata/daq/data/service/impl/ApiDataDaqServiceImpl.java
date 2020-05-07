package net.zdsoft.bigdata.daq.data.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.bigdata.daq.data.service.ApiDataDaqService;
import net.zdsoft.bigdata.frame.data.kafka.KafkaProduceService;
import net.zdsoft.bigdata.frame.data.redis.BgRedisUtils;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("apiDataDaqService")
public class ApiDataDaqServiceImpl implements ApiDataDaqService {

    @Autowired
    private KafkaProduceService kafkaProduceService;

    @Override
    public void saveApiData() {
        Jedis jedis = RedisUtils.getJedis();
        List<String> apiDatas = new ArrayList<>();
        try {
            int max = 500;
            for (int i = 0; i < max; i++) {
                try {
                    String s = BgRedisUtils.lpop("api.stat.data.by.minute");
                    if (StringUtils.isBlank(s)) {
                        if (i == 0) {
                            ApiInterfaceCount tsApiCount = new ApiInterfaceCount();
                            tsApiCount.setCreationTime(DateUtils.addMinute(new Date(), -1));
                            tsApiCount.setCount(0);
                            tsApiCount.setTime(0);
                            tsApiCount.setTimes(0);
                            tsApiCount.setSequenceId(1);
                            tsApiCount.setDataStatus("推送");
                            String tsEmptyData = JSON.toJSONString(tsApiCount);
                            apiDatas.add(tsEmptyData);

                            ApiInterfaceCount lqApiCount = new ApiInterfaceCount();
                            lqApiCount.setCreationTime(DateUtils.addMinute(new Date(), -1));
                            lqApiCount.setCount(0);
                            lqApiCount.setTime(0);
                            lqApiCount.setTimes(0);
                            lqApiCount.setSequenceId(1);
                            lqApiCount.setDataStatus("拉取");
                            String lqEmptyData = JSON.toJSONString(lqApiCount);
                            apiDatas.add(lqEmptyData);
                        }
                        break;
                    }
                    apiDatas.add(s);
                } catch (Exception e) {
                    break;
                }
            }
        } finally {
            RedisUtils.returnResource(jedis);
        }
        if (CollectionUtils.isNotEmpty(apiDatas)) {
            for (String data : apiDatas) {
                kafkaProduceService.submitData2Kafka("topic_api_monitor", data);
            }
        }

    }
}
