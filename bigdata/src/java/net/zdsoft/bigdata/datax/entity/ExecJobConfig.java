package net.zdsoft.bigdata.datax.entity;

import java.util.Date;

/**
 * Created by wangdongdong on 2019/4/29 14:16.
 */
public class ExecJobConfig {

    /**
     * job id
     */
    private String jobId;

    private String jobInstanceId;

    private String jobInsLogId;

    private String jobName;

    private String jobRemark;

    private String dataSourceId;

    private String dataSourceName;

    private String dataTargetId;

    private String dataTargetName;

    private String jobInsName;

    private String jobInsRemark;

    /**
     * 是否是增量同步
     */
    private Integer isIncrement;

    /**
     * 上次执行时间
     */
    private Date lastCommitTime;

    /**
     * 子任务
     */
    private ExecJobConfig child;

    /**
     * 服务器ip
     */
    private String host;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 端口
     */
    private Integer port;

    /**
     * job json
     */
    private String jobJson;

    /**
     * 执行命令路径
     */
    private String jobExecPath;

    /**
     * json保存的路径 FILE.PATH + jobInsId.json
     */
    private String jobJsonPath;

    /**
     * job日志保存路径
     */
    private String jobLogPath;

    /**
     * json保存的路径
     */
    private String jobJsonMainPath;


    public String getHost() {
        return host;
    }

    public ExecJobConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ExecJobConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ExecJobConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getJobJson() {
        return jobJson;
    }

    public ExecJobConfig setJobJson(String jobJson) {
        this.jobJson = jobJson;
        return this;
    }

    public String getJobExecPath() {
        return jobExecPath;
    }

    public ExecJobConfig setJobExecPath(String jobExecPath) {
        this.jobExecPath = jobExecPath;
        return this;
    }

    public String getJobJsonPath() {
        return jobJsonPath;
    }

    public ExecJobConfig setJobJsonPath(String jobJsonPath) {
        this.jobJsonPath = jobJsonPath;
        return this;
    }

    public String getJobId() {
        return jobId;
    }

    public ExecJobConfig setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobInstanceId() {
        return jobInstanceId;
    }

    public ExecJobConfig setJobInstanceId(String jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public ExecJobConfig setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobRemark() {
        return jobRemark;
    }

    public ExecJobConfig setJobRemark(String jobRemark) {
        this.jobRemark = jobRemark;
        return this;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public ExecJobConfig setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public ExecJobConfig setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

    public String getDataTargetId() {
        return dataTargetId;
    }

    public void setDataTargetId(String dataTargetId) {
        this.dataTargetId = dataTargetId;
    }

    public String getDataTargetName() {
        return dataTargetName;
    }

    public ExecJobConfig setDataTargetName(String dataTargetName) {
        this.dataTargetName = dataTargetName;
        return this;
    }

    public String getJobInsName() {
        return jobInsName;
    }

    public ExecJobConfig setJobInsName(String jobInsName) {
        this.jobInsName = jobInsName;
        return this;
    }

    public String getJobInsRemark() {
        return jobInsRemark;
    }

    public ExecJobConfig setJobInsRemark(String jobInsRemark) {
        this.jobInsRemark = jobInsRemark;
        return this;
    }

    public String getJobInsLogId() {
        return jobInsLogId;
    }

    public void setJobInsLogId(String jobInsLogId) {
        this.jobInsLogId = jobInsLogId;
    }

    public String getJobLogPath() {
        return jobLogPath;
    }

    public void setJobLogPath(String jobLogPath) {
        this.jobLogPath = jobLogPath;
    }

    public ExecJobConfig getChild() {
        return child;
    }

    public void setChild(ExecJobConfig child) {
        this.child = child;
    }

    public Integer getIsIncrement() {
        return isIncrement;
    }

    public void setIsIncrement(Integer isIncrement) {
        this.isIncrement = isIncrement;
    }

    public Date getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(Date lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    public String getJobJsonMainPath() {
        return jobJsonMainPath;
    }

    public void setJobJsonMainPath(String jobJsonMainPath) {
        this.jobJsonMainPath = jobJsonMainPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
