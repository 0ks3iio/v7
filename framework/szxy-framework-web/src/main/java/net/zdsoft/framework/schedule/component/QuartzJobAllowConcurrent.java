package net.zdsoft.framework.schedule.component;


import net.zdsoft.framework.schedule.entity.ScheduleJobDomain;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzJobAllowConcurrent implements Job {
	private static final Logger log = LoggerFactory.getLogger(QuartzJobAllowConcurrent.class);
    public void execute(JobExecutionContext context) 
            throws JobExecutionException{
    	ScheduleJobDomain job = (ScheduleJobDomain) context.getMergedJobDataMap().get(ScheduleJobDomain.SCHEDULE_JOB_DOMAIN_KEY);
    	long start = System.currentTimeMillis();
    	log.info("开始执行任务："+job);
    	if(job!=null && job.getTask()!=null){
    		job.getTask().excute(job.getJobGroup(),job.getJobName());
    	}
    	long end = System.currentTimeMillis();
    	log.info("结束执行任务："+job+",耗时："+(end-start));
    }
}
