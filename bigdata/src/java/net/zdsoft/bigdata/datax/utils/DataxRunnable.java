package net.zdsoft.bigdata.datax.utils;

import com.jcraft.jsch.*;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.DataxJobIns;
import net.zdsoft.bigdata.datax.entity.DataxJobInsLog;
import net.zdsoft.bigdata.datax.entity.ExecJobConfig;
import net.zdsoft.bigdata.datax.enums.DataxJobResultEnum;
import net.zdsoft.bigdata.datax.enums.DataxJobStatusEnum;
import net.zdsoft.bigdata.datax.service.DataxJobInsLogService;
import net.zdsoft.bigdata.datax.service.DataxJobInsService;
import net.zdsoft.framework.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by wangdongdong on 2019/4/29 17:36.
 */
public class DataxRunnable implements Runnable {


    private static final Logger logger = LoggerFactory.getLogger(DataxRunnable.class);

    private String UTF8 = "UTF-8";

    private Session session = null;

    private ExecJobConfig execJobConfig;

    public DataxRunnable(ExecJobConfig execJobConfig) {
        this.execJobConfig = execJobConfig;
    }

    @Override
    public void run() {
        try {
            // 保存执行状态日志
            DataxJobInsLog dataxJobInsLog = new DataxJobInsLog();
            BeanUtils.copyProperties(execJobConfig, dataxJobInsLog);
            dataxJobInsLog.setStatus(DataxJobStatusEnum.EXECUTING.getCode());
            dataxJobInsLog.setStartTime(new Date());
            getDataxJobInstanceLogService().saveDataxJobInstanceLog(dataxJobInsLog);
            execJobConfig.setJobInsLogId(dataxJobInsLog.getId());
            execJob(execJobConfig);
        } catch (BigDataBusinessException e) {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
            String log = e.getMessage();
            if (log.length() > 299) {
                log = log.substring(0, 299);
            }
            // 保存执行失败日志
            DataxJobInsLog dataxJobInsLog = getDataxJobInstanceLogService().findOne(execJobConfig.getJobInsLogId());
            if (dataxJobInsLog != null) {
                dataxJobInsLog.setStatus(DataxJobStatusEnum.COMPLETE.getCode());
                dataxJobInsLog.setResult(DataxJobResultEnum.FAILED.getCode());
                dataxJobInsLog.setEndTime(new Date());
                dataxJobInsLog.setJobInsRemark(log);
                getDataxJobInstanceLogService().saveDataxJobInstanceLog(dataxJobInsLog);
            } else {
                dataxJobInsLog = new DataxJobInsLog();
                BeanUtils.copyProperties(execJobConfig, dataxJobInsLog);
                dataxJobInsLog.setStatus(DataxJobStatusEnum.COMPLETE.getCode());
                dataxJobInsLog.setResult(DataxJobResultEnum.FAILED.getCode());
                dataxJobInsLog.setEndTime(new Date());
                dataxJobInsLog.setStartTime(new Date());
                dataxJobInsLog.setJobInsRemark(log);
                getDataxJobInstanceLogService().saveDataxJobInstanceLog(dataxJobInsLog);
            }
        }
    }

