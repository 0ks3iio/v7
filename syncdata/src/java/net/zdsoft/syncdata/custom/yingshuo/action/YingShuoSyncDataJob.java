package net.zdsoft.syncdata.custom.yingshuo.action;

import java.util.List;

import net.zdsoft.basedata.constant.custom.YingShuoConstant;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.yingshuo.service.YingShuoNewService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class YingShuoSyncDataJob implements Job {
	private Logger log = Logger.getLogger(YingShuoSyncDataJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
        log.info("-----------鹰硕基础数据的同步");
        YingShuoNewService ysService = (YingShuoNewService) Evn.getBean("yingShuoNewService");
        SchoolRemoteService schoolRemoteService = (SchoolRemoteService) Evn.getBean("schoolRemoteService");
        try {
        	long time1 = System.currentTimeMillis();
        	ysService.saveSchool();
        	ysService.saveDept();
        	ysService.saveClass();   
         	ysService.saveSchoolSemester();
        	ysService.saveCourse();
        	ysService.saveBuilding();
        	ysService.savePlace();
        	List<School> allSchool = SUtils.dt(schoolRemoteService.findByRegionCodes(YingShuoConstant.YS_REGION_CODE_VAL),
    				new TR<List<School>>() {});
        	if(CollectionUtils.isNotEmpty(allSchool)){
        		allSchool.forEach(c->{
        			ysService.saveTeacher(c.getId());
        			ysService.saveStudent(c.getId());
        		}); 
        	}
    		log.info("---------保存数据耗时：" + ((System.currentTimeMillis() - time1)) + "ms");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(">>>>任务结束！");
	}


}
