package net.zdsoft.bigdata.taskScheduler.service.impl;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.service.BgNodeService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobStep;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.taskScheduler.service.*;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.log4j.Logger;
import org.pentaho.di.trans.steps.jobexecutor.JobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("groupJobService")
public class GroupJobServiceImpl implements GroupJobService {
    private Logger logger = Logger.getLogger(GroupJobServiceImpl.class);

    @Autowired
    private EtlJobService etlJobService;

    @Autowired
    private EtlJobLogService etlJobLogService;

    @Autowired
    private EtlJobStepService etlJobStepService;

    @Autowired
    SysOptionRemoteService sysOptionRemoteService;

    @Resource
    private BigLogService bigLogService;

    @Override
    public void saveGroupJob(EtlJob group, String jobIds) {
        boolean isAdd = false;


        if (group.getIsSchedule() == 1) {
            group.setHasParam(0);
        } else {
            group.setScheduleParam("");
        }
        if (StringUtils.isBlank(group.getId())) {
            isAdd = true;
            group.setId(UuidUtils.generateUuid());
        }

        if (!isAdd) {
            group.setModifyTime(new Date());
            EtlJob oldGroup = etlJobService.findOne(group.getId());
            etlJobService.update(group, group.getId(), new String[]{"name",
                    "etlType", "unitId", "jobType", "isSchedule", "scheduleParam", "remark", "modifyTime", "flowChartJson"});
            //业务日志埋点  修改
            LogDto logDto = new LogDto();
            logDto.setBizCode("update-group");
            logDto.setDescription("group " + oldGroup.getName());
            logDto.setOldData(oldGroup);
            logDto.setNewData(group);
            logDto.setBizName("批处理");
            bigLogService.updateLog(logDto);

        } else {
            group.setCreationTime(new Date());
            group.setModifyTime(new Date());
            etlJobService.save(group);
            //业务日志埋点  新增
            LogDto logDto = new LogDto();
            logDto.setBizCode("insert-group");
            logDto.setDescription("group " + group.getName());
            logDto.setNewData(group);
            logDto.setBizName("批处理组");
            bigLogService.insertLog(logDto);

        }

        //
        etlJobStepService.saveJobSteps(group.getId(), jobIds);

        // 如果是定时任务 需要增加到定时任务列表中去 定时任务指定某一台机器执行 通过参数配置
        if (group.getIsSchedule() == 1) {
            Json json = new Json();
            json.put("jobId", group.getId());
            json.put("cron", group.getScheduleParam());
            if (isAdd) {// 增加
                json.put("operation", "add");
            } else {// 修改
                json.put("operation", "modify");
            }
            RedisUtils.publish(EtlChannelConstant.ETL_GROUP_REDIS_CHANNEL,
                    json.toJSONString());
        } else {
            Json json = new Json();
            json.put("jobId", group.getId());
            json.put("cron", "");
            // 删除
            json.put("operation", "delete");
            RedisUtils.publish(EtlChannelConstant.ETL_GROUP_REDIS_CHANNEL,
                    json.toJSONString());
        }
    }

    @Override
    public boolean dealJob(EtlJob etlJob, String params) {

        ScheduledExecutorService moduleExec = Executors
                .newScheduledThreadPool(1);
        moduleExec.schedule(new GroupJobExecutor(etlJob, params), 3, TimeUnit.SECONDS);
        return true;


    }


    class GroupJobExecutor implements Runnable {
        private EtlJob etlJob;
        private String params;

        GroupJobExecutor(EtlJob etlJob, String params) {
            this.etlJob = etlJob;
            this.params = params;
        }

        @Override
        public void run() {
            List<EtlJobStep> steps = etlJobStepService.findEtlJobStepsByGroupId(etlJob.getId());
            long startTime = System.currentTimeMillis(); // 获取开始时间
            String errorMsg = "";
            try {
                boolean result = true;
                for (EtlJobStep step : steps) {
                    RedisUtils.del(step.getJobId());
                    etlJobService.dealEtlQuantzJob(step.getJobId());
                    String execResult = RedisUtils.get(step.getJobId());
                    int times = 1;
                    while (StringUtils.isBlank(execResult)) {
                        if (times == 3600) {
                            execResult = "false";
                            break;
                        }
                        execResult = RedisUtils.get(step.getJobId());
                        Thread.sleep(1000);
                    }
                    if ("false".equals(execResult)) {
                        result = false;
                        errorMsg = "批处理--"+step.getJobName()+"--执行失败,具体错误信息请到相应的批处理任务中查看";
                        break;
                    }
                }
                if (result) {
                    EtlJobLog log = assembledGroupLog(etlJob, startTime,
                            EtlJobLog.state_success, "执行成功,详细信息请查看具体的批处理");
                    etlJobLogService.save(log);
                    etlJobService.updateEtlJobStateById(etlJob.getId(),
                            EtlJob.STATE_TRUE, log.getId());
                } else {
                    EtlJobLog log = assembledGroupLog(etlJob, startTime,
                            EtlJobLog.state_fail, errorMsg);
                    etlJobLogService.save(log);
                    etlJobService.updateEtlJobStateById(etlJob.getId(),
                            EtlJob.STATE_ERROR, log.getId());
                }
            } catch (Exception e) {
                errorMsg = e.getMessage();
                if (errorMsg.length() > 2000) {
                    errorMsg = errorMsg.substring(0, 2000);
                }
                EtlJobLog log = assembledGroupLog(etlJob, startTime,
                        EtlJobLog.state_fail, errorMsg);
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_ERROR, log.getId());
            }
        }
    }

    private EtlJobLog assembledGroupLog(EtlJob etlJob, long startTime, int state,
                                        String logDescription) {
        EtlJobLog log = new EtlJobLog();
        log.setId(UuidUtils.generateUuid());
        log.setLogTime(new Date());
        log.setJobId(etlJob.getId());
        log.setName(etlJob.getName());
        log.setType("group");
        log.setUnitId(etlJob.getUnitId());
        log.setState(state);
        long endTime = System.currentTimeMillis();
        log.setDurationTime((endTime - startTime));
        log.setLogDescription(logDescription);
        return log;
    }
}
