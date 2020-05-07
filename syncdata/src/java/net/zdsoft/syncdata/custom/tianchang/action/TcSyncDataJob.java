package net.zdsoft.syncdata.custom.tianchang.action;

import java.util.List;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.tianchang.constant.TcBaseConstant;
import net.zdsoft.syncdata.custom.tianchang.service.TcSyncService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class TcSyncDataJob implements Job {
	private Logger log = Logger.getLogger(TcSyncDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
        log.info("-----------天长基础数据的同步");
        TcSyncService tcSyncService = (TcSyncService) Evn.getBean("tcSyncService");
        SchoolRemoteService schoolRemoteService = (SchoolRemoteService) Evn.getBean("schoolRemoteService");
        try {
        	long time1 = System.currentTimeMillis();
        	tcSyncService.saveEdu();
        	tcSyncService.saveEduTeacher();
        	tcSyncService.saveSchool();
        	tcSyncService.saveClass();
        	tcSyncService.saveTeacher();
        	List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(TcBaseConstant.TC_REGION_CODE_VAL),
    				new TR<List<School>>() {});
        	if(CollectionUtils.isNotEmpty(allSchool)){
        		allSchool.forEach(c->{
        			tcSyncService.saveStudent(c.getId());
        		});    
        	}
    		log.info("---------推送数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}


}
