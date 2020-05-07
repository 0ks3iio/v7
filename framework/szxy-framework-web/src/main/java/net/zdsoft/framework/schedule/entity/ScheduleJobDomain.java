package net.zdsoft.framework.schedule.entity;


public class ScheduleJobDomain {
	public static final String SCHEDULE_JOB_DOMAIN_KEY = "schedule.job.domian.key";
	
	private String jobGroup;    //任务组
    private String jobName;  //任务名称
    private String showName;  //用于展示时显示的名称
    /** 任务状态 */
    private String jobStatus;
    private boolean allowConcurrent = false;//同一job,上一次未执行结束，是否允许执行下一次---默认false
    private boolean runSingle = true;//只在配置定时器参数的那台跑--默认true

    private String cron;   //cron表达式
    private int delaySecend = 0; //多少秒后执行,正整数
    private BaseJobTask task;   //执行的job的接口

	public String getJobGroup() {
		return jobGroup;
	}

	public ScheduleJobDomain setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
		return this;
	}

	public String getJobName() {
		return jobName;
	}

	public ScheduleJobDomain setJobName(String jobName) {
		this.jobName = jobName;
		return this;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public ScheduleJobDomain setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
		return this;
	}

	public String getCron() {
		return cron;
	}

	public ScheduleJobDomain setCron(String cron) {
		this.cron = cron;
		return this;
	}

	public BaseJobTask getTask() {
		return task;
	}

	public ScheduleJobDomain setTask(BaseJobTask task) {
		this.task = task;
		return this;
	}

	public boolean isAllowConcurrent() {
		return allowConcurrent;
	}

	public ScheduleJobDomain setAllowConcurrent(boolean allowConcurrent) {
		this.allowConcurrent = allowConcurrent;
		return this;
	}

	public boolean isRunSingle() {
		return runSingle;
	}

	public ScheduleJobDomain setRunSingle(boolean runSingle) {
		this.runSingle = runSingle;
		return this;
	}

	public int getDelaySecend() {
		return delaySecend;
	}

	public ScheduleJobDomain setDelaySecend(int delaySecend) {
		this.delaySecend = delaySecend;
		return this;
	}

	public String getShowName() {
		return showName;
	}

	public ScheduleJobDomain setShowName(String showName) {
		this.showName = showName;
		return this;
	}

	@Override
	public String toString() {
		return "ScheduleJobDomain [jobGroup=" + jobGroup + ", jobName="
				+ jobName + ", showName=" + showName + ", jobStatus="
				+ jobStatus + ", allowConcurrent=" + allowConcurrent
				+ ", runSingle=" + runSingle + ", cron=" + cron
				+ ", delaySecend=" + delaySecend + ", task=" + task + "]";
	}
}
