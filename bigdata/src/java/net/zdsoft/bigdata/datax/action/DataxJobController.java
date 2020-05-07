package net.zdsoft.bigdata.datax.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.service.BgNodeServerService;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.datax.entity.*;
import net.zdsoft.bigdata.datax.enums.DataxJobStatusEnum;
import net.zdsoft.bigdata.datax.enums.DataxTypeEnum;
import net.zdsoft.bigdata.datax.service.*;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2019/5/5 11:12.
 */
@Controller
@RequestMapping("/bigdata/datax")
public class DataxJobController extends BigdataBaseAction {

    @Resource
    private DataxJobService dataxJobService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private DataxJobInsService dataxJobInsService;
    @Resource
    private DataxJobInsParamService dataxJobInsParamService;
    @Resource
    private DataxJobInsLogService dataxJobInsLogService;
    @Resource
    private DataxJobInsRuleService dataxJobInsRuleService;
    @Resource
    private OpenApiAppService openApiAppService;
    @Resource
    private BigLogService bigLogService;
    @Resource
    private NosqlDatabaseService nosqlDatabaseService;
    @Resource
    private BgNodeServerService bgNodeServerService;


    @RequestMapping("/job/list")
    public String jobList(ModelMap map) {
        List<DataxJob> dataxJobs = dataxJobService.findAllDataxJob();
        dataxJobs.forEach(e->{
            List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobId(e.getId());
            if (logs.size() > 0) {
                e.setDataxJobInsLog(logs.get(0));
            }
        });
        map.put("dataxJobs", dataxJobs);
        return "/bigdata/datax/list";
    }

    @RequestMapping("/job/edit")
    public String jobEdit(ModelMap map, String dataxJobId) {
        DataxJob dataxJob = StringUtils.isNotBlank(dataxJobId) ? dataxJobService.findOne(dataxJobId) : new DataxJob();
        List<Node> nodeList = bgNodeServerService.findByTypeAndStatus("datax");
        map.put("dataxJob", dataxJob);
        map.put("nodeList", nodeList);
        return "/bigdata/etl/datax/dataxAdd.ftl";
    }

    @RequestMapping("/job/editJobInstance")
    public String editJobInstance(ModelMap map, String dataxJobId) {
        if (StringUtils.isNotBlank(dataxJobId)) {
            DataxJobIns topJobIns = dataxJobInsService.findTopJobIns(dataxJobId);
            map.put("topJobIns", topJobIns);
        }
        map.put("dataxJobId", dataxJobId);
        return "/bigdata/etl/datax/dataxEdit.ftl";
    }

