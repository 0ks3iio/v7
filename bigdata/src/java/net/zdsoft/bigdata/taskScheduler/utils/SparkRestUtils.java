package net.zdsoft.bigdata.taskScheduler.utils;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.data.entity.SparkRestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by wangdongdong on 2018/11/1 10:27.
 */
public class SparkRestUtils {

    private static RestTemplate restTemplate = null;

    public static final String SUBMITTED = "SUBMITTED";
    public static final String RUNNING = "RUNNING";
    public static final String FAILED = "FAILED";
    public static final String KILLED = "KILLED";
    public static final String FINISHED = "FINISHED";


    private static final String SPARK_URL = "spark_url";
    private static final String SPARK_VERSION = "spark_version";
    private static final String SPARK_ENVENTLOG_ENABLED = "spark_enventLog_enabled";
    private static final String SPARK_EVENTLOG_DIR = "spark_eventLog_dir";
    private static final String SPARK_EXECUTOR_CORES = "spark_executor_cores";
    private static final String SPARK_CORES_MAX = "spark_cores_max";
    private static final String SPARK_MASTER = "spark_master";

    private static final String HDFS_URL = "hdfs_url";


    private static void init() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
    }

    /**
     * 提交spark任务
     * @param sparkJob
     * @return
     */
    public static SparkRestResponse submit(EtlJob sparkJob, OptionDto sparkDto, String hdfsUrl) {
        init();
        String jarFileName = sparkJob.getPath().substring(sparkJob.getPath().lastIndexOf("/") + 1);
        String appResource = hdfsUrl + "/spark/job/" + sparkJob.getId() + "/" + jarFileName;
        String mainClass = sparkJob.getFileName();
        String[] args = {hdfsUrl + "/spark/job/" + sparkJob.getId() + "/" + sparkJob.getBusinessFile()};  //spark程序需要的参数

        JSONObject sparkParam = new JSONObject();
        sparkParam.put("action", "CreateSubmissionRequest");
        sparkParam.put("appArgs", args);
        sparkParam.put("appResource", appResource);
        Map<String, String> frameParamMap = sparkDto.getFrameParamMap();
        sparkParam.put("clientSparkVersion", frameParamMap.getOrDefault(SPARK_VERSION, "2.3.1"));

        JSONObject environmentVariables = new JSONObject();
        environmentVariables.put("SPARK_ENV_LOADED", "1");
        sparkParam.put("environmentVariables", environmentVariables);
        sparkParam.put("mainClass", mainClass);

        JSONObject sparkProp = new JSONObject();
        sparkProp.put("spark.jars", appResource);
        sparkProp.put("spark.driver.supervise", "false");
        sparkProp.put("spark.app.name", sparkJob.getName());
        sparkProp.put("spark.eventLog.enabled", frameParamMap.getOrDefault(SPARK_ENVENTLOG_ENABLED, "true"));
        sparkProp.put("spark.eventLog.dir", frameParamMap.getOrDefault(SPARK_EVENTLOG_DIR, "/spark/log"));
        sparkProp.put("spark.submit.deployMode", "cluster");
        sparkProp.put("spark.executor.cores", frameParamMap.getOrDefault(SPARK_EXECUTOR_CORES, "1"));
        sparkProp.put("spark.cores.max", frameParamMap.getOrDefault(SPARK_CORES_MAX, "2"));
        sparkProp.put("spark.master", frameParamMap.get(SPARK_MASTER));

        sparkParam.put("sparkProperties", sparkProp);
        return post("http://" + frameParamMap.get(SPARK_URL) + ":6066/v1/submissions/create", SparkRestResponse.class,null,null,sparkParam.toJSONString());
    }

    /**
     * 结束已提交的任务
     * @param submissionId
     * @return
     */
    public static SparkRestResponse killJob(String submissionId, OptionDto sparkParam) {
        return SparkRestUtils.post("http://" + sparkParam.getFrameParamMap().get(SPARK_URL) + ":6066/v1/submissions/kill/" + submissionId, SparkRestResponse.class,null,null, null);
    }

    /**
     * 获取任务执行状态
     * @param submissionId
     * @return
     */
    public static SparkRestResponse monitorJob(String submissionId, OptionDto sparkParam) {
        if (StringUtils.isBlank(submissionId)) {
            return SparkRestResponse.error();
        }
        return SparkRestUtils.get("http://" + sparkParam.getFrameParamMap().get(SPARK_URL) + ":6066/v1/submissions/status/" + submissionId, SparkRestResponse.class);
    }


    /**
     * post请求,包含了路径,返回类型,Header,Parameter
     *
     * @param url:地址
     * @param returnClassName:返回对象类型,如:String.class
     * @param inputHeader
     * @param inputParameter
     * @param jsonBody
     * @return
     */
    public static  <T> T post(String url, Class<T> returnClassName, Map<String,Object> inputHeader, Map<String,Object> inputParameter, String jsonBody){
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        //请求Header
        HttpHeaders httpHeaders = new HttpHeaders();
        //拼接Header
        if (inputHeader != null) {
            Set<String> keys = inputHeader.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = i.next();
                httpHeaders.add(key, inputHeader.get(key).toString());
            }
        }
        //设置请求的类型及编码
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(type);
        httpHeaders.add("Accept", "application/json");
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.ALL);
        httpHeaders.setAccept(acceptableMediaTypes);

        HttpEntity<String> formEntity = new HttpEntity<String>(jsonBody, httpHeaders);
        if (inputParameter==null) {
            return restTemplate.postForObject(url, formEntity, returnClassName);
        }
        return restTemplate.postForObject(url, formEntity, returnClassName, inputParameter);
    }

    public static <T> T get(String url, Class<T> returnClassName){
        init();
        return restTemplate.getForObject(url, returnClassName);
    }

}
