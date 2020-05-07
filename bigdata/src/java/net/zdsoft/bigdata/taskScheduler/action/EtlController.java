package net.zdsoft.bigdata.taskScheduler.action;


import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.service.BgNodeServerService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.utils.HdfsUtils;
import net.zdsoft.bigdata.datax.entity.DataxJob;
import net.zdsoft.bigdata.datax.entity.DataxJobInsLog;
import net.zdsoft.bigdata.datax.service.DataxJobInsLogService;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.frame.data.kylin.KylinClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobStep;
import net.zdsoft.bigdata.taskScheduler.entity.KylinCube;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.taskScheduler.service.*;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bigdata/etl")
public class EtlController extends BigdataBaseAction {

    @Autowired
    private KettleService kettleService;

    @Autowired
    private ShellJobService shellJobService;

    @Autowired
    private FlinkJobService flinkJobService;

    @Autowired
    private EtlJobService etlJobService;

    @Autowired
    private EtlJobStepService etlJobStepService;

    @Autowired
    private KylinClientService kylinClientService;

    @Autowired
    private PythonJobService pythonJobService;

    @Autowired
    private DataxJobService dataxJobService;

    @Autowired
    private DataxJobInsLogService dataxJobInsLogService;

    @Autowired
    private SparkService sparkService;

    @Autowired
    private GroupJobService groupJobService;

    @Autowired
    private EtlJobLogService etlJobLogService;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private OptionService optionService;

    @Resource
    private OpenApiAppService openApiAppService;

    @Resource
    private BigLogService bigLogService;

