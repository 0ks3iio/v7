package net.zdsoft.basedata.component;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.zdsoft.basedata.entity.ConstantBasedata;
import net.zdsoft.framework.config.Evn;

@Component
@Lazy(false)
public class StudentFlowJobManager {

    private static final Logger log = Logger.getLogger(StudentFlowJobManager.class);

    @PostConstruct public void run() {
        if (Evn.isScheduler()) {
            if (StringUtils.isNotBlank(Evn.getString(ConstantBasedata.IMPORT_STU_FLOW_OUT_PERIOD))) {
                log.info("------学生异动调出任务启动");
                JobDetail job = JobBuilder.newJob(StudentOutImportJob.class).build();
                Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
                        SimpleScheduleBuilder.repeatSecondlyForever(Evn.getInt(ConstantBasedata.IMPORT_STU_FLOW_OUT_PERIOD))).startNow().build();
                Evn.addJob(job, trigger);
            }
            if(StringUtils.isNotBlank(Evn.getString(ConstantBasedata.IMPORT_STU_FLOW_IN_PERIOD))){
            	log.info("------学生异动调入任务启动");
            	 JobDetail job = JobBuilder.newJob(StudentInImportJop.class).build();
                 Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
                         SimpleScheduleBuilder.repeatSecondlyForever(Evn.getInt(ConstantBasedata.IMPORT_STU_FLOW_IN_PERIOD))).startNow().build();
                 Evn.addJob(job, trigger);
            }
        }
    }
}
