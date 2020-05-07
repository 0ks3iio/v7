package net.zdsoft.bigdata.extend.data.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.data.entity.SparkRestResponse;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.utils.HdfsUtils;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.taskScheduler.service.FlinkJobService;
import net.zdsoft.bigdata.taskScheduler.service.SparkService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bigdata/calculate")
public class CalculateController extends BigdataBaseAction {

    @Autowired
    private KylinClientService kylinClientService;

    @Autowired
    private EtlJobService etlJobService;

    @Autowired
    private EtlJobLogService etlJobLogService;

    @Autowired
    private SparkService sparkService;

    @Autowired
    private FlinkJobService flinkJobService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private MetadataService metadataService;

    @Resource
    private BigLogService bigLogService;


    @RequestMapping("/offline/index")
    public String offline_index(ModelMap map) {
        map.put("calculateType", "2");
        return "/bigdata/calculate/offlineFrame.ftl";
        // return "/bigdata/calculate/offlineIndex.ftl";
    }

    @RequestMapping("realtime/index")
    public String realtime_index(ModelMap map) {
        OptionDto flinkDto = optionService.getAllOptionParam("flink");
        if (flinkDto == null || flinkDto.getStatus() == 0) {
            map.put("serverName", "Flink");
            map.put("serverCode", "Flink");
            return "/bigdata/noServer.ftl";
        }
        return "/bigdata/calculate/realtimeFrame.ftl";
    }

    @RequestMapping("realtime/overview")
    public String realtime_overview(ModelMap map){
        OptionDto flinkDto = optionService.getAllOptionParam("flink");
        if (flinkDto == null || flinkDto.getStatus() == 0) {
            map.put("serverName", "Flink");
            map.put("serverCode", "Flink");
            return "/bigdata/noServer.ftl";
        }
        String flinkUrl = flinkDto.getFrameParamMap().get("web_url");
        String result = "";
        String jobs = "";
        try {
            result = HttpClientUtils.getSync(flinkUrl + "/overview");
            jobs = HttpClientUtils.getSync(flinkUrl + "/jobs/overview");
        } catch (IOException e) {
            e.printStackTrace();
            return error("出错了:" + e.getMessage());
        }
        //处理overview数据
        JSONObject overview = JSON.parseObject(result);
        HashMap<String, Object> resultMap = new HashMap<>();
        //集群机器数
        resultMap.put("taskmanagers",overview.get("taskmanagers"));
        //总的计算节点数
        resultMap.put("slotsTotal",overview.get("slots-total"));
        //可用的计算节点数
        resultMap.put("slotsAvailable",overview.get("slots-available"));
        resultMap.put("jobsRunning",overview.get("jobs-running"));
        resultMap.put("jobsFinished",overview.get("jobs-finished"));
        resultMap.put("jobsCancelled",overview.get("jobs-cancelled"));
        resultMap.put("jobsFailed",overview.get("jobs-failed"));
        //处理jobs数据
        //获取正在执行的任务
        List<HashMap<String, Object>> runningjobs = resultJobs(jobs).get(0);
        //获取已完成的任务
        List<HashMap<String, Object>> completedjobs = resultJobs(jobs).get(1);
        map.put("overview",resultMap);
        map.put("completedjobs",completedjobs);
        map.put("runningjobs",runningjobs);
        return "/bigdata/calculate/flinkOverview.ftl";
    }

    @RequestMapping("realtime/jobs")
    public String completed_jobs(ModelMap map, @RequestParam("type") String type) {
        OptionDto flinkDto = optionService.getAllOptionParam("flink");
        if (flinkDto == null || flinkDto.getStatus() == 0) {
            map.put("serverName", "Flink");
            map.put("serverCode", "Flink");
            return "/bigdata/noServer.ftl";
        }
        String flinkUrl = flinkDto.getFrameParamMap().get("web_url");
        String result = "";
        try {
            result = HttpClientUtils.getSync(flinkUrl + "/jobs/overview");
        } catch (IOException e) {
            e.printStackTrace();
            return error("出错了:" + e.getMessage());
        }
        //获取正在执行的任务
        List<HashMap<String, Object>> runningjobs = resultJobs(result).get(0);
        //获取已完成的任务
        List<HashMap<String, Object>> completedjobs = resultJobs(result).get(1);
        if ("completed-jobs".equals(type)){
            map.put("jobs",completedjobs);
            return "/bigdata/calculate/completedJobList.ftl";
        }else {
            map.put("jobs",runningjobs);
            return "/bigdata/calculate/runningJobList.ftl";
        }
    }