    @Resource
    private BgNodeServerService bgNodeServerService;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        return "/bigdata/etl/etlOverView.ftl";
    }

    @RequestMapping("/detail")
    public String detail(String etlType, ModelMap map) {
        map.put("etlType", etlType);
        OptionDto streamsetsDto = optionService.getAllOptionParam("streamsets");
        if (streamsetsDto != null && streamsetsDto.getStatus() == 1) {
            String etlMoreUrl = streamsetsDto.getFrameParamMap().get(
                    "streamsets_url");
            map.put("etlMoreUrl", etlMoreUrl);
        }
        return "/bigdata/etl/etlIndex.ftl";
    }

    @ControllerInfo("进入列表页面")
    @RequestMapping("/list")
    public String etlList(Integer etlType, HttpServletRequest request,
                          ModelMap map) throws Exception {
        //kettle
        if (EtlType.KETTLE.getValue() == etlType) {
            List<EtlJob> kettleList = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.KETTLE.getValue());
            map.put("kettleList", kettleList);
            return "/bigdata/etl/kettle/kettleList.ftl";
        } else if (EtlType.SHELL.getValue() == etlType) {
            OptionDto sqoopDto = optionService.getAllOptionParam("sqoop");
            if (sqoopDto == null || sqoopDto.getStatus() == 0) {
                map.put("serverName", "Sqoop");
                map.put("serverCode", "sqoop");
                return "/bigdata/noServer.ftl";
            }
            List<EtlJob> shellList = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.SHELL.getValue());
            map.put("shellList", shellList);
            return "/bigdata/etl/shell/shellList.ftl";
        } else if (EtlType.KYLIN.getValue() == etlType) {
            OptionDto kylinDto = optionService.getAllOptionParam("kylin");
            if (kylinDto == null || kylinDto.getStatus() == 0) {
                map.put("serverName", "Kylin");
                map.put("serverCode", "kylin");
                return "/bigdata/noServer.ftl";
            }
            List<KylinCube> kylinList = kylinClientService
                    .getKylinCubesList(getLoginInfo().getUnitId());
            map.put("kylinList", kylinList);
            return "/bigdata/etl/kylin/kylinList.ftl";
        } else if (EtlType.FLINK.getValue() == etlType) {
            OptionDto flinkDto = optionService.getAllOptionParam("flink");
            if (flinkDto == null || flinkDto.getStatus() == 0) {
                map.put("serverName", "Flink");
                map.put("serverCode", "Flink");
                return "/bigdata/noServer.ftl";
            }
            List<EtlJob> flinkList = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.FLINK.getValue());
            map.put("flinkList", flinkList);
            return "/bigdata/etl/flink/flinkList.ftl";
        } else if (EtlType.PYTHON.getValue() == etlType) {
            OptionDto flinkDto = optionService.getAllOptionParam("python");
            if (flinkDto == null || flinkDto.getStatus() == 0) {
                map.put("serverName", "Python");
                map.put("serverCode", "Python");
                return "/bigdata/noServer.ftl";
            }
            List<EtlJob> pythonList = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.PYTHON.getValue());
            map.put("pythonList", pythonList);
            return "/bigdata/etl/python/pythonList.ftl";
        } else if (EtlType.SPARK.getValue() == etlType) {
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
            return "/bigdata/etl/spark/sparkList.ftl";
        } else if (EtlType.GROUP.getValue() == etlType) {
            List<EtlJob> groupList = etlJobService.findEtlJobsByUnitId(
                    getLoginInfo().getUnitId(), EtlType.GROUP.getValue());
            map.put("groupList", groupList);
            return "/bigdata/etl/group/groupList.ftl";
        } else if (0 == etlType) {
            List<EtlJob> systemJobList = etlJobService.findEtlJobsByUnitId(
                    BaseConstants.ZERO_GUID, 0);
            map.put("systemJobList", systemJobList);
            return "/bigdata/etl/system/systemJobList.ftl";
        } else {
            return "/bigdata/v3/common/error.ftl";
        }
    }

    @ControllerInfo("进入kettle/shell明细页面")
    @RequestMapping(value = {"/kettle/edit", "/kettle/view"})
    public String editKettle(String id, String operation,
                             HttpServletRequest request, ModelMap map) {
        EtlJob kettle = new EtlJob();
        if (StringUtils.isNotBlank(id)) {
            kettle = etlJobService.findOne(id);
        } else {
            kettle.setEtlType(EtlType.KETTLE.getValue());
            kettle.setSourceType("table");
        }
        map.put("kettle", kettle);

        List<Metadata> mdList = metadataService.findByMdType("table");
        map.put("mdList", mdList);
        List<OpenApiApp> appList = openApiAppService.getApps();
        map.put("appList", appList);

        if ("view".equals(operation)) {
            return "/bigdata/etl/kettle/kettleView.ftl";
        }
        return "/bigdata/etl/kettle/kettleEdit.ftl";
    }

    @ControllerInfo("进入shell明细页面")
    @RequestMapping(value = {"/shell/edit", "/shell/view"})
    public String editShell(String id, String operation,
                            HttpServletRequest request, ModelMap map) {
        EtlJob shell = new EtlJob();
        if (StringUtils.isNotBlank(id)) {
            shell = etlJobService.findOne(id);
        } else {
            shell.setEtlType(EtlType.SHELL.getValue());
        }
        List<Metadata> mdList = metadataService.findByMdType("table");
        map.put("mdList", mdList);

        List<Node> nodeList = bgNodeServerService.findByTypeAndStatus("sqoop");
        map.put("nodeList", nodeList);
        List<OpenApiApp> appList = openApiAppService.getApps();
        map.put("appList", appList);
        map.put("shell", shell);
        if ("view".equals(operation)) {
            return "/bigdata/etl/shell/shellView.ftl";
        }
        return "/bigdata/etl/shell/shellEdit.ftl";
    }

    @ControllerInfo("进入flink明细页面")
    @RequestMapping(value = {"/flink/edit", "/flink/view"})
    public String editFlink(String id, String operation,
                            HttpServletRequest request, ModelMap map) {
        EtlJob flink = new EtlJob();
        if (StringUtils.isNotBlank(id)) {
            flink = etlJobService.findOne(id);
        } else {
            flink.setEtlType(EtlType.FLINK.getValue());
        }
        List<Metadata> mdList = metadataService.findByMdType("table");
        map.put("mdList", mdList);

        List<Node> nodeList = bgNodeServerService.findByTypeAndStatus("flink");
        map.put("nodeList", nodeList);

        List<OpenApiApp> appList = openApiAppService.getApps();
        map.put("appList", appList);
        map.put("flink", flink);
        if ("view".equals(operation)) {
            return "/bigdata/etl/flink/flinkView.ftl";
        }
        return "/bigdata/etl/flink/flinkEdit.ftl";
    }

    @ControllerInfo("进入python明细页面")
    @RequestMapping(value = {"/python/edit", "/python/view"})
    public String editPython(String id, String operation,
                             HttpServletRequest request, ModelMap map) {
        EtlJob python = new EtlJob();
        if (StringUtils.isNotBlank(id)) {
            python = etlJobService.findOne(id);
        } else {
            python.setEtlType(EtlType.PYTHON.getValue());
        }
        List<Metadata> mdList = metadataService.findByMdType("table");
        map.put("mdList", mdList);

        List<Node> nodeList = bgNodeServerService.findByTypeAndStatus("python");
        map.put("nodeList", nodeList);

        List<OpenApiApp> appList = openApiAppService.getApps();
        map.put("appList", appList);
        map.put("python", python);
        if ("view".equals(operation)) {
            return "/bigdata/etl/python/pythonView.ftl";
        }
        return "/bigdata/etl/python/pythonEdit.ftl";
    }

    @ControllerInfo("进入spark明细页面")
    @RequestMapping(value = {"/spark/edit", "/spark/view"})
    public String editSpark(String operation,
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
            return "/bigdata/etl/spark/sparkView.ftl";
        }
        return "/bigdata/etl/spark/sparkEdit.ftl";
    }

    @ControllerInfo("进入kylin明细页面")
    @RequestMapping(value = {"/kylin/edit", "/kylin/view"})
    public String editKylin(String cubeName, String operationType,
                            HttpServletRequest request, ModelMap map) throws Exception {
        EtlJob kylin = new EtlJob();
        if (StringUtils.isNotBlank(cubeName)) {
            kylin = kylinClientService.getKylinJob(getLoginInfo().getUnitId(),
                    cubeName);
            if (kylin == null) {
                kylin = new EtlJob();
            }
        }
        if (StringUtils.isBlank(kylin.getId())) {
            kylin.setEtlType(EtlType.KYLIN.getValue());
            kylin.setJobType(EtlJob.KYLIN_JOB);
        }

        KylinCube cube = kylinClientService.getKylinCube(cubeName);
        map.put("kylin", kylin);
        map.put("kylinCube", cube);
        if ("view".equals(operationType)) {
            return "/bigdata/etl/kylin/kylinView.ftl";
        }
        return "/bigdata/etl/kylin/kylinEdit.ftl";
    }

    @ControllerInfo("进入group明细页面")
    @RequestMapping(value = {"/group/edit", "/group/view"})
    public String editGroup(String id, HttpServletRequest request, ModelMap map) {
        EtlJob group = new EtlJob();
        if (StringUtils.isNotBlank(id)) {
            group = etlJobService.findOne(id);
        } else {
            group.setEtlType(EtlType.GROUP.getValue());
        }
        List<EtlJobStep> steps = etlJobStepService.findEtlJobStepsByGroupId(id);
        map.put("group", group);
        map.put("steps", steps);
        return "/bigdata/etl/group/groupEdit.ftl";
    }

    @ResponseBody
    @ControllerInfo("保存kettle")
    @RequestMapping("/kettle/save")
    public String saveKettle(EtlJob kettle) {
        try {
            kettle.setUnitId(getLoginInfo().getUnitId());
            kettle.setCreateUserId(getLoginInfo().getUserId());
            kettleService.saveKettleJob(kettle);
            return success("保存kettle任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("保存shell")
    @RequestMapping("/shell/save")
    public String saveShell(EtlJob shell) {
        try {
            shell.setUnitId(getLoginInfo().getUnitId());
            shell.setCreateUserId(getLoginInfo().getUserId());
            shellJobService.saveShellJob(shell);
            return success("保存shell任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("保存flink")
    @RequestMapping("/flink/save")
    public String saveFlink(EtlJob flink) {
        try {
            //做日志时 要区分流计算和批处理 这里传一个type,1为批处理，2为流计算
            Integer type = 1;
            flink.setUnitId(getLoginInfo().getUnitId());
            flink.setCreateUserId(getLoginInfo().getUserId());
            flinkJobService.saveFlinkJob(flink, type);
            return success("保存Flink任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("保存python")
    @RequestMapping("/python/save")
    public String savePython(EtlJob python) {
        try {
            python.setUnitId(getLoginInfo().getUnitId());
            python.setCreateUserId(getLoginInfo().getUserId());
            pythonJobService.savePythonJob(python);
            return success("保存Python任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @ResponseBody
    @ControllerInfo("保存kylin")
    @RequestMapping("/kylin/save")
    public String saveKylin(EtlJob kylin) {
        try {
            kylin.setUnitId(getLoginInfo().getUnitId());
            kylin.setCreateUserId(getLoginInfo().getUserId());
            kylinClientService.saveKylinJob(kylin);
            return success("保存kylin任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
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
    @ControllerInfo("保存group")
    @RequestMapping("/group/save")
    public String saveGroup(EtlJob group, String jobIds) {
        try {
            group.setUnitId(getLoginInfo().getUnitId());
            group.setCreateUserId(getLoginInfo().getUserId());
            groupJobService.saveGroupJob(group, jobIds);
            return success("保存Group任务成功!");
        } catch (Exception e) {
            return error("出错了:" + e.getMessage());
        }
    }

    @RequestMapping("/group/jobs")
    public String loadGroupJobs(String jobId, ModelMap map) {
        List<EtlJob> jobs = etlJobService.findByUnitId(getLoginInfo().getUnitId());
        List<EtlJob> resultList = new ArrayList<>();
        for (EtlJob job : jobs) {
            if (job.getEtlType() != 0 && job.getEtlType() != 8 && job.getEtlType() != 9) {
                resultList.add(job);
            }
        }
        map.put("jobs", resultList);
        map.put("jobId", jobId);
        return "/bigdata/etl/group/groupJobs.ftl";
    }

    @ResponseBody
    @ControllerInfo("删除etl")
    @RequestMapping(value = {"/kettle/delete", "/kylin/delete", "/flink/delete", "/kylin/delete", "/spark/delete", "/python/delete",
            "/shell/delete", "/group/delete"})
    public String deleteEtlJob(String id) {
        try {
            EtlJob job = etlJobService.findOne(id);

            //判断是否被group引用
            Long count = etlJobStepService.countByJobId(id);
            if (count != null && count > 0) {
                return error("该批处理任务已经被引用，不能删除");
            }
            etlJobService.deleteJob(job);
            //业务日志埋点  删除
            LogDto logDto = new LogDto();
            logDto.setBizCode("delete-etl");
            logDto.setDescription("批处理任务 " + job.getName());
            logDto.setBizName("批处理");
            logDto.setOldData(job);
            bigLogService.deleteLog(logDto);

            Json json = new Json();
            json.put("jobId", id);
            json.put("cron", "");
            json.put("operation", "delete");
            if (job.getEtlType() == EtlType.KETTLE.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_KETTLE_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.SHELL.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_SHELL_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.FLINK.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_FLINK_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.KYLIN.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_KYLIN_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.SPARK.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_SPARK_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.PYTHON.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_PYTHON_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.GROUP.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_GROUP_REDIS_CHANNEL,
                        json.toJSONString());
            }
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
            if (EtlType.KETTLE.getValue() == job.getEtlType()) {
                if (EtlJob.KETTLE_JOB.equalsIgnoreCase(job.getJobType())) {
                    result = kettleService.dealJob(job, params);
                } else {
                    result = kettleService.dealTrans(job, params);
                }
            } else if (EtlType.SHELL.getValue() == job.getEtlType()) {
                result = shellJobService.dealJob(job, null);
            } else if (EtlType.FLINK.getValue() == job.getEtlType()) {
                result = flinkJobService.dealJob(job, null);
            } else if (EtlType.KYLIN.getValue() == job.getEtlType()) {
                result = kylinClientService.dealJob(job);
            } else if (EtlType.PYTHON.getValue() == job.getEtlType()) {
                result = pythonJobService.dealJob(job, null);
            } else if (EtlType.GROUP.getValue() == job.getEtlType()) {
                result = groupJobService.dealJob(job, null);
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

    @ControllerInfo("进入etl日志列表")
    @RequestMapping("/log")
    public String list(HttpServletRequest request, ModelMap model,
                       @RequestParam(required = false) Date beginDate,
                       @RequestParam(required = false) Date endDate,
                       @RequestParam(required = false, value = "taskName") String taskName) {
        Pagination page = createPagination(request);
        if (beginDate == null) {
            beginDate = DateUtils.currentStartDate();
        }
        if (endDate == null) {
            endDate = DateUtils.currentEndDate();
        }
        List<EtlJobLog> kettleLogs = etlJobLogService.findByUnitId(
                getLoginInfo().getUnitId(), page, taskName,
                DateUtils.getStartDate(beginDate),
                DateUtils.getEndDate(endDate));
        model.addAttribute("kettleLogs", kettleLogs);
        model.addAttribute("beginDate",
                DateUtils.date2String(beginDate, "yyyy-MM-dd"));
        model.addAttribute("endDate",
                DateUtils.date2String(endDate, "yyyy-MM-dd"));
        model.addAttribute("taskName", taskName);
        sendPagination(request, model,null, page);
        return "/bigdata/etl/etlLog.ftl";
    }

    @ControllerInfo("进入etl日志列表(单个job)")
    @RequestMapping("/log/jobId")
    public String listByJobId(String jobId, HttpServletRequest request,
                              ModelMap map) {
        Pagination page = createPagination(request);
        page.setPageSize(5);
        EtlJob job = etlJobService.findOne(jobId);
        List<EtlJobLog> logList = etlJobLogService.findByJobId(jobId, page);
        map.addAttribute("logList", logList);
        map.addAttribute("jobName", job.getName());
        return "/bigdata/etl/etlLog.ftl";
    }

    @RequestMapping("/datax")
    public String datax(ModelMap map) {
        List<DataxJob> dataxJobList = dataxJobService.findAllDataxJob();
        dataxJobList.forEach(e -> {
            List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobId(e.getId());
            if (logs.size() > 0) {
                e.setDataxJobInsLog(logs.get(0));
            }
        });
        map.put("dataxJobList", dataxJobList);
        return "/bigdata/etl/datax/dataxList.ftl";
    }
}
