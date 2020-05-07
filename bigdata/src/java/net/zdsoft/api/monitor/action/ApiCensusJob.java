package net.zdsoft.api.monitor.action;

import net.zdsoft.api.monitor.service.ApiCensusJobService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class ApiCensusJob implements Job{
	private Logger log = Logger.getLogger(ApiCensusJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("统计任务启动！");
		ApiCensusJobService apiService = (ApiCensusJobService) Evn.getBean("apiCensusService");
        try {
        	long time1 = System.currentTimeMillis();
        	apiService.doCensus();
    		log.info("---------保存数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}

}
