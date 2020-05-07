package net.zdsoft.bigdata.taskScheduler.service.impl;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.taskScheduler.service.FlinkJobService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("flinkJobService")
public class FlinkJobServiceImpl implements FlinkJobService {
    private Logger logger = Logger.getLogger(FlinkJobServiceImpl.class);

    @Autowired
    private EtlJobService etlJobService;

    @Autowired
    private EtlJobLogService etlJobLogService;

    @Autowired
    SysOptionRemoteService sysOptionRemoteService;

    @Autowired
    private OptionService optionService;

    @Resource
    private BigLogService bigLogService;

    @Override
    public void saveFlinkJob(EtlJob flink,Integer type) {
        boolean isAdd = false;

        String systemFilePath = sysOptionRemoteService
                .findValue(Constant.FILE_PATH);
        //systemFilePath = "d://test";
        String realFilePath = systemFilePath + File.separator + "flink"
                + File.separator + flink.getUnitId();

        // 处理文件
        String tempFileFullPath = flink.getPath() + File.separator
                + flink.getFileName();

        String realFileFullPath = realFilePath + File.separator
                + flink.getFileName();
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
        // 处理业务
        flink.setPath(realFilePath);
        if (flink.getIsSchedule() == 1) {
            flink.setHasParam(0);
        } else {
            flink.setScheduleParam("");
        }

        if (StringUtils.isBlank(flink.getId())) {
            isAdd = true;
            flink.setId(UuidUtils.generateUuid());
        }

        //建立Job和来源去向的关系
        etlJobService.saveEtlJobRelations(flink);

        if (!isAdd) {
            flink.setModifyTime(new Date());
            EtlJob oldFlink = etlJobService.findOne(flink.getId());
            etlJobService.update(flink, flink.getId(), new String[]{"name",
                    "etlType", "unitId", "jobType", "path", "isSchedule",
                    "fileName", "scheduleParam", "remark", "sourceId", "targetId", "jobCode","sourceType", "targetType", "modifyTime", "flowChartJson"});
            //业务日志埋点  修改
            LogDto logDto = new LogDto();
            logDto.setBizCode("update-flink");
            logDto.setDescription("flink " + oldFlink.getName());
            logDto.setOldData(oldFlink);
            logDto.setNewData(flink);
            if (type==1){
                logDto.setBizName("批处理");
            }else {
                logDto.setBizName("流计算");
            }
            bigLogService.updateLog(logDto);

        } else {
            flink.setCreationTime(new Date());
            flink.setModifyTime(new Date());
            etlJobService.save(flink);
            //业务日志埋点  新增
            LogDto logDto = new LogDto();
            logDto.setBizCode("insert-flink");
            logDto.setDescription("flink " + flink.getName());
            logDto.setNewData(flink);
            if (type==1){
                logDto.setBizName("批处理");
            }else {
                logDto.setBizName("流计算");
            }
            bigLogService.insertLog(logDto);


        }
        // 如果是定时任务 需要增加到定时任务列表中去 定时任务指定某一台机器执行 通过参数配置
        if (flink.getEtlType() == EtlType.FLINK.getValue()) {
            if (flink.getIsSchedule() == 1) {
                Json json = new Json();
                json.put("jobId", flink.getId());
                json.put("cron", flink.getScheduleParam());
                if (isAdd) {// 增加
                    json.put("operation", "add");
                } else {// 修改
                    json.put("operation", "modify");
                }
                RedisUtils.publish(EtlChannelConstant.ETL_FLINK_REDIS_CHANNEL,
                        json.toJSONString());
            } else {
                Json json = new Json();
                json.put("jobId", flink.getId());
                json.put("cron", "");
                // 删除
                json.put("operation", "delete");
                RedisUtils.publish(EtlChannelConstant.ETL_FLINK_REDIS_CHANNEL,
                        json.toJSONString());
            }
        }
    }

