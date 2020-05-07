package net.zdsoft.syncdata.custom.uc.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.uc.constant.UcConstant;
import net.zdsoft.syncdata.custom.uc.service.UcSyncService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class UcSyncDataJob implements Job {
    private Logger log = Logger.getLogger(UcSyncDataJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.info("任务启动！");
        log.info("-----------数据同步到用户中心");
        UcSyncService ucSyncService = (UcSyncService) Evn.getBean("ucSyncService");
        try {
        	long time1 = System.currentTimeMillis();
        	ucSyncService.saveDate(UcConstant.UC_MSYK_AP_CODE,UcConstant.UC_MSYK_VERIFY_KEY,UcConstant.UC_MSYK_NONCE_STR);
    		log.info("---------推送数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
    }
}
