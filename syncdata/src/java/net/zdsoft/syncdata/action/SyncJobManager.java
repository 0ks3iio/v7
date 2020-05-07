package net.zdsoft.syncdata.action;

import java.util.Date;

import javax.annotation.PostConstruct;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.changzhi.action.ChangZhiSyncDataJob;
import net.zdsoft.syncdata.custom.dongguan.action.DongGuanDataJob;
import net.zdsoft.syncdata.custom.gansu.action.GanSuSyncDataJob;
import net.zdsoft.syncdata.custom.laibin.action.LaiBinDataJob;
import net.zdsoft.syncdata.custom.longyou.action.LYSyncDataJob;
import net.zdsoft.syncdata.custom.tianchang.action.TcSyncDataJob;
import net.zdsoft.syncdata.custom.uc.action.UcSyncDataJob;
import net.zdsoft.syncdata.custom.xunfei.action.HsxfSyncDataJob;
import net.zdsoft.syncdata.custom.yaohai.action.YhBaseSyncDataJob;
import net.zdsoft.syncdata.custom.yingshuo.action.YingShuoSyncDataJob;
import net.zdsoft.syncdata.custom.zy.action.ZyCardSyncDataJob;
import net.zdsoft.syncdata.entity.ConstantSyncData;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component
public class SyncJobManager {
	private Logger log = Logger.getLogger(SyncJobManager.class);

	@PostConstruct
	public void postDone() {
		if (Evn.isScheduler()) {
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_HSXF))) {
				log.info("------增加黄山讯飞同步任务");
				JobDetail job = JobBuilder.newJob(HsxfSyncDataJob.class)
						.withIdentity("SyncJobManagerHSXF").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerHSXF")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_HSXF)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置黄山讯飞同步时间，跳过定时任务");
			}
			// 正元一卡通
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_ZYYKT))) {
				log.info("------增加正元一卡通同步任务");
				JobDetail job = JobBuilder.newJob(ZyCardSyncDataJob.class)
						.withIdentity("SyncJobManagerZYYKT").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerZYYKT")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_ZYYKT)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置正元一卡通同步时间，跳过定时任务");
			}
			
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_AHYH))) {
				log.info("------增加安徽瑶海同步任务");
				JobDetail job = JobBuilder.newJob(AhyhSyncDataJob.class)
						.withIdentity("SyncJobManagerAHYH").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerAHYH")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_AHYH)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置安徽瑶海同步时间，跳过定时任务");
			}
			
			//瑶海讯飞同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_YHXF))) {
				log.info("------增加瑶海讯飞同步任务");
				JobDetail job = JobBuilder.newJob(YhBaseSyncDataJob.class)
						.withIdentity("SyncJobManagerYHXF").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerYHXF")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_YHXF)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置瑶海讯飞同步时间，跳过定时任务");
			}
			
			//用户中心数据同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_MSYK_TO_UC))) {
				log.info("------增加全国环境数据同步到uc任务");
				JobDetail job = JobBuilder.newJob(UcSyncDataJob.class)
						.withIdentity("SyncJobManagerMSYKTOUC").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerMSYKTOUC")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_MSYK_TO_UC)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置全国环境数据同步到uc同步时间，跳过定时任务");
			}
			
			//天长基础数据同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_AHTC))) {
				log.info("------增加天长基础数据同步任务");
				JobDetail job = JobBuilder.newJob(TcSyncDataJob.class)
						.withIdentity("SyncJobManagerAHTC").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerAHTC")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_AHTC)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置天长基础数据同步时间，跳过定时任务");
			}
			
			//广东鹰硕同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_GDYS))) {
				log.info("------增加广东鹰硕同步任务");
				JobDetail job = JobBuilder.newJob(YingShuoSyncDataJob.class)
						.withIdentity("SyncJobManagerGDYS").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerGDYS")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_GDYS)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置广东鹰硕同步时间，跳过定时任务");
			}
			
			//来宾同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_LBGZ))) {
				log.info("------增加来宾同步同步任务");
				JobDetail job = JobBuilder.newJob(LaiBinDataJob.class)
						.withIdentity("SyncJobManagerLBGZ").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerLBGZ")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_LBGZ)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置来宾同步同步时间，跳过定时任务");
			}
			
			//长治同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATE_CHANGZHI))) {
				log.info("------增加长治同步任务");
				JobDetail job = JobBuilder.newJob(ChangZhiSyncDataJob.class)
						.withIdentity("SyncJobManagerCHANGZHI").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerCHANGZHI")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATE_CHANGZHI)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置长治同步时间，跳过定时任务");
			}
			
			//甘肃同步
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_GS))) {
				log.info("------增加甘肃同步任务");
				JobDetail job = JobBuilder.newJob(GanSuSyncDataJob.class)
						.withIdentity("SyncJobManagerGS").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerGS")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_GS)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置甘肃同步时间，跳过定时任务");
			}
			
			//东莞同步
			if (StringUtils.isNotBlank(Evn.getString(ConstantSyncData.SYNC_DATA_DG))) {
				log.info("------增加东莞同步任务");
				JobDetail job = JobBuilder.newJob(DongGuanDataJob.class)
						.withIdentity("SyncJobManagerDG").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerDG")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_DG)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置东莞同步时间，跳过定时任务");
			}
			
			//龙游项目
			if (StringUtils.isNotBlank(Evn.getString(ConstantSyncData.SYNC_DATA_LY))) {
				log.info("------增加龙游同步任务");
				JobDetail job = JobBuilder.newJob(LYSyncDataJob.class)
						.withIdentity("SyncJobManagerLY").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("SyncJobManagerLY")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_LY)))
						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
				Evn.addJob(job, trigger);
			} else {
				log.info("------没有设置龙游同步时间，跳过定时任务");
			}
			
			
			//新增广元基础数据同步
//			if (StringUtils.isNotBlank(Evn
//					.getString(ConstantSyncData.SYNC_DATA_GYCX_CT))) {
//				log.info("------增加广元基础数据同步任务");
//				JobDetail job = JobBuilder.newJob(GySyncDataJob.class)
//						.withIdentity("SyncJobManagerGYJC").build();
//				Trigger trigger = TriggerBuilder
//						.newTrigger()
//						.withIdentity("SyncJobManagerGYJC")
//						.withSchedule(
//								CronScheduleBuilder.cronSchedule(Evn
//										.getString(ConstantSyncData.SYNC_DATA_AHTC)))
//						.startAt(DateUtils.addMinutes(new Date(), 1)).build();
//				Evn.addJob(job, trigger);
//			} else {
//				log.info("------没有设置天长基础数据同步时间，跳过定时任务");
//			}
			
		}
	}
}
