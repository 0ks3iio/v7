package net.zdsoft.basedata.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.zdsoft.basedata.service.TaskRecordService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
@DisallowConcurrentExecution
public class TaskJobDeleteRun implements Job {
	
	private static final Logger log = Logger.getLogger(TaskJobDeleteRun.class);
	
    private TaskRecordService taskRecordService;
    
    public TaskRecordService getTaskRecordService(){
    	if(taskRecordService == null){
    		taskRecordService = Evn.getBean("taskRecordServiceImpl");
    	}
    	return taskRecordService;
    }
    
	@Override
	public void execute(JobExecutionContext jobec) throws JobExecutionException {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss"); 
		log.info("------进入删除任务调度"+df.format(new Date()));
		try{
			getTaskRecordService().deleteOld();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
