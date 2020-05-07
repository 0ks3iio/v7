package net.zdsoft.bigdata.datax.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.service.BgNodeService;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.AbstractDatabase;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.datax.dao.DataxJobDao;
import net.zdsoft.bigdata.datax.entity.*;
import net.zdsoft.bigdata.datax.enums.DataxJobParamEnum;
import net.zdsoft.bigdata.datax.enums.DataxTypeEnum;
import net.zdsoft.bigdata.datax.job.CommonReaderParameterBuilder;
import net.zdsoft.bigdata.datax.job.CommonWriterParameterBuilder;
import net.zdsoft.bigdata.datax.job.reader.*;
import net.zdsoft.bigdata.datax.job.writer.*;
import net.zdsoft.bigdata.datax.service.*;
import net.zdsoft.bigdata.datax.utils.DataxUtil;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataxJobServiceImpl extends BaseServiceImpl<DataxJob, String> implements DataxJobService {

    @Resource
    private DataxJobDao dataxJobDao;
    @Resource
    private DataxJobInsService dataxJobInsService;
    @Resource
    private DataxJobInsParamService dataxJobInsParamService;
    @Resource
    private DatabaseService databaseService;
    @Resource
    private NosqlDatabaseService nosqlDatabaseService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private DataxJobInsLogService dataxJobInsLogService;
    @Resource
    private DataxJobInsRuleService dataxJobInsRuleService;
    @Resource
    private OptionService optionService;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private BigLogService bigLogService;
    @Resource
    private BgNodeService bgNodeService;

    @Override
    protected BaseJpaRepositoryDao<DataxJob, String> getJpaDao() {
        return dataxJobDao;
    }

    @Override
    protected Class<DataxJob> getEntityClass() {
        return DataxJob.class;
    }

    @Override
    public void saveDataxJob(DataxJob dataxJob) {
        boolean isAdd = false;
        dataxJob.setModifyTime(new Date());
        if (StringUtils.isBlank(dataxJob.getId())) {
            dataxJob.setCreationTime(new Date());
            dataxJob.setId(UuidUtils.generateUuid());
            isAdd = true;
            dataxJobDao.save(dataxJob);
            //业务日志埋点  新增
            LogDto logDto = new LogDto();
            logDto.setBizCode("insert-dataxJob");
            logDto.setDescription("同步任务 " + dataxJob.getName());
            logDto.setNewData(dataxJob);
            logDto.setBizName("数据同步");
            bigLogService.insertLog(logDto);
        } else {

            DataxJob oldDataxJob = dataxJobDao.findById(dataxJob.getId()).get();
            dataxJobDao.update(dataxJob, new String[]{"name", "isSchedule", "scheduleParam", "remark", "modifyTime", "nodeId"});
            //业务日志埋点  修改
            LogDto logDto = new LogDto();
            logDto.setBizCode("update-dataxJob");
            logDto.setDescription("同步任务 " + oldDataxJob.getName());
            logDto.setOldData(oldDataxJob);
            logDto.setNewData(dataxJob);
            logDto.setBizName("数据同步");
            bigLogService.updateLog(logDto);
        }


        // 如果是定时任务 需要增加到定时任务列表中去 定时任务指定某一台机器执行 通过参数配置
        if (dataxJob.getIsSchedule() == 1) {
            Json json = new Json();
            json.put("jobId", dataxJob.getId());
            json.put("cron", dataxJob.getScheduleParam());
            if (isAdd) {// 增加
                json.put("operation", "add");
            } else {// 修改
                json.put("operation", "modify");
            }
            RedisUtils.publish(EtlChannelConstant.ETL_DATAX_REDIS_CHANNEL,
                    json.toJSONString());
        } else {
            Json json = new Json();
            json.put("jobId", dataxJob.getId());
            json.put("cron", "");
            // 删除
            json.put("operation", "delete");
            RedisUtils.publish(EtlChannelConstant.ETL_DATAX_REDIS_CHANNEL,
                    json.toJSONString());
        }
    }

    @Override
    public void executeDataxJob(String jobId) throws BigDataBusinessException {
        DataxJobIns topJobIns = dataxJobInsService.findTopJobIns(jobId);
        executeDataxJobIns(topJobIns);
    }

    @Override
    public String getDataxJobJson(String jobId) throws BigDataBusinessException {
        DataxJobIns topJobIns = dataxJobInsService.findTopJobIns(jobId);
        ExecJobConfig execJobConfig = getExecJobConfig(topJobIns);
        return execJobConfig.getJobJson();
    }

    @Override
    public List<String> getDataxJobJsons(String jobId) throws BigDataBusinessException {
        DataxJobIns topJobIns = dataxJobInsService.findTopJobIns(jobId);
        ExecJobConfig execJobConfig = getExecJobConfig(topJobIns);

        List<String> jsonList = Lists.newArrayList();
        getDataxJobJsons(execJobConfig, jsonList);

        return jsonList;
    }

    private void getDataxJobJsons(ExecJobConfig execJobConfig, List<String> jsonList) {
        jsonList.add(execJobConfig.getJobJson());
        if (execJobConfig.getChild() != null) {
            getDataxJobJsons(execJobConfig.getChild(), jsonList);
        }
    }

    @Override
    public void deleteDataxJob(String jobId) {
        // 删除日志
        dataxJobInsLogService.deleteByJobId(jobId);
        // 删除任务实例
        dataxJobInsService.deleteByJobId(jobId);
        // 删除参数
        dataxJobInsParamService.deleteByJobId(jobId);
        // 删除规则
        dataxJobInsRuleService.deleteByJobId(jobId);
        // 删除任务
        dataxJobDao.deleteById(jobId);
    }

    @Override
    public String viewJobLog(String logId) throws BigDataBusinessException {
        DataxJobInsLog log = dataxJobInsLogService.findOne(logId);
        DataxJob dataxJob = this.findOne(log.getJobId());
        String systemFilePath = sysOptionRemoteService
                .findValue(Constant.FILE_PATH);

        ExecJobConfig execJobConfig = new ExecJobConfig();
        Map<String, String> frameParamMap = getDataxParamMap();
        if (dataxJob.getNodeId() == null) {
            execJobConfig.setHost(frameParamMap.get("domain"));
            execJobConfig.setUsername(frameParamMap.get("username"));
            execJobConfig.setPassword(frameParamMap.get("password"));
            execJobConfig.setPort(Integer.valueOf(frameParamMap.get("port")));
        } else {
            Node node = bgNodeService.findOne(dataxJob.getNodeId());
            execJobConfig.setHost(node.getDomain());
            execJobConfig.setUsername(node.getUsername());
            execJobConfig.setPassword(node.getPassword());
            execJobConfig.setPort(Integer.valueOf(node.getPort()));
        }

        execJobConfig.setJobLogPath(systemFilePath + frameParamMap.get("log_path"));
        execJobConfig.setJobInsLogId(logId);
        return DataxUtil.viewLog(execJobConfig);
    }

    @Override
    public List<DataxJob> findScheduledDataxJobs() {
        return dataxJobDao.findScheduledDataxJobs();
    }

    @Override
    public List<DataxJob> findAllDataxJob() {
        dataxJobInsLogService.updateJobStatus();
        return dataxJobDao.findAllByOrderByModifyTimeDesc();
    }

    private void executeDataxJobIns(DataxJobIns dataxJobIns) throws BigDataBusinessException {
        // datax配置信息
        DataxUtil.execJob(getExecJobConfig(dataxJobIns));
    }

    private ExecJobConfig getExecJobConfig(DataxJobIns dataxJobIns) throws BigDataBusinessException {
        if (dataxJobIns == null) {
            throw new BigDataBusinessException("请添加任务配置");
        }

        CommonDataxJob dataxJob = new CommonDataxJob();

        ExecJobConfig execJobConfig = constructExecJobConfig(dataxJobIns);

        if (dataxJobIns.getChild() != null) {
            execJobConfig.setChild(getExecJobConfig(dataxJobIns.getChild()));
        }

        // 查询数据源
        List<DataxJobInsParam> dataxJobInsParamList = dataxJobInsParamService.findDataxJobParamByJobInstanceId(dataxJobIns.getId());

        if (dataxJobInsParamList.size() < 1) {
            throw new BigDataBusinessException(dataxJobIns.getName() + "配置信息不完整");
        }
        Map<String, String> paramMap = dataxJobInsParamList.stream().collect(Collectors.toMap(DataxJobInsParam::getParamKey, DataxJobInsParam::getParamValue));

        String sourceType = paramMap.get(DataxJobParamEnum.SOURCE_TYPE.getCode());
        if ("datasource".equals(sourceType)) {
            String sourceId = paramMap.get(DataxJobParamEnum.SOURCE_ID.getCode());
            execJobConfig.setDataSourceId(sourceId);
            Database source = databaseService.findOne(sourceId);

            if (source == null) {
                NosqlDatabase nosqlDatabase = nosqlDatabaseService.findOne(sourceId);
                if (nosqlDatabase == null) {
                    throw new BigDataBusinessException("源数据数据源不存在或者不支持");
                }
                String dbType = DatabaseType.parse(nosqlDatabase.getType()).getDbType();

                dataxJob.dataxReaderType = DataxTypeEnum.valueOfCode(dbType);
                dataxJob.sourceDatabase = nosqlDatabase;
                execJobConfig.setDataSourceName(nosqlDatabase.getName());
            } else {
                String dbType = DatabaseType.parse(source.getType()).getDbType();
                dataxJob.dataxReaderType = DataxTypeEnum.valueOfCode(dbType);
                dataxJob.sourceDatabase = source;
                execJobConfig.setDataSourceName(source.getName());
            }

        } else if ("textfile".equals(sourceType)) {
            dataxJob.dataxReaderType = DataxTypeEnum.TEXTFILE;
        } else if ("ftp".equals(sourceType)) {
            dataxJob.dataxReaderType = DataxTypeEnum.FTP;
        }

        execJobConfig.setIsIncrement(dataxJobIns.getIsIncrement());
        // 判断是否是增量同步
        if (dataxJobIns.getIsIncrement() != null && dataxJobIns.getIsIncrement() == 1) {
            String querySql = paramMap.get(DataxJobParamEnum.QUERY_SQL.getCode());
            if (StringUtils.isBlank(querySql)) {
                String where = paramMap.get(DataxJobParamEnum.READER_WHERE.getCode());
                if (StringUtils.isBlank(where)) {
                    throw new BigDataBusinessException("增量同步任务必须配置筛选条件!");
                }
                if (!where.contains("${start_time}") || !where.contains("${end_time}")) {
                    throw new BigDataBusinessException("增量同步任务筛选条件必须包含'${start_time}'和'${end_time}'");
                }
            } else {
                if (!querySql.contains("${start_time}") || !querySql.contains("${end_time}")) {
                    throw new BigDataBusinessException("增量同步任务查询sql必须包含'${start_time}'和'${end_time}'");
                }
            }
        }
        execJobConfig.setLastCommitTime(dataxJobIns.getLastCommitTime());

        String targetType = paramMap.get(DataxJobParamEnum.TARGET_TYPE.getCode());
        if ("metadata".equals(targetType)) {
            String targetId = paramMap.get(DataxJobParamEnum.TARGET_ID.getCode());
            Metadata metadata = metadataService.findOne(targetId);
            if (metadata == null) {
                throw new BigDataBusinessException("目标元数据不存在!");
            }

            execJobConfig.setDataTargetId(targetId);
            execJobConfig.setDataTargetName(metadata.getName());
            if ("hbase".equals(metadata.getDbType())) {
                dataxJob.dataxWriterType = metadata.getIsPhoenix() == 1 ? DataxTypeEnum.PHOENIX4 : DataxTypeEnum.HBASE11;
            } else {
                dataxJob.dataxWriterType = DataxTypeEnum.valueOfCode(metadata.getDbType());
            }
            MetadataTransfer metadataTransfer = new MetadataTransfer();
            metadataTransfer.setTableName(metadata.getTableName());
            if (DataxTypeEnum.ELASTICSEARCH.getCode().equals(metadata.getDbType())) {
                metadataTransfer.setColumnList(metadataTableColumnService.findByMetadataId(targetId));
            }
            dataxJob.targetMetadata = metadataTransfer;
        } else if ("message".equals(targetType)) {
            dataxJob.dataxWriterType = DataxTypeEnum.KAFKA;
        }

        // 转换规则 transformer
        dataxJob.dataxJobInsRules = dataxJobInsRuleService.findAllByJobInsId(dataxJobIns.getId());
        dataxJob.paramMap = paramMap;
        execJobConfig.setJobJson(JSON.toJSONString(new JobJson().setJob(dataxJob.builder())));
        return execJobConfig;
    }

    private ExecJobConfig constructExecJobConfig(DataxJobIns dataxJobIns) throws BigDataBusinessException {
        DataxJob dataxJob = this.findOne(dataxJobIns.getJobId());
        Map<String, String> frameParamMap = getDataxParamMap();
        String systemFilePath = sysOptionRemoteService
                .findValue(Constant.FILE_PATH);
        ExecJobConfig execJobConfig = new ExecJobConfig();

        Node node = bgNodeService.findOne(dataxJob.getNodeId());
        if (node == null) {
            throw new BigDataBusinessException("节点不存在!");
        }

        execJobConfig.setHost(node.getDomain());
        execJobConfig.setPort(Integer.valueOf(node.getPort()));
        execJobConfig.setJobExecPath(frameParamMap.get("exec_path"));
        execJobConfig.setJobJsonPath(systemFilePath + frameParamMap.get("json_path") + "/" + dataxJobIns.getId() + ".json");
        execJobConfig.setJobLogPath(systemFilePath + frameParamMap.get("log_path"));
        execJobConfig.setUsername(node.getUsername());
        execJobConfig.setPassword(node.getPassword());
        execJobConfig.setJobJsonMainPath(systemFilePath + frameParamMap.get("json_path"));

        execJobConfig.setJobId(dataxJobIns.getJobId());
        execJobConfig.setJobInstanceId(dataxJobIns.getId());

        execJobConfig.setJobName(dataxJob.getName());
        execJobConfig.setJobRemark(dataxJob.getRemark());
        execJobConfig.setJobInsName(dataxJobIns.getName());
        execJobConfig.setJobInsRemark(dataxJobIns.getRemark());
        return execJobConfig;
    }

    private Map<String, String> getDataxParamMap() throws BigDataBusinessException {
        OptionDto dataxDto = optionService.getAllOptionParam("datax");
        if (dataxDto == null || dataxDto.getStatus() == 0) {
            throw new BigDataBusinessException("datax服务未启动");
        }
        return dataxDto.getFrameParamMap();
    }

    private Map<String, String> getMysqlParamMap() throws BigDataBusinessException {
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        if (mysqlDto == null || mysqlDto.getStatus() == 0) {
            throw new BigDataBusinessException("mysql服务未启动");
        }
        return mysqlDto.getFrameParamMap();
    }

    private Map<String, String> getHbaseParamMap() throws BigDataBusinessException {
        OptionDto hbaseDto = optionService.getAllOptionParam("hbase");
        if (hbaseDto == null || hbaseDto.getStatus() == 0) {
            throw new BigDataBusinessException("hbase服务未启动");
        }
        return hbaseDto.getFrameParamMap();
    }

    private Map<String, String> getEsParamMap() throws BigDataBusinessException {
        OptionDto esDto = optionService.getAllOptionParam("es");
        if (esDto == null || esDto.getStatus() == 0) {
            throw new BigDataBusinessException("hbase服务未启动");
        }
        return esDto.getFrameParamMap();
    }

    class CommonDataxJob {

        private DataxTypeEnum dataxReaderType;

        private DataxTypeEnum dataxWriterType;
        /**
         * 参数map
         */
        private Map<String, String> paramMap;

        /**
         * 转换规则
         */
        private List<DataxJobInsRule> dataxJobInsRules;

        /**
         * 源数据库
         */
        private AbstractDatabase sourceDatabase;

        /**
         * 目标元数据
         */
        private MetadataTransfer targetMetadata;

        public Job builder() throws BigDataBusinessException {

            JobContent jobContent = new JobContent();
            // 组装reader
            JobContentReader reader = this.getJobContentReader();
            // 组装writer
            JobContentWriter writer = this.getJobContentWriter();
            // 转换规则
            List<JobContentTransformer> transformerList = this.getTransformerList();
            // 基础设置
            JobSetting setting = this.getSetting();

            return new Job().setContent(Lists.newArrayList(
                    jobContent
                            .setReader(reader)
                            .setWriter(writer)
                            .setTransformer(transformerList)))
                    .setSetting(setting);
        }

        protected JobContentReader getJobContentReader() throws BigDataBusinessException {
            JobContentReader reader = new JobContentReader();
            CommonReaderParameterBuilder parameter = null;
            switch (dataxReaderType) {
                case MYSQL:
                case ORACLE:
                case SQLSERVER:
                case POSTGRESQL:
                    parameter = new MysqlReaderParameterBuilder();
                    break;
                case HBASE11:
                    parameter = new HbaseReaderParameterBuilder();
                    break;
                case PHOENIX4:
                    parameter = new HbasePhoenixReaderParameterBuilder();
                    break;
                case TEXTFILE:
                    parameter = new TextFileReaderParameterBuilder();
                    break;
                case FTP:
                    parameter = new FtpReaderParameterBuilder();
                    break;
                default:
                    throw new BigDataBusinessException("不支持该数据源类型");
            }
            reader.setParameter(parameter.getJobContentReaderParameter(paramMap, sourceDatabase));
            reader.setName(dataxReaderType.getrValue());
            return reader;
        }

        protected JobContentWriter getJobContentWriter() throws BigDataBusinessException {
            JobContentWriter writer = new JobContentWriter();
            CommonWriterParameterBuilder parameter = null;
            switch (dataxWriterType) {
                case MYSQL:
                    parameter = new MysqlWriterParameterBuilder();
                    parameter.setFrameParamMap(getMysqlParamMap());
                    break;
                case HBASE11:
                    parameter = new HbaseWriterParameterBuilder();
                    parameter.setFrameParamMap(getHbaseParamMap());
                    break;
                case PHOENIX4:
                    parameter = new HbasePhoenixWriterParameterBuilder();
                    parameter.setFrameParamMap(getHbaseParamMap());
                    break;
                case KAFKA:
                    parameter = new KafkaWriterParameterBuilder();
                    break;
                case ELASTICSEARCH:
                    parameter = new EsWriterParameterBuilder();
                    parameter.setFrameParamMap(getEsParamMap());
                    break;
                default:
                    throw new BigDataBusinessException("不支持该数据源类型");
            }
            writer.setParameter(parameter.getJobContentWriterParameter(paramMap, targetMetadata));
            writer.setName(dataxWriterType.getwValue());
            return writer;
        }

        protected List<JobContentTransformer> getTransformerList() throws BigDataBusinessException {
            List<JobContentTransformer> transformerList = null;
            // 转换规则 transformer
            if (dataxJobInsRules.size() > 0) {
                transformerList = Lists.newArrayList();
                for (DataxJobInsRule rule : dataxJobInsRules) {
                    List<String> detailList = JSON.parseArray(rule.getRule().replace("notlike", "not like"), String.class);
                    if (detailList.size() < 2) {
                        throw new BigDataBusinessException("字段转换规则格式不正确");
                    }
                    JobContentTransformer jobContentTransformer = new JobContentTransformer();
                    jobContentTransformer.setName(detailList.get(1));
                    JobContentTransformerParameter jobContentTransformerParameter = new JobContentTransformerParameter();
                    int columnIndex = Integer.valueOf(detailList.get(2)) - 1;
                    jobContentTransformerParameter.setColumnIndex(columnIndex);
                    jobContentTransformerParameter.setParas(detailList.subList(3, detailList.size()));
                    jobContentTransformer.setParameter(jobContentTransformerParameter);
                    transformerList.add(jobContentTransformer);
                }
            }
            return transformerList;
        }

        /**
         * datax设置基础参数
         *
         * @return
         */
        private JobSetting getSetting() {
            return new JobSetting()
                    .setSpeed(new JobSettingSpeed()
                            .setChannel(paramMap.get(DataxJobParamEnum.CHANNEL.getCode()))
                            .setSpeedByte(paramMap.get(DataxJobParamEnum.BYTE.getCode())))
                    .setErrorLimit(new JobSettingErrorLimit()
                            .setRecord(paramMap.get(DataxJobParamEnum.RECORD.getCode()))
                            .setPercentage(paramMap.get(DataxJobParamEnum.ERROR_PERCENTAGE.getCode())));
        }
    }

}
