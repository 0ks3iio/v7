package net.zdsoft.api.openapi.pushjob.schedule;
//package net.zdsoft.api.openapi.pushjob.schedule;
//
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import net.zdsoft.api.base.entity.eis.OpenApiPushJob;
//import net.zdsoft.api.openapi.pushjob.service.BasePushJobService;
//import net.zdsoft.framework.config.Evn;
//import net.zdsoft.framework.schedule.entity.ScheduleJobDomain;
//import net.zdsoft.framework.schedule.service.ScheduleJobService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
//import com.google.common.collect.Lists;
//
//@Component
//@Lazy(false)
//public class OpenApiAddJob {
//	@Autowired
//	private ScheduleJobService scheduleJobService;
//	@Autowired
//	private BasePushJobService basePushJobService;
//	@PostConstruct
//	public void addJob() {
//		//开启定时参数的 服务上添加job
//		if (Evn.isScheduler()) {
//			List<ScheduleJobDomain> domains = Lists.newArrayList();
//			List<OpenApiPushJob> pushJobs = basePushJobService.getBasePushJobAll();
//			for(OpenApiPushJob job:pushJobs){
//				ScheduleJobDomain scheduleJobDomain = new ScheduleJobDomain()
//				.setCron(job.getCron())
//				.setJobGroup(job.getTicketKey())
//				.setJobName(job.getId())
//				.setDelaySecend(60)//启动一分钟后开始
//				.setShowName("openapi-"+job.getDataType())
//				.setTask(new OpenApiJobTask());
//				domains.add(scheduleJobDomain);
//			}
//			scheduleJobService.addJobList(domains);
//		}
//	}
//}