    @RequestMapping("/cancel/jobs")
    @ResponseBody
    public Response cancel_jobs(@RequestParam("id") String id) {
        OptionDto flinkDto = optionService.getAllOptionParam("flink");
        String flinkUrl = flinkDto.getFrameParamMap().get("web_url");
        try {
            HttpClientUtils.getSync(flinkUrl + "/jobs/"+id+"/yarn-cancel");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok().message("操作成功").build();
    }

    private List<List<HashMap<String, Object>>> resultJobs(String result) {
        List<Json> jobs = JSONArray.parseArray(JSON.parseObject(result).getString("jobs"), Json.class);
//        return jobs.stream().map(x -> {
//            HashMap<String, Object> resultMap = new HashMap<>();
//            resultMap.put("startTime",x.get("start-time"));
//            resultMap.put("endTime",x.get("end-time"));
//            //持续时间
//            resultMap.put("duration",x.get("duration"));
//            resultMap.put("name",x.get("name"));
//            resultMap.put("jid",x.get("jid"));
//            resultMap.put("state",x.get("state"));
//            return resultMap;
//        }).collect(Collectors.toList());

        List<HashMap<String, Object>> running = new ArrayList<>();
        List<HashMap<String, Object>> completed = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jobs)){
            for (Json job : jobs) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("startTime",job.get("start-time"));
                map.put("endTime",job.get("end-time"));
                //持续时间
                String duration = formatDuring((Integer)job.get("duration"));
                map.put("duration",duration);
                map.put("name",job.get("name"));
                map.put("jid",job.get("jid"));
                map.put("state",job.get("state"));
                if("RUNNING".equals(job.get("state"))){
                    running.add(map);
                }else {
                    completed.add(map);
                }
            }
        }
        ArrayList<List<HashMap<String, Object>>> objects = new ArrayList<>();
        objects.add(running);
        objects.add(completed);
        return objects;
    }

    public static String formatDuring(long mss) {

        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (days>0){
            return days + "d " + hours + "h";
        }else if (hours>0){
            return hours + "h " + minutes + "m";
        }else if (minutes>0){
            return minutes + "m " + seconds + "s";
        }else {
            return seconds + "s";
        }
    }


    @ControllerInfo("进入realtime列表页面")
    @RequestMapping("/realtime/list")
    public String realtime_list(Integer calculateType,
                                HttpServletRequest request, ModelMap map) throws Exception {
        if (EtlType.SPARK.getValue() == calculateType) {
            OptionDto sparkDto = optionService.getAllOptionParam("spark");
            if (sparkDto == null || sparkDto.getStatus() == 0) {
                map.put("serverName", "Spark");
                map.put("serverCode", "spark");
                return "/bigdata/noServer.ftl";
            }
            OptionDto hdfsDto = optionService.getAllOptionParam("hdfs");
            if (hdfsDto == null || hdfsDto.getStatus() == 0) {
                map.put("serverName", "Hdfs");
                map.put("serverCode", "hdfs");
                return "/bigdata/noServer.ftl";
            }
            List<EtlJob> sparkJobs = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.SPARK.getValue());
            map.put("sparkJobs", sparkJobs);
            return "/bigdata/calculate/spark/sparkList.ftl";
        } else if (EtlType.JSTORM.getValue() == calculateType) {
            return "/bigdata/calculate/jstorm/jstormList.ftl";
        } else if (EtlType.FLINK_STREAM.getValue() == calculateType) {
            OptionDto flinkDto = optionService.getAllOptionParam("flink");
            if (flinkDto == null || flinkDto.getStatus() == 0) {
                map.put("serverName", "Flink");
                map.put("serverCode", "Flink");
                return "/bigdata/noServer.ftl";
            }
            String flinkUrl = flinkDto.getFrameParamMap().get("web_url");
            String result = "";
            try {
                result = HttpClientUtils.getSync(flinkUrl + "/jobs/overview");
            } catch (IOException e) {
                e.printStackTrace();
                return error("出错了"+e.getMessage());
            }

            List<Json> jobs = JSONArray.parseArray(JSON.parseObject(result).getString("jobs"), Json.class);
            Set<String> keys = RedisUtils.keys("FLINK_STREAM*");
            //根据job的code屏蔽按钮
            List<Object> collect = jobs.stream()
                    .filter(x->"RUNNING".equals(x.get("state")))
                    .map(x ->x.get("name")).collect(Collectors.toList());
            //根据redis屏蔽按钮
            List<String> collect1 = keys.stream().map(x -> x.substring(x.length() - 32))
                    .collect(Collectors.toList());

            List<EtlJob> flinkJobs = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.FLINK_STREAM.getValue());
            map.put("flinkList", flinkJobs);
            map.put("submitFlag",collect);
            map.put("redisFlag",collect1);
            return "/bigdata/calculate/flink/flinkStreamList.ftl";
        }
        return "/bigdata/calculate/spark/sparkList.ftl";
    }

    @ControllerInfo("进入spark明细页面")
    @RequestMapping(value = {"/spark/edit", "/spark/view"})
    public String editSpark(String cubeName, String operation,
                            HttpServletRequest request, ModelMap map, String id)
            throws Exception {
        EtlJob spark = StringUtils.isNotBlank(id) ? etlJobService.findOne(id)
                : new EtlJob();
        if (spark.getPath() != null) {
            spark.setAllFileNames(spark.getPath().substring(
                    spark.getPath().lastIndexOf("/") + 1));
        }
        map.put("spark", spark);
        if ("view".equals(operation)) {
            return "/bigdata/calculate/spark/sparkView.ftl";
        }
        return "/bigdata/calculate/spark/sparkEdit.ftl";
    }

    @ControllerInfo("进入flink明细页面")
    @RequestMapping(value = {"/flink/edit", "/flink/view"})
    public String editFlink(String id, String operation,
                            HttpServletRequest request, ModelMap map) {
        EtlJob flink = new EtlJob();
        if (StringUtils.isNotBlank(id)) {
            flink = etlJobService.findOne(id);
        } else {
            flink.setEtlType(EtlType.FLINK_STREAM.getValue());
        }

        List<Metadata> mdList = metadataService.findByMdType("table");
        map.put("mdList", mdList);
        map.put("appList", new ArrayList<String>());

        map.put("flink", flink);
        if ("view".equals(operation)) {
            return "/bigdata/calculate/flink/flinkStreamView.ftl";
        }
        return "/bigdata/calculate/flink/flinkStreamEdit.ftl";
    }

    @ResponseBody
    @ControllerInfo("保存flink")
    @RequestMapping("/flink/save")
    public String saveFlink(EtlJob flink) {
        try {
            //做日志时 要区分流计算和批处理 这里传一个type,1为批处理，2为流计算
            Integer type=2;
            flink.setUnitId(getLoginInfo().getUnitId());
            flink.setCreateUserId(getLoginInfo().getUserId());
            flinkJobService.saveFlinkJob(flink,type);

            return success("保存Flink任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("删除")
    @RequestMapping(value = {"/flink/delete"})
    public String deleteEtlJob(String id) {
        try {
            EtlJob job = etlJobService.findOne(id);
            etlJobService.deleteJob(job);
            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-etlJob");
            logDto.setDescription(job.getName());
            logDto.setBizName("流计算");
            logDto.setOldData(job);
            bigLogService.deleteLog(logDto);
            return success("删除任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("执行任务")
    @RequestMapping("/job/exec")
    public String execJob(String id, String params) {
        try {
            EtlJob job = etlJobService.findOne(id);
            boolean result = true;
            if (EtlType.KYLIN.getValue() == job.getEtlType()) {
                result = kylinClientService.dealJob(job);
            }
            if(EtlType.FLINK_STREAM.getValue() ==job.getEtlType()){
                RedisUtils.set("FLINK_STREAM"+id,id,60);
                result =flinkJobService.dealJob(job,null);
            }
            if (result)
                return success("执行任务成功!");
            else
                return error("执行任务失败，请查看错误日志!");
        } catch (Exception e) {
            e.printStackTrace();
            return error("出错了:" + e.getMessage());
        }
    }

    @ControllerInfo("查看日志")
    @RequestMapping("/viewLog")
    public String viewLog(String logId, HttpServletRequest request, ModelMap map) {
        EtlJobLog log = etlJobLogService.findOne(logId);
        if (log == null)
            log = new EtlJobLog();
        map.put("log", log);
        return "/bigdata/etl/viewLog.ftl";
    }

    @ControllerInfo("参数配置说明")
    @RequestMapping("/paramView")
    public String paramView(HttpServletRequest request, ModelMap map) {
        return "/bigdata/etl/etlParamView.ftl";
    }

    @ControllerInfo("进入etl日志列表(单个job)")
    @RequestMapping("/log/jobId")
    public String listByJobId(String jobId, HttpServletRequest request,
                              ModelMap map) {
        Pagination page = createPagination(request);
        page.setPageSize(5);
        List<EtlJobLog> logList = etlJobLogService.findByJobId(jobId, page);
        map.addAttribute("logList", logList);
        return "/bigdata/calculate/calculateLog.ftl";
    }

    @ResponseBody
    @ControllerInfo("保存spark")
    @RequestMapping("/spark/save")
    public String saveSpark(EtlJob spark, HttpServletRequest request) {
        try {
            spark.setUnitId(getLoginInfo().getUnitId());
            spark.setCreateUserId(getLoginInfo().getUserId());
            spark.setJobType("spark");
            spark.setEtlType(EtlType.SPARK.getValue());
            String businessFileName = request.getParameter("businessFileName");
            if (StringUtils.isNotBlank(businessFileName)) {
                spark.setBusinessFile(businessFileName);
            }
            sparkService.saveSparkJob(spark);
            // 上传分析的文件
            MultipartFile file = StorageFileUtils.getFile(request);
            OptionDto hdfs = optionService.getAllOptionParam("hdfs");
            if (file != null) {
                InputStream in = file.getInputStream();
                HdfsUtils.copyFileStreamToHdfs(in, HdfsUtils.businessFilePath
                        + spark.getId() + "/", businessFileName, hdfs.getFrameParamMap().get("hdfs_url"));
            }
            return success("保存spark任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("执行spark任务")
    @RequestMapping("/spark/exec")
    public String execSparkJob(String id) {
        try {
            SparkRestResponse result = sparkService.dealSparkJob(id);
            return "true".equals(result.getSuccess()) ? success(result
                    .getMessage()) : error(result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("停止spark任务")
    @RequestMapping("/spark/stop")
    public String stopSparkJob(String id, ModelMap map) {
        try {
            SparkRestResponse result = sparkService.stopSparkJob(id);
            map.put("status", result.getDriverState());
            return "true".equals(result.getSuccess()) ? success(result
                    .getMessage()) : error(result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return error("出错了:" + e.getMessage());
        }
    }

    @ControllerInfo("查看spark执行状态")
    @RequestMapping("/spark/status")
    public String sparkStatus(String id, ModelMap map,
                              HttpServletRequest request) {
        SparkRestResponse result = sparkService.monitorSparkJob(id);

        Pagination page = createPagination(request);
        page.setPageSize(5);
        EtlJob job = etlJobService.findOne(id);
        List<EtlJobLog> logList = etlJobLogService.findByJobId(id, page);
        map.addAttribute("logList", logList);
        map.addAttribute("jobName", job.getName());
        map.put("response", JSON.toJSONString(result));
        map.put("status", result.getDriverState());
        return "/bigdata/calculate/spark/sparkStatus.ftl";
    }

}
