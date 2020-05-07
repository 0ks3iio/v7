package net.zdsoft.framework.schedule.service;

import java.util.List;

import net.zdsoft.framework.schedule.entity.ScheduleJobDomain;

import org.quartz.SchedulerException;


public interface ScheduleJobService {
	//获取所有计划中的任务
    List<ScheduleJobDomain> getPlanJobs() throws SchedulerException;

    //获取所有运行中的任务
    List<ScheduleJobDomain> getRunningJobs() throws SchedulerException;

    //暂停任务
    void pauseJob(ScheduleJobDomain scheduleJob) throws SchedulerException;

    //恢复任务
    void resumeJob(ScheduleJobDomain scheduleJob) throws SchedulerException;

    //删除任务
    void deleteJob(ScheduleJobDomain scheduleJob) throws SchedulerException;

    //立即运行任务 ,只会运行一次
    void runOnce(ScheduleJobDomain scheduleJob) throws SchedulerException;

    //更新任务的时间表达式
    void updateExpression(ScheduleJobDomain job,String expression) throws SchedulerException;

    //添加任务
    void addJob(ScheduleJobDomain scheduleJob) throws SchedulerException;
    //添加任务列表
    void addJobList(List<ScheduleJobDomain> scheduleJobs);
}
