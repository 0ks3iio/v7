package net.zdsoft.bigdata.taskScheduler.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.taskScheduler.service.KettleService;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Set;

@Service("kettleService")
public class KettleServiceImpl implements KettleService {
    private Logger logger = Logger.getLogger(KettleServiceImpl.class);

    @Autowired
    private EtlJobService etlJobService;

    @Autowired
    private EtlJobLogService etlJobLogService;

    @Autowired
    SysOptionRemoteService sysOptionRemoteService;

    @Resource
    private BigLogService bigLogService;

    @Override
    public void saveKettleJob(EtlJob kettle) {
        boolean isAdd = false;

        if (EtlJob.KETTLE_JOB.equals(kettle.getJobType())) {
            if (!kettle.getFileName().endsWith("kjb")) {
                logger.warn("调度主文件格式错误！");
                throw new ControllerException("调度主文件格式错误！");
            }
        }
        if (EtlJob.KETTLE_TRANSFORMATION.equals(kettle.getJobType())) {
            if (!kettle.getFileName().endsWith("ktr")) {
                logger.warn("调度主文件格式错误！");
                throw new ControllerException("调度主文件格式错误！");
            }
        }

        String systemFilePath = sysOptionRemoteService
                .findValue(Constant.FILE_PATH);
        // systemFilePath = "/Project";
        String realFilePath = systemFilePath + java.io.File.separator
                + "kettle" + java.io.File.separator + kettle.getUnitId();

        String[] fileNames = kettle.getAllFileNames().split(",");
        for (String fileName : fileNames) {
            // 处理文件
            String tempFileFullPath = kettle.getPath() + java.io.File.separator
                    + fileName;

            String realFileFullPath = realFilePath + java.io.File.separator
                    + fileName;
            File tempFile = new File(tempFileFullPath);
            if (!tempFileFullPath.equalsIgnoreCase(realFileFullPath)) {
                File newFile = new File(realFilePath);
                if (!newFile.exists()) {
                    newFile.mkdirs();
                }
                copyFile(tempFileFullPath, realFileFullPath);
                if (tempFile.exists())
                    tempFile.delete();
            }
        }
        // 处理业务
        kettle.setPath(realFilePath);
        if (kettle.getIsSchedule() == 1) {
            kettle.setHasParam(0);
        } else {
            kettle.setScheduleParam("");
        }

        if (StringUtils.isBlank(kettle.getId())) {
            isAdd = true;
            kettle.setId(UuidUtils.generateUuid());
        }

        //建立Job和来源去向的关系
        etlJobService.saveEtlJobRelations(kettle);

        if (!isAdd) {
            kettle.setModifyTime(new Date());
            EtlJob oldKettle = etlJobService.findOne(kettle.getId());
            etlJobService.update(kettle, kettle.getId(), new String[]{"name",
                    "etlType", "unitId", "jobType", "path", "isSchedule",
                    "hasParam", "fileName", "allFileNames", "scheduleParam", "sourceId", "targetId", "sourceType", "targetType",
                    "remark", "modifyTime", "flowChartJson"});
            //业务日志埋点  修改
            LogDto logDto = new LogDto();
            logDto.setBizCode("update-kettle");
            logDto.setDescription("kettle " + oldKettle.getName());
            logDto.setOldData(oldKettle);
            logDto.setNewData(kettle);
            logDto.setBizName("批处理");
            bigLogService.updateLog(logDto);

        } else {
            kettle.setCreationTime(new Date());
            kettle.setModifyTime(new Date());
            etlJobService.save(kettle);
            //业务日志埋点  新增
            LogDto logDto = new LogDto();
            logDto.setBizCode("insert-kettle");
            logDto.setDescription("kettle " + kettle.getName());
            logDto.setNewData(kettle);
            logDto.setBizName("批处理");
            bigLogService.insertLog(logDto);

        }

        // 如果是定时任务 需要增加到定时任务列表中去 定时任务指定某一台机器执行 通过参数配置
        if (kettle.getIsSchedule() == 1) {
            Json json = new Json();
            json.put("jobId", kettle.getId());
            json.put("cron", kettle.getScheduleParam());
            if (isAdd) {// 增加
                json.put("operation", "add");
            } else {// 修改
                json.put("operation", "modify");
            }
            RedisUtils.publish(EtlChannelConstant.ETL_KETTLE_REDIS_CHANNEL,
                    json.toJSONString());
        } else {
            Json json = new Json();
            json.put("jobId", kettle.getId());
            json.put("cron", "");
            // 删除
            json.put("operation", "delete");
            RedisUtils.publish(EtlChannelConstant.ETL_KETTLE_REDIS_CHANNEL,
                    json.toJSONString());
        }
    }

