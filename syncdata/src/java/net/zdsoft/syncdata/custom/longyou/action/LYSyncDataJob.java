package net.zdsoft.syncdata.custom.longyou.action;
import java.util.List;

import net.zdsoft.basedata.constant.custom.LyConstant;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.longyou.service.LYdySyncService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class LYSyncDataJob implements Job {
    private Logger log = Logger.getLogger(LYSyncDataJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.info("任务启动！");
        log.info("-----------鼎勇数据同步");
        LYdySyncService lydySyncService = (LYdySyncService) Evn.getBean("lydySyncService");
        UnitRemoteService unitRemoteService = (UnitRemoteService) Evn.getBean("unitRemoteService");
        try {
        	//小初高
        	long time1 = System.currentTimeMillis();
        	lydySyncService.saveEduUnit("jaoyuju","14f7534d-0990-4f27-a053-b12f943a4a2f","JYJ"); //教育局
        	lydySyncService.saveUnit("XCGXX", "a6c133f7-d249-409b-b1a2-ec40d5ed36f2", "XCGXX"); //小初高学校
        	lydySyncService.saveUnit("XQXX", "8bde7ba6-063d-40fb-9b70-296a7bcb1c78", "XQXX"); //学前学校
        	lydySyncService.saveUnit("ZZXX", "f51fb0dc-2fdb-40d0-a238-dc6ccc2270cf", "ZZXX"); //中职学校
        	lydySyncService.saveSubSchool("XCGXQ", "83264af7-5c97-43f1-bc9b-37dee6e99e1a");  //小初高校区
        	lydySyncService.saveDept("BMZ","62fc83f7-6f63-4911-8288-6e0715cbba1a","BMZ");  //部门组    需要学校id
        	
        	lydySyncService.saveTeacher("JYJJS", "b40204bc-8b0e-48f2-aa7c-028840d28cd3", "JYJJS");  //教育局教师
        	lydySyncService.saveTeacher("XCGJS", "472eff65-904b-4c9c-9f2e-6ef4bc439358", "XCGJS"); //小初高教师
        	lydySyncService.saveTeacher("XQJS", "69f841fd-d0cf-4a4a-902b-67b1113715f8", "XQJS"); //学前教师
        	lydySyncService.saveTeacher("ZZJS", "6333213f-482c-43b9-90a8-43c7fc948f85", "ZZJS"); //中职教师
        	
    		List<Unit> allSchool = SUtils.dt(unitRemoteService.findByUnitClassAndRegion(Unit.UNIT_CLASS_SCHOOL,
    				LyConstant.LY_DEFAULT_REGION_CODE), new TR<List<Unit>>() {});
        	if(CollectionUtils.isNotEmpty(allSchool)){
        		allSchool.forEach(c->{
        			try {
	        			if(Unit.UNIT_SCHOOL_KINDERGARTEN == c.getUnitType()){
	        				lydySyncService.saveClass("XQ", "5762465a-872f-4af6-bb7c-df42bfd136e5", "XQ" , c.getId());//学前班级
							lydySyncService.saveStudent("XQXS", "d8624e8f-ba62-41dc-b76a-c8bb82b55ddf", "XQXS", c.getId());//学前学生
	        			}else{
							lydySyncService.saveClass("XCGBJ", "4347b7f6-89e2-48ab-bbd2-ed61447967fc", "XCGBJ" , c.getId());//小初高班级
							lydySyncService.saveStudent("XCGXS", "7130b45f-b3fb-4542-a3e9-ad93a573ad4e", "XCGXS", c.getId());//小初高学生
	        			}
        			} catch (Exception e) {
						e.printStackTrace();
					} 
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