    @RequestMapping("/job/jobConfig")
    public String jobConfig(ModelMap map, String jobInsId) {

        String unitId = getLoginInfo().getUnitId();
        // 查询数据源
        List<Database> databaseList = databaseService.findDatabasesByUnitId(unitId);
        List<NosqlDatabase> nosqlDatabaseList = nosqlDatabaseService.findNosqlDatabasesByUnitIdAndType(unitId, DatabaseType.HBASE.getType());
        List<AbstractDatabase> dbList = new ArrayList();
        for (Database database : databaseList) {
            if (DatabaseType.MYSQL.getType().equals(database.getType())) {
                dbList.add(database);
            }

            if (DatabaseType.ORACLE.getType().equals(database.getType())) {
                dbList.add(database);
            }

            if (DatabaseType.SQL_SERVER.getType().equals(database.getType())) {
                dbList.add(database);
            }
        }

        Map<String, String> datasourceMap = databaseList.stream().collect(Collectors.toMap(Database::getId, AbstractDatabase::getName));
        datasourceMap.putAll(nosqlDatabaseList.stream().collect(Collectors.toMap(NosqlDatabase::getId, NosqlDatabase::getName)));

        // 元数据表数据
        List<Metadata> metadataList = metadataService.findByMdType("table");
        Map<String, String> metadataMap = metadataList.stream().collect(Collectors.toMap(Metadata::getId, Metadata::getName));

        List<DataxJobInsParam> dataxJobInsParamList = dataxJobInsParamService.findDataxJobParamByJobInstanceId(jobInsId);
        Map<String, String> dataxJobInsParamMap = dataxJobInsParamList.stream().collect(Collectors.toMap(DataxJobInsParam::getParamKey, DataxJobInsParam::getParamValue));

        if (dataxJobInsParamList.size() > 0) {
            String writerColumn = dataxJobInsParamMap.get("writerColumn");
            String[] writerColumns = writerColumn.split(",");
            String readerColumn = dataxJobInsParamMap.get("readerColumn");
            if (readerColumn != null) {
                String[] readerColumns = readerColumn.split(",");
                map.put("readerColumns", readerColumns);
                map.put("writerColumns", writerColumns);
            }
        }

        Map<String, List<String>> transformerMap = Maps.newHashMap();
        List<DataxJobInsRule> dataxJobInsRules =  dataxJobInsRuleService.findAllByJobInsId(jobInsId);
        // 转换规则 transformer
        if (dataxJobInsRules.size() > 0) {
            Map<String, List<DataxJobInsRule>> ruleMap = dataxJobInsRules.stream().collect(Collectors.groupingBy(DataxJobInsRule::getColumnId));
            ruleMap.forEach((k, v) -> {
                List<String> detailList = Lists.newArrayList();
                for (DataxJobInsRule rule : v) {
                    String ru = rule.getRule();
                    ru = ru.replace(">", "&gt;").replace(" ", "&nbsp;");
                    detailList.add(ru);
                }
                transformerMap.put(k, detailList);
            });
        }

        DataxJobIns dataxJobIns = dataxJobInsService.findOne(jobInsId);

        List<OpenApiApp> apps = openApiAppService.getApps();
        Map<String, String> appMap = apps.stream().collect(Collectors.toMap(OpenApiApp::getId, OpenApiApp::getName));
        map.put("transformerMap", transformerMap);
        map.put("apps", apps);
        map.put("appMap", appMap);

        map.put("dbList", dbList);
        map.put("nosqlDatabaseList", nosqlDatabaseList);
        map.put("metadataList", metadataList);
        map.put("dataxJobInsParamMap", dataxJobInsParamMap);
        map.put("datasourceMap", datasourceMap);
        map.put("metadataMap", metadataMap);
        map.put("dataxJobIns", dataxJobIns);
        return "/bigdata/etl/datax/dataxConfig.ftl";
    }

    @RequestMapping("/job/jobReaderConfig")
    public String jobReaderConfig(ModelMap map, String jobInsId, String dbType, String connectMode) {
        List<DataxJobInsParam> dataxJobInsParamList = dataxJobInsParamService.findDataxJobParamByJobInstanceId(jobInsId);
        Map<String, String> dataxJobInsParamMap = dataxJobInsParamList.stream().collect(Collectors.toMap(DataxJobInsParam::getParamKey, DataxJobInsParam::getParamValue));
        map.put("dataxJobInsParamMap", dataxJobInsParamMap);

        if (DataxTypeEnum.MYSQL.getCode().equals(dbType)
                || DataxTypeEnum.ORACLE.getCode().equals(dbType)
                || DataxTypeEnum.SQLSERVER.getCode().equals(dbType)
                || DataxTypeEnum.POSTGRESQL.getCode().equals(dbType)) {
            if ("onlySql".equals(connectMode)) {
                return "/bigdata/etl/datax/sourceConfig/onlySqlConfig.ftl";
            }
            return "/bigdata/etl/datax/sourceConfig/commonConfig.ftl";
        } else if (DataxTypeEnum.HBASE11.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/sourceConfig/hbase11xConfig.ftl";
        } else if (DataxTypeEnum.PHOENIX4.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/sourceConfig/hbase11xsqlConfig.ftl";
        } else if (DataxTypeEnum.TEXTFILE.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/sourceConfig/textfileConfig.ftl";
        } else if (DataxTypeEnum.FTP.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/sourceConfig/ftpConfig.ftl";
        }
        return "/bigdata/etl/datax/sourceConfig/commonConfig.ftl";
    }

