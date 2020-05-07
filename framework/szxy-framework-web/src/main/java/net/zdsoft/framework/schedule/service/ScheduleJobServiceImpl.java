package net.zdsoft.framework.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.schedule.component.QuartzJobAllowConcurrent;
import net.zdsoft.framework.schedule.component.QuartzJobDisallowConcurrent;
import net.zdsoft.framework.schedule.entity.ScheduleJobDomain;
import net.zdsoft.framework.utils.DateUtils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {
	private static final Logger log = LoggerFactory.getLogger(ScheduleJobServiceImpl.class);

	private Scheduler scheduler = getScheduler();
	private Scheduler getScheduler(){
    	scheduler = Evn.getScheduler();
    	if (scheduler == null) {
    		try {
    			scheduler= Evn.getSchedulerfactory().getScheduler();
			} catch (SchedulerException e) {
				log.error("获取scheduler失败"+e.getMessage());
			}
    	}
    	return  scheduler;
    }
    
    @Override
    public List<ScheduleJobDomain> getPlanJobs() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJobDomain> jobList = new ArrayList<>();
        for(JobKey jobKey  : jobKeys){
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers){
                ScheduleJobDomain job = (ScheduleJobDomain) trigger.getJobDataMap().get(ScheduleJobDomain.SCHEDULE_JOB_DOMAIN_KEY);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCron(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    @Override
    public List<ScheduleJobDomain> getRunningJobs() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJobDomain> jobList = new ArrayList<>();
        for (JobExecutionContext executingJob : executingJobs){
            Trigger trigger = executingJob.getTrigger();
            ScheduleJobDomain job = (ScheduleJobDomain) trigger.getJobDataMap().get(ScheduleJobDomain.SCHEDULE_JOB_DOMAIN_KEY);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCron(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    @Override
    public void pauseJob(ScheduleJobDomain scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(ScheduleJobDomain scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(ScheduleJobDomain scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        boolean ok = scheduler.deleteJob(jobKey);
        if(ok){
        	log.info("删除定时任务成功："+scheduleJob);
        }else{
        	log.info("删除定时任务失败："+scheduleJob);
        }
    }

    @Override
    public void runOnce(ScheduleJobDomain scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    @Override
    public void updateExpression(ScheduleJobDomain scheduleJob, String expression) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(),
                scheduleJob.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(expression);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                .withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
        log.info("修改定时任务表达式成功："+scheduleJob);
    }

    @Override
    public void addJob(ScheduleJobDomain scheduleJob) throws SchedulerException {
    	if(scheduleJob.isRunSingle() && !Evn.isScheduler()){
    		return;
    	}
        TriggerKey key = TriggerKey.triggerKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        Trigger trigger = scheduler.getTrigger(key);
        if(trigger == null){
        	JobDetail jobDetail = null;
            //在创建任务时如果不存在新建一个
        	if(scheduleJob.isAllowConcurrent()){
        		jobDetail = JobBuilder.newJob(QuartzJobAllowConcurrent.class)
        				.withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup()).build();
        	}else{
        		jobDetail = JobBuilder.newJob(QuartzJobDisallowConcurrent.class)
        				.withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup()).build();
        	}
            jobDetail.getJobDataMap().put(ScheduleJobDomain.SCHEDULE_JOB_DOMAIN_KEY,scheduleJob);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCron());
            //按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup())
                    .withSchedule(scheduleBuilder).startAt(DateUtils.addSecond(new Date(),scheduleJob.getDelaySecend())).build();
            trigger.getJobDataMap().put(ScheduleJobDomain.SCHEDULE_JOB_DOMAIN_KEY,scheduleJob);
            scheduler.scheduleJob(jobDetail,trigger);
            log.info("添加定时任务成功："+scheduleJob);
        }else{
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCron());
            trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put(ScheduleJobDomain.SCHEDULE_JOB_DOMAIN_KEY,scheduleJob);
            //重新执行
            scheduler.rescheduleJob(key,trigger);
            log.info("更新定时任务成功："+scheduleJob);
        }
    }

	@Override
	public void addJobList(List<ScheduleJobDomain> scheduleJobs) {
		for(ScheduleJobDomain domain:scheduleJobs){
			try {
				addJob(domain);
			} catch (SchedulerException e) {
				log.error("添加定时任务失败"+domain);
			}
		}
	}
}
