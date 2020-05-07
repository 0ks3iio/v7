package net.zdsoft.basedata.component;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.zdsoft.basedata.entity.ConstantBasedata;
import net.zdsoft.framework.config.Evn;

/**
 * @author shenke
 * @since 2017/2/7 14:24
 */
@Component
@Lazy(false)
public class ClassFlowJobManager{

    private static final Logger LOG = LoggerFactory.getLogger(ClassFlowImportJob.class);
    //@Autowired
    //private SysOptionService sysOptionService;

    @PostConstruct
    public void run(){

        if(Evn.isScheduler()){
            //SysOption sysOption = sysOptionService.findBy("iniid", ConstantBasedata.IMPORT_CLASS_FLOW_PERIOD);
            //SysOption sysOption = new SysOption();
            //sysOption.setNowValue();
            String period = Evn.getString(ConstantBasedata.IMPORT_CLASS_FLOW_PERIOD);
            if(StringUtils.isNotBlank(period)){
                int period2 = NumberUtils.toInt(period,60);
                LOG.info("[转班任务启动]");
                System.out.println("[转班任务启动]");
                JobDetail job = JobBuilder.newJob(ClassFlowImportJob.class).build();
                Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(period2)).startNow().build();
                Evn.addJob(job, trigger);
            }
        }
    }
}