    @RequestMapping("/job/jobWriterConfig")
    public String jobWriterConfig(ModelMap map, String jobInsId, String dbType) {
        List<DataxJobInsParam> dataxJobInsParamList = dataxJobInsParamService.findDataxJobParamByJobInstanceId(jobInsId);
        Map<String, String> dataxJobInsParamMap = dataxJobInsParamList.stream().collect(Collectors.toMap(DataxJobInsParam::getParamKey, DataxJobInsParam::getParamValue));
        map.put("dataxJobInsParamMap", dataxJobInsParamMap);
        map.put("dbType", dbType);

        if (DataxTypeEnum.MYSQL.getCode().equals(dbType)
                || DataxTypeEnum.ORACLE.getCode().equals(dbType)
                || DataxTypeEnum.SQLSERVER.getCode().equals(dbType)
                || DataxTypeEnum.POSTGRESQL.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/targetConfig/commonConfig.ftl";
        } else if (DataxTypeEnum.HBASE11.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/targetConfig/hbaseConfig.ftl";
        } else if (DataxTypeEnum.KAFKA.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/targetConfig/kafkaConfig.ftl";
        } else if (DataxTypeEnum.ELASTICSEARCH.getCode().equals(dbType)) {
            return "/bigdata/etl/datax/targetConfig/esConfig.ftl";
        }
        return "/bigdata/etl/datax/targetConfig/commonConfig.ftl";
    }

    @RequestMapping("/job/saveJobParam")
    @ResponseBody
    public Response saveJobParam(String paramMap, String jobId, String jobInsId, Integer isIncrement) {

        try {
            Map<String, String> pMap = JSON.parseObject(
                    paramMap, new TypeReference<HashMap<String, String>>() {
                    });

            // 保存是否增量同步
            DataxJobIns dataxJobIns = new DataxJobIns();
            dataxJobIns.setId(jobInsId);
            dataxJobIns.setJobId(jobId);
            dataxJobIns.setIsIncrement(isIncrement);


            List<DataxJobInsParam> dataxJobInsParamList = Lists.newArrayList();
            for (Map.Entry<String, String> entry : pMap.entrySet()) {
                if (StringUtils.isBlank(entry.getValue())) {
                    continue;
                }
                DataxJobInsParam param = new DataxJobInsParam();
                param.setJobId(jobId);
                param.setJobInsId(jobInsId);
                param.setParamKey(entry.getKey());
                param.setParamValue(entry.getValue());
                dataxJobInsParamList.add(param);
            }
            dataxJobInsParamService.saveDataxJobInsParam(pMap, dataxJobIns, dataxJobInsParamList);

            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-dataxJobInsParam");
            logDto.setDescription("任务配置参数 ");
            logDto.setNewData(dataxJobIns);
            logDto.setBizName("数据同步任务配置");
            bigLogService.insertLog(logDto);

            return Response.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.error().message(e.getMessage()).build();
        }
    }

    @RequestMapping("/job/saveJobInstance")
    @ResponseBody
    public Response saveJobInstance(DataxJobIns dataxJobIns) {
        dataxJobInsService.saveDataxJobIns(dataxJobIns);
        return Response.ok().data(dataxJobIns.getId()).build();
    }

    @RequestMapping("/job/deleteJobInstance")
    @ResponseBody
    public Response deleteJobInstance(String id) {
        List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobInstanceId(id);
        if (logs.size() > 0) {
            DataxJobInsLog dataxJobInsLog = logs.get(0);
            if (DataxJobStatusEnum.EXECUTING.getCode().equals(dataxJobInsLog.getStatus())) {
                return Response.error().message("任务正在执行中, 不能删除!").build();
            }
        }
        dataxJobInsService.deleteDataxJobIns(id);
        return Response.ok().build();
    }

