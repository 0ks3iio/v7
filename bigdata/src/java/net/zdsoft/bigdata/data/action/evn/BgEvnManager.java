package net.zdsoft.bigdata.data.action.evn;

import net.zdsoft.bigdata.daq.data.job.ApiDataDaqJob;
import net.zdsoft.bigdata.daq.data.job.BizOperationLogJob;
import net.zdsoft.bigdata.daq.data.job.DaqOperationLogJob;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.taskScheduler.listener.EtlRedisListener;
import net.zdsoft.bigdata.taskScheduler.task.EtlQuartzJobService;
import net.zdsoft.bigdata.extend.data.action.sync.StudentTagSyncDataJob;
import net.zdsoft.bigdata.extend.data.action.sync.TeacherTagSyncDataJob;
import net.zdsoft.bigdata.extend.data.listener.BgCalChannelConstant;
import net.zdsoft.bigdata.extend.data.listener.BgCalRedisListener;
import net.zdsoft.bigdata.extend.data.task.BgCalQuartzJobService;
import net.zdsoft.bigdata.stat.Job.BgMetadataStatJob;
import net.zdsoft.bigdata.stat.Job.BgSysStatJob;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.util.Date;

@Lazy(false)
@Component
public class BgEvnManager {
    private Logger logger = Logger.getLogger(BgEvnManager.class);

    public static final String EIS_SCHEDULER_BIGDATA_DAQ = "eis.scheduler.bigdata.daq";

    @PostConstruct
    public void postDone() {

        if (BooleanUtils.toBoolean(Evn.getString(EIS_SCHEDULER_BIGDATA_DAQ))) {
            System.out.println("==================EIS_SCHEDULER_BIGDATA_DAQ======================");
            // 埋点数据采集
            DaqOperationLogJob daqOperationLogJob = new DaqOperationLogJob();
            try {
                daqOperationLogJob.readLog();
                logger.info("埋点数据采集初始化成功");
            } catch (Exception e) {
                logger.error("埋点数据采集初始化失败" + e.getMessage());
            }
        }

        if (Evn.isScheduler()) {
            System.out.println("==================EIS_SCHEDULER_BIGDATA_NORMAL======================");
            // 订阅kettle调度通道
            JedisPubSub kettlePubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的kettle调度通道");
                    RedisUtils.subscribe(kettlePubSub,
                            EtlChannelConstant.ETL_KETTLE_REDIS_CHANNEL);
                }
            }).start();

            // 订阅kylin调度通道
            JedisPubSub kylinPubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的kylin调度通道");
                    RedisUtils.subscribe(kylinPubSub,
                            EtlChannelConstant.ETL_KYLIN_REDIS_CHANNEL);
                }
            }).start();

            // 订阅shell调度通道
            JedisPubSub ShellPubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的shell调度通道");
                    RedisUtils.subscribe(ShellPubSub,
                            EtlChannelConstant.ETL_SHELL_REDIS_CHANNEL);
                }
            }).start();

            // 订阅python调度通道
            JedisPubSub pythonPubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的python调度通道");
                    RedisUtils.subscribe(pythonPubSub,
                            EtlChannelConstant.ETL_PYTHON_REDIS_CHANNEL);
                }
            }).start();


            // 订阅flink调度通道
            JedisPubSub flinkPubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的flink调度通道");
                    RedisUtils.subscribe(flinkPubSub,
                            EtlChannelConstant.ETL_FLINK_REDIS_CHANNEL);
                }
            }).start();

            // 订阅flink调度通道
            JedisPubSub groupPubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的group调度通道");
                    RedisUtils.subscribe(groupPubSub,
                            EtlChannelConstant.ETL_GROUP_REDIS_CHANNEL);
                }
            }).start();

            // 订阅spark调度通道