    public void execJob(ExecJobConfig execJobConfig) throws BigDataBusinessException {
        // 初始化连接会话
        initSession(execJobConfig);
        // json转成文件保存
        uploadJobJson(execJobConfig);
        try {

            Date nowTime = new Date();

            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            InputStream in = channel.getInputStream();
            channel.setPty(true);
            channel.connect();
            OutputStream os = channel.getOutputStream();
            StringBuilder command = new StringBuilder("python ");

            command.append(execJobConfig.getJobExecPath())
                    .append(" ")
                    .append(execJobConfig.getJobJsonPath());

            // 判断是否是增量同步
            if (execJobConfig.getIsIncrement() != null && execJobConfig.getIsIncrement() == 1) {
                Date startTime = execJobConfig.getLastCommitTime();
                // 开始时间
                if (startTime == null) {
                    startTime = new Date(0);
                }
                // 2018-07-13 14:31:00
                command.append(" -p ")
                        .append("\"-Dstart_time=")
                        .append("'" + DateUtils.date2StringBySecond(startTime) + "'")
                        .append(" -Dend_time=")
                        .append("'" + DateUtils.date2StringBySecond(nowTime) + "'")
                        .append("\"");
            }

            command.append(" | tee ")
                    .append(execJobConfig.getJobLogPath() + "/" + execJobConfig.getJobInsLogId() + ".log").append("\n");

            os.write(command.toString().getBytes());
            os.flush();

            byte[] tmp = new byte[1024];

            DataxJobInsLog dataxJobInsLog = getDataxJobInstanceLogService().findOne(execJobConfig.getJobInsLogId());

            while (true) {
                if (in.available() > 0) {

                    int i = in.read(tmp, 0, 1024);
                    if (i <= 0) {
                        break;
                    }
                    String s = new String(tmp, 0, i, UTF8);
                    if (s.contains("读写失败总数")) {
                        logger.info(execJobConfig.getJobName() + " 执行结束=================================");
                        String[] rows = s.split("\r\n");
                        for (String row : rows) {
                            if (row.contains("任务启动时刻")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                Date startTime = DateUtils.string2DateTime(StringUtils.trim(info));
                                dataxJobInsLog.setStartTime(startTime);
                            }
                            if (row.contains("任务结束时刻")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                Date endTime = DateUtils.string2DateTime(StringUtils.trim(info));
                                dataxJobInsLog.setEndTime(endTime);
                            }
                            if (row.contains("任务总计耗时")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                dataxJobInsLog.setDuration(Long.valueOf(StringUtils.trim(info).replace("s", "")));
                            }
                            if (row.contains("任务平均流量")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                dataxJobInsLog.setAverageFlow(StringUtils.trim(info));
                            }
                            if (row.contains("记录写入速度")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                dataxJobInsLog.setWriteSpeed(StringUtils.trim(info));
                            }
                            if (row.contains("读出记录总数")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                dataxJobInsLog.setTotalCount(Long.valueOf(StringUtils.trim(info)));
                            }
                            if (row.contains("读写失败总数")) {
                                String info = row.substring(row.indexOf(":") + 1);
                                dataxJobInsLog.setErrorCount(Long.valueOf(StringUtils.trim(info)));
                                dataxJobInsLog.setSuccessCount(dataxJobInsLog.getTotalCount() - dataxJobInsLog.getErrorCount());
                            }
                        }
                        // 保存成功日志
                        dataxJobInsLog.setStatus(DataxJobStatusEnum.COMPLETE.getCode());
                        dataxJobInsLog.setResult(DataxJobResultEnum.SUCCESS.getCode());
                        getDataxJobInstanceLogService().saveDataxJobInstanceLog(dataxJobInsLog);
                        // 保存
                        DataxJobIns dataxJobIns = new DataxJobIns();
                        dataxJobIns.setLastCommitTime(nowTime);
                        getDataxJobInService().update(dataxJobIns, execJobConfig.getJobInstanceId(), new String[]{"lastCommitTime"});

                        // 执行子任务
                        if (execJobConfig.getChild() != null) {
                            DataxUtil.execJob(execJobConfig.getChild());
                        }
                        break;
                    }

                    if (s.contains("com.alibaba.datax.common.exception")) {
                        throw new BigDataBusinessException(s);
                    }

                    if (s.contains("Could not find or load main class")) {
                        throw new BigDataBusinessException(s);
                    }

                    if (s.contains("No such file or directory")) {
                        throw new BigDataBusinessException(s);
                    }
                }

                if (channel.isClosed()) {
                    logger.info("exit-status: " + channel.getExitStatus());
                    break;
                }
            }
            os.close();
            in.close();
            channel.disconnect();
        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
            throw new BigDataBusinessException(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new BigDataBusinessException(e.getMessage());
        } finally {
            session.disconnect();
        }
    }

    public void initSession(ExecJobConfig execJobConfig) throws BigDataBusinessException {
        try {
            if (session == null || !session.isConnected()) {
                JSch jsch = new JSch();
                InetAddress address = InetAddress.getLocalHost();
                if (execJobConfig.getHost().equals(address.getHostAddress())) {
                    session = jsch.getSession("127.0.0.1");
                } else {
                    // Create and connect session.
                    session = jsch.getSession(execJobConfig.getUsername(), execJobConfig.getHost(), execJobConfig.getPort());
                    session.setPassword(execJobConfig.getPassword());
                    //去掉连接确认的
                    session.setConfig("StrictHostKeyChecking", "no");
                }
                session.connect(30000);
            }
        } catch (JSchException e) {
            logger.error("初始化linux连接出错,请检查用户名密码是否正确:" + e.getMessage(), e);
            throw new BigDataBusinessException("初始化linux连接出错,请检查用户名密码是否正确:" + e.getMessage());
        } catch (UnknownHostException e) {
            logger.error("初始化linux连接出错:" + e.getMessage(), e);
            throw new BigDataBusinessException("初始化linux连接出错:" + e.getMessage());
        }

    }

    public void uploadJobJson(ExecJobConfig execJobConfig) throws BigDataBusinessException {
        if (!session.isConnected()) {
            initSession(execJobConfig);
        }
        try {
            ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(execJobConfig.getJobJson().getBytes(UTF8));
            channelSftp.connect();
            checkPath(channelSftp, execJobConfig);
            channelSftp.setFilenameEncoding(UTF8);
            channelSftp.put(inputStream, execJobConfig.getJobJsonPath(), ChannelSftp.OVERWRITE);
            channelSftp.disconnect();
        } catch (Exception e) {
            logger.error("上传json出错:" + e.getMessage(), e);
            throw new BigDataBusinessException("上传json出错,请检查路径配置（"+ execJobConfig.getJobJsonPath() +"）是否正确:" + e.getMessage());
        }
    }

    /**
     * 检测日志和json路径是否存在，不存在则新建路径
     * @param channelSftp
     */
    private void checkPath(ChannelSftp channelSftp, ExecJobConfig execJobConfig) throws SftpException {
        checkPath(channelSftp, execJobConfig.getJobJsonMainPath());
        checkPath(channelSftp, execJobConfig.getJobLogPath());
    }

    private void checkPath(ChannelSftp channelSftp, String path) throws SftpException {
        String[] folders = StringUtils.split(path, "/");
        int i = 1;
        // 检测路径是否存在，不存在新建
        for (String folder : folders) {
            if (i++ == 1) {
                folder = "/" + folder;
            }
            try {
                channelSftp.cd(folder);
            } catch (SftpException e) {
                if(ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id){
                    channelSftp.mkdir(folder);
                    channelSftp.cd(folder);
                }
            }
        }
    }

    private DataxJobInsLogService getDataxJobInstanceLogService() {
        return DataxUtil.getBean(DataxJobInsLogService.class);
    }

    private DataxJobInsService getDataxJobInService() {
        return DataxUtil.getBean(DataxJobInsService.class);
    }
}