    @RequestMapping("/job/saveJob")
    @ResponseBody
    public Response saveJob(DataxJob dataxJob) {
        dataxJobService.saveDataxJob(dataxJob);
        return Response.ok().data(dataxJob).build();
    }

    @RequestMapping("/job/executeJob")
    @ResponseBody
    public Response executeJob(String jobId) {
        try {
            List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobId(jobId);
            if (logs.size() > 0) {
                DataxJobInsLog dataxJobInsLog = logs.get(0);
                if (DataxJobStatusEnum.EXECUTING.getCode().equals(dataxJobInsLog.getStatus())) {
                    return Response.error().message("任务正在执行中, 请勿重复执行!").build();
                }
            }
            dataxJobService.executeDataxJob(jobId);
        } catch (BigDataBusinessException e) {
            e.printStackTrace();
            return Response.error().message(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    @RequestMapping("/job/previewJobJson")
    public String previewJobJson(ModelMap map, String jobId) {
        try {

            List<String> dataxJobJsons = dataxJobService.getDataxJobJsons(jobId);
            if (dataxJobJsons.size() == 1) {
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(dataxJobJsons.get(0), Object.class);
                map.put("json", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
            } else {
                List<String> jsons = Lists.newArrayList();
                for (String e : dataxJobJsons) {
                    ObjectMapper mapper = new ObjectMapper();
                    Object obj = mapper.readValue(e, Object.class);
                    jsons.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
                }
                map.put("jsons", jsons);
            }

        } catch (Exception e) {
            map.put("json", e.getMessage());
        }
        return "/bigdata/etl/datax/previewJson.ftl";
    }

    @RequestMapping("/job/log")
    public String log(ModelMap map, String jobId) {
        List<DataxJobInsLog> logList = dataxJobInsLogService.findByJobId(jobId);
        if (logList.size() > 3) {
            logList = logList.subList(0, 3);
        }
        map.put("logList", logList);
        return "/bigdata/etl/datax/dataxLog.ftl";
    }

    @RequestMapping("/job/viewLog")
    public String viewLog(ModelMap map, String logId) {
        try {
            String logDescription = dataxJobService.viewJobLog(logId);
            if (StringUtils.isBlank(logDescription)) {
                DataxJobInsLog dataxJobInsLog = dataxJobInsLogService.findOne(logId);
                logDescription = dataxJobInsLog.getJobInsRemark();
            }
            dataxJobService.viewJobLog(logId);
            map.put("logDescription", logDescription);
        } catch (BigDataBusinessException e) {
            log.error(e.getMessage(), e);
            map.put("logDescription", e.getMessage());
        }
        return "/bigdata/etl/datax/viewLog.ftl";
    }

    @RequestMapping("/job/monitorJob")
    @ResponseBody
    public Response monitorJob(String jobId) {
        List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobId(jobId);
        DataxJobInsLog dataxJobInsLog = logs.get(0);
        return Response.ok().data(dataxJobInsLog).build();
    }

    @RequestMapping("/job/deleteDataxJob")
    @ResponseBody
    public Response deleteDataxJob(String jobId) {
        List<DataxJobInsLog> logs = dataxJobInsLogService.findByJobId(jobId);
        if (logs.size() > 0) {
            DataxJobInsLog dataxJobInsLog = logs.get(0);
            if (DataxJobStatusEnum.EXECUTING.getCode().equals(dataxJobInsLog.getStatus())) {
                return Response.error().message("任务正在执行中, 不能删除!").build();
            }
        }
        DataxJob oldDataxJob = dataxJobService.findOne(jobId);
        dataxJobService.deleteDataxJob(jobId);

        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-dataxJob");
        logDto.setDescription("同步任务 "+oldDataxJob.getName());
        logDto.setBizName("数据同步");
        logDto.setOldData(oldDataxJob);
        bigLogService.deleteLog(logDto);

        return Response.ok().build();
    }
}