//			JedisPubSub sparkPubSub = new EtlRedisListener();
//			new Thread(new Runnable() {
//				public void run() {
//					logger.info("订阅Redis的spark调度通道");
//					BgRedisUtils.subscribe(sparkPubSub,
//							EtlChannelConstant.ETL_SPARK_REDIS_CHANNEL);
//				}
//			}).start();

            // 订阅datax调度通道
            JedisPubSub dataxPubSub = new EtlRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的spark调度通道");
                    RedisUtils.subscribe(dataxPubSub,
                            EtlChannelConstant.ETL_DATAX_REDIS_CHANNEL);
                }
            }).start();

            // 大数据计算通道
            JedisPubSub bgCalPubSub = new BgCalRedisListener();
            new Thread(new Runnable() {
                public void run() {
                    logger.info("订阅Redis的大数据计算调度通道");
                    RedisUtils.subscribe(bgCalPubSub,
                            BgCalChannelConstant.BG_CAL_REDIS_CHANNEL);
                }
            }).start();
            try {
                Evn.<EtlQuartzJobService>getBean("etlQuartzJobService")
                        .initEtlJob();
                logger.info("etl调度定时任务初始化");
            } catch (Exception e) {
                logger.error("etl定时任务初始化失败" + e.getMessage());
            }

            try {
                Evn.<BgCalQuartzJobService>getBean("bgCalQuartzJobService")
                        .initCalJob();
                logger.info("大数据计算定时任务初始化");
            } catch (Exception e) {
                logger.error("大数据计算定时任务初始化失败" + e.getMessage());
            }

            // 学生标签库后台计算
            // 每天2点30分执行
            JobDetail stuJob = JobBuilder.newJob(StudentTagSyncDataJob.class)
                    .withIdentity("syncJob_student_tag").build();
            Trigger stu_trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("syncJob_student_tag")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 30 2 * * ?"))
                    .startAt(DateUtils.addMinute(new Date(), 1)).build();
            Evn.addJob(stuJob, stu_trigger);
            logger.info("学生标签库定制统计任务已启动");

            // 教师标签库后台计算
            // 每天3点30分执行
            JobDetail teaJob = JobBuilder.newJob(TeacherTagSyncDataJob.class)
                    .withIdentity("syncJob_teacher_tag").build();
            Trigger tea_trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("syncJob_teacher_tag")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 30 3 * * ?"))
                    .startAt(DateUtils.addMinute(new Date(), 1)).build();
            Evn.addJob(teaJob, tea_trigger);
            logger.info("教师标签库定制统计任务已启动");

            // 元数据后台统计
            // 每天0点30分执行
            JobDetail bgMetadataStatJob = JobBuilder.newJob(BgMetadataStatJob.class)
                    .withIdentity("metadata_daily_stat_Job").build();
            Trigger metadata_stat_trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("metadata_daily_stat_Job")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 30 0 * * ?"))
                    .startAt(DateUtils.addMinute(new Date(), 1)).build();
            Evn.addJob(bgMetadataStatJob, metadata_stat_trigger);
            logger.info("元数据后台统计任务已启动");

            // 每隔1小时执行
            JobDetail bgSysStatJob = JobBuilder.newJob(BgSysStatJob.class)
                    .withIdentity("bg_sys_stat_job").build();
            Trigger bgSysStatJob_trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("bg_sys_stat_job")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?"))
                    // CronScheduleBuilder.cronSchedule("*/30 * * * * ?"))
                    .startNow().build();
            Evn.addJob(bgSysStatJob, bgSysStatJob_trigger);


            JobDetail bizOperationLogJob = JobBuilder.newJob(BizOperationLogJob.class).build();
            Trigger logTrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30))
                    .startAt(DateUtils.addMinute(new Date(), 1)).build();
            Evn.addJob(bizOperationLogJob, logTrigger);
            logger.info("业务日志采集任务已启动");


            JobDetail apiJob = JobBuilder.newJob(ApiDataDaqJob.class).withIdentity("bg_api_job").build();
            Trigger apiJobTrigger = TriggerBuilder.newTrigger().withIdentity("bg_api_job")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                    .startNow().build();
            Evn.addJob(apiJob, apiJobTrigger);
            logger.info("api数据采集任务已启动");
        }
    }
}
