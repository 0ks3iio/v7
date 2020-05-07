package net.zdsoft.bigdata.stat.Job;

import net.zdsoft.bigdata.stat.service.BgSysStatService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BgSysStatJob implements Job {
	private Logger log = Logger.getLogger(BgSysStatJob.class);

	// 每一小时执行一次    
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.debug(">>>>大数据后台统计计算任务开始！");

		BgSysStatService bgSysStatService = (BgSysStatService) Evn
				.getBean("bgSysStatService");

		log.debug(">>>>大数据概览统计计算任务开始！");
		bgSysStatService.summaryStat();

		log.debug(">>>>大数据用量统计计算任务开始！");
		bgSysStatService.usageStat();

		log.debug(">>>>大数据模块使用计算任务开始！");
		bgSysStatService.moduleStat();

		log.debug(">>>>大数据任务运行计算任务开始！");
		bgSysStatService.jobStat();

		log.debug(">>>>大数据log计算任务开始！");
		bgSysStatService.logStat();

		log.debug(">>>>节点监听任务开始！");
		bgSysStatService.nodeMonitoring();
		
		log.debug(">>>>大数据后台统计计算任务结束！");
	}
}
