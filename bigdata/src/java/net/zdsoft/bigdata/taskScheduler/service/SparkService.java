package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.data.entity.SparkRestResponse;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * spark任务调度
 * Created by wangdongdong on 2018/10/25 16:31.
 */
public interface SparkService {


    void saveSparkJob(EtlJob etlJob) throws IOException, URISyntaxException;

    SparkRestResponse dealSparkJob(String id);

    SparkRestResponse deleteSparkJob(String id);

    SparkRestResponse monitorSparkJob(String id);

    SparkRestResponse stopSparkJob(String id);
}
