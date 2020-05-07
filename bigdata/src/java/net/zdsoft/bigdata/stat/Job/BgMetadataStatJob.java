package net.zdsoft.bigdata.stat.Job;

import net.zdsoft.bigdata.stat.service.BgMetadataStatService;
import net.zdsoft.framework.config.Evn;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BgMetadataStatJob implements Job {
    private Logger log = Logger.getLogger(BgMetadataStatJob.class);

    // 每天0点30分执行一次    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.debug("元数据统计......");
        BgMetadataStatService bgMetadataStatService = (BgMetadataStatService) Evn
                .getBean("bgMetadataStatService");
        bgMetadataStatService.metadataStatByDaily(null);
    }
}