    @Override
    public boolean dealJob(EtlJob etlJob, String params) {
        boolean result = true;
        long startTime = System.currentTimeMillis(); // 获取开始时间
        try {
            KettleEnvironment.init();
            // jobname 是Job脚本的路径及名称
            JobMeta jobMeta = new JobMeta(etlJob.getPath() + File.separator
                    + etlJob.getFileName(), null);
            Job job = new Job(null, jobMeta);
            // 向Job 脚本传递参数，脚本中获取参数值：${参数名}
            // job.setVariable(paraname, paravalue);
            String kettlePropertiesPath = Evn
                    .getString("kettle_properties_path");
            // kettlePropertiesPath="/Project/kettle/conf/kettle.properties";
            if (StringUtils.isNotBlank(kettlePropertiesPath)) {
                job.setVariable("kettle.pro.path", kettlePropertiesPath);
            }
            job.setVariable("kettle.relative.path", etlJob.getPath());
            if (StringUtils.isNotBlank(params)) {
                JSONObject jsonObject = Json.parseObject(params);
                Set<String> paramKeys = jsonObject.keySet();
                for (String key : paramKeys) {
                    job.setVariable(key, jsonObject.getString(key));
                }
            } else {
                String kettleCustomQueryParams = Evn
                        .getString("kettle_custom_query_param");
                if (StringUtils.isNotBlank(kettleCustomQueryParams)) {
                    String[] customParams = kettleCustomQueryParams.split(";");
                    for (String param : customParams) {
                        job.setVariable(param, "null");
                    }
                } else {
                    job.setVariable("year", "null");
                    job.setVariable("month", "null");
                    job.setVariable("qsrq", "null");
                }
            }
            job.start();
            job.waitUntilFinished();

            if (job.getErrors() > 0) {
                String[] errMsgList = KettleLogStore.getAppender()
                        .getBuffer(job.getLogChannelId(), false).toString()
                        .split("\n\r\n");
                String errMsg = errMsgList[0];
                errMsg = errMsg.replaceAll("\n", "<br>");
                EtlJobLog log = assembledKettleLog(etlJob, startTime,
                        EtlJobLog.state_fail, errMsg);
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_ERROR, log.getId());
                result = false;
            } else {
                String[] errMsgList = KettleLogStore.getAppender()
                        .getBuffer(job.getLogChannelId(), false).toString()
                        .split("\n\r\n");
                String logMsg = errMsgList[0];
                logMsg = logMsg.replaceAll("\n", "<br>");
                EtlJobLog log = assembledKettleLog(etlJob, startTime,
                        EtlJobLog.state_success, logMsg);
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_TRUE, log.getId());
                RedisUtils.set(etlJob.getId(), "true");
            }
        } catch (KettleException e) {
            String errorMsg = e.getMessage();
            if (errorMsg.length() > 2000) {
                errorMsg = errorMsg.substring(0, 2000);
            }
            EtlJobLog log = assembledKettleLog(etlJob, startTime,
                    EtlJobLog.state_fail, errorMsg);
            etlJobLogService.save(log);
            etlJobService.updateEtlJobStateById(etlJob.getId(),
                    EtlJob.STATE_ERROR, log.getId());
            result = false;
            RedisUtils.set(etlJob.getId(), "false");
        }
        return result;

    }

    @Override
    public boolean dealTrans(EtlJob etlJob, String params) {
        boolean result = true;
        long startTime = System.currentTimeMillis(); // 获取开始时间
        try {
            KettleEnvironment.init();
            TransMeta transMeta = new TransMeta(etlJob.getPath()
                    + File.separator + etlJob.getFileName());
            Trans trans = new Trans(transMeta);
            trans.prepareExecution(null);
            String kettlePropertiesPath = Evn
                    .getString("kettle_properties_path");
            if (StringUtils.isNotBlank(kettlePropertiesPath)) {
                trans.setVariable("kettle.pro.path", kettlePropertiesPath);
            }
            trans.setVariable("kettle.relative.path", etlJob.getPath());
            if (StringUtils.isNotBlank(params)) {
                JSONObject jsonObject = Json.parseObject(params);
                Set<String> paramKeys = jsonObject.keySet();
                for (String key : paramKeys) {
                    trans.setVariable(key, jsonObject.getString(key));
                }
            } else {
                String kettleCustomQueryParams = Evn
                        .getString("kettle_custom_query_param");
                if (StringUtils.isNotBlank(kettleCustomQueryParams)) {
                    String[] customParams = kettleCustomQueryParams.split(";");
                    for (String param : customParams) {
                        trans.setVariable(param, "null");
                    }
                } else {
                    trans.setVariable("year", "null");
                    trans.setVariable("month", "null");
                    trans.setVariable("qsrq", "null");
                }
            }
            trans.startThreads();
            trans.waitUntilFinished();
            if (trans.getErrors() != 0) {
                String[] errMsgList = KettleLogStore.getAppender()
                        .getBuffer(trans.getLogChannelId(), false).toString()
                        .split("\n\r\n");
                String errMsg = errMsgList[0];
                errMsg = errMsg.replaceAll("\n", "<br>");
                EtlJobLog log = assembledKettleLog(etlJob, startTime,
                        EtlJobLog.state_fail, errMsg);
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_ERROR, log.getId());
                RedisUtils.set(etlJob.getId(), "false");
                result = false;
            } else {
                String[] errMsgList = KettleLogStore.getAppender()
                        .getBuffer(trans.getLogChannelId(), false).toString()
                        .split("\n\r\n");
                String logMsg = errMsgList[0];
                logMsg = logMsg.replaceAll("\n", "<br>");
                EtlJobLog log = assembledKettleLog(etlJob, startTime,
                        EtlJobLog.state_success, logMsg);
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_TRUE, log.getId());
                RedisUtils.set(etlJob.getId(), "true");
            }
        } catch (KettleException e) {
            String errorMsg = e.getMessage();
            if (errorMsg.length() > 2000) {
                errorMsg = errorMsg.substring(0, 2000);
            }
            EtlJobLog log = assembledKettleLog(etlJob, startTime,
                    EtlJobLog.state_fail, errorMsg);
            etlJobLogService.save(log);
            etlJobService.updateEtlJobStateById(etlJob.getId(),
                    EtlJob.STATE_ERROR, log.getId());
            RedisUtils.set(etlJob.getId(), "false");
            result = false;
        }
        return result;
    }

    private EtlJobLog assembledKettleLog(EtlJob etlJob, long startTime,
                                         int state, String logDescription) {
        EtlJobLog log = new EtlJobLog();
        log.setId(UuidUtils.generateUuid());
        log.setLogTime(new Date());
        log.setJobId(etlJob.getId());
        log.setName(etlJob.getName());
        log.setType(etlJob.getJobType());
        log.setUnitId(etlJob.getUnitId());
        log.setLogDescription(logDescription);
        log.setState(state);
        long endTime = System.currentTimeMillis();
        log.setDurationTime((endTime - startTime));
        return log;
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

}