    @Override
    public boolean dealJob(EtlJob etlJob, String params) {
        ScheduledExecutorService moduleExec = Executors
                .newScheduledThreadPool(1);
        moduleExec.schedule(new FlinkJobExecutor(etlJob, params), 3, TimeUnit.SECONDS);
        return true;

    }

    private EtlJobLog assembledFlinkLog(EtlJob etlJob, long startTime, int state,
                                        String logDescription) {
        EtlJobLog log = new EtlJobLog();
        log.setId(UuidUtils.generateUuid());
        log.setLogTime(new Date());
        log.setJobId(etlJob.getId());
        log.setName(etlJob.getName());
        log.setType("flink");
        log.setUnitId(etlJob.getUnitId());
        log.setState(state);
        long endTime = System.currentTimeMillis();
        log.setDurationTime((endTime - startTime));
        log.setLogDescription(logDescription);
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

    private static final String ENCODING = "UTF-8";

    private String execCommandByJSch(OptionDto option, String command) throws IOException, JSchException {

        JSch jsch = new JSch();

        Session session = jsch.getSession(option.getFrameParamMap().get("username"), option.getFrameParamMap().get("domain"), Integer.valueOf(option.getFrameParamMap().get("port")));
        session.setPassword(option.getFrameParamMap().get("password"));
        session.setConfig("StrictHostKeyChecking", "no");//第一次访问服务器不用输入yes
        session.setTimeout(60 * 60 * 1000);
        session.connect();

        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        InputStream in = channel.getInputStream();
        channel.setPty(true);
        channel.connect();
        OutputStream os = channel.getOutputStream();
        os.write((command + "\r\n").getBytes());
        os.flush();

        StringBuffer s = new StringBuffer();
        boolean end = false;

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(ENCODING)));

        String buf = null;
        while (true) {
            while ((buf = reader.readLine()) != null) {
                s.append(buf).append("<br>");
                if (buf.indexOf("---finished---") >= 0) {
                    end = true;
                    break;
                }
            }
            if (end)
                break;
        }

        os.close();
        in.close();
        reader.close();
        channel.disconnect();
        session.disconnect();

        return s.toString();
    }

    class FlinkJobExecutor implements Runnable {
        private EtlJob etlJob;
        private String params;

        FlinkJobExecutor(EtlJob etlJob, String params) {
            this.etlJob = etlJob;
            this.params = params;
        }

        @Override
        public void run() {
            String filePath = etlJob.getPath() + File.separator
                    + etlJob.getFileName();
//            filePath="/opt/app/flink-1.7.2/data/WordCount.jar";
            long startTime = System.currentTimeMillis(); // 获取开始时间
            try {
                OptionDto flinkDto = optionService.getAllOptionParam("flink");

                StringBuffer assembeldCmd = new StringBuffer();
                assembeldCmd.append(flinkDto.getFrameParamMap().get("path")).append("/bin/flink run ").append(filePath);
                //bin/flink run -c net.jf.flink.stream.kafka.kafka2EsDemo data/Demo-1.0-SNAPSHOT.jar

                //bin/flink run -c net.jf.flink.stream.kafka.kafka2EsDemo data/Demo-1.0-SNAPSHOT.jar
                String msg = execCommandByJSch(flinkDto, assembeldCmd.toString());

                EtlJobLog log = assembledFlinkLog(etlJob, startTime, EtlJobLog.state_success,
                        msg);
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_TRUE, log.getId());
                RedisUtils.set(etlJob.getId(),"true");
            } catch (Exception e) {
                e.printStackTrace();
                EtlJobLog log = assembledFlinkLog(etlJob, startTime, EtlJobLog.state_fail,
                        e.getMessage());
                etlJobLogService.save(log);
                etlJobService.updateEtlJobStateById(etlJob.getId(),
                        EtlJob.STATE_ERROR, log.getId());
                RedisUtils.set(etlJob.getId(),"false");
            }
        }
    }

}
