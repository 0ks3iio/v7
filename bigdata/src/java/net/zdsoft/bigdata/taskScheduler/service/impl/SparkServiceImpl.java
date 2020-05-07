package net.zdsoft.bigdata.taskScheduler.service.impl;

import com.alibaba.fastjson.JSON;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.bigdata.data.entity.SparkRestResponse;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.data.utils.HdfsUtils;
import net.zdsoft.bigdata.taskScheduler.utils.SparkRestUtils;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobLogService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.taskScheduler.service.SparkService;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/10/25 16:33.
 */
@Service
public class SparkServiceImpl implements SparkService {

	private static final Logger logger = LoggerFactory
			.getLogger(SparkServiceImpl.class);

	@Resource
	private SysOptionRemoteService sysOptionRemoteService;
	@Resource
	private EtlJobService etlJobService;
	@Resource
	private EtlJobLogService etlJobLogService;
	@Resource
	private OptionService optionService;
	@Resource
	private BigLogService bigLogService;


	@Override
	public void saveSparkJob(EtlJob spark) throws IOException,
			URISyntaxException {
		boolean isAdd = true;
		if (!spark.getPath().endsWith("jar")) {
			logger.warn("调度文件格式错误！");
			throw new ControllerException("调度主文件格式错误！");
		}
		String systemFilePath = sysOptionRemoteService
				.findValue(Constant.FILE_PATH);
		// 临时文件地址
		String tempFileFullPath = spark.getPath();

		String jarFileName = tempFileFullPath.substring(tempFileFullPath
				.lastIndexOf("/") + 1);

		String id = StringUtils.isNotBlank(spark.getId()) ? spark.getId()
				: UuidUtils.generateUuid();
		// 拷贝文件到hdfs
		System.setProperty("HADOOP_USER_NAME", "root");
		// 修改正确的jar路径
		String realPath = HdfsUtils.businessFilePath + id + "/";
		OptionDto hdfsParam = optionService.getAllOptionParam("hdfs");
		HdfsUtils.copyFileToHdfs(tempFileFullPath, realPath, jarFileName,
				hdfsParam.getFrameParamMap().get("hdfs_url"));

		// 保存到数据库
		spark.setPath(tempFileFullPath);
		spark.setModifyTime(new Date());
		if (StringUtils.isNotBlank(spark.getId())) {
			isAdd = false;
			EtlJob oldSpark = etlJobService.findOne(spark.getId());
			etlJobService.update(spark, id, new String[] { "name", "fileName",
					"remark", "path", "businessFile", "modifyTime", "runType",
					"isSchedule", "scheduleParam", "hasParam" });
			//业务日志埋点  修改
			LogDto logDto=new LogDto();
			logDto.setBizCode("update-spark");
			logDto.setDescription("spark "+oldSpark.getName());
			logDto.setOldData(oldSpark);
			logDto.setNewData(spark);
			logDto.setBizName("批处理");
			bigLogService.updateLog(logDto);
		} else {
			spark.setCreationTime(new Date());
			spark.setId(id);
			etlJobService.save(spark);
			//业务日志埋点  新增
			LogDto logDto=new LogDto();
			logDto.setBizCode("insert-spark");
			logDto.setDescription("spark "+spark.getName());
			logDto.setNewData(spark);
			logDto.setBizName("批处理");
			bigLogService.insertLog(logDto);
		}
		// 如果是定时任务 需要增加到定时任务列表中去 定时任务指定某一台机器执行 通过参数配置
		if (spark.getIsSchedule() == 1) {
			Json json = new Json();
			json.put("jobId", spark.getId());
			json.put("cron", spark.getScheduleParam());
			if (isAdd) {// 增加
				json.put("operation", "add");
			} else {// 修改
				json.put("operation", "modify");
			}
			RedisUtils.publish(EtlChannelConstant.ETL_SPARK_REDIS_CHANNEL,
					json.toJSONString());
		} else {
			Json json = new Json();
			json.put("jobId", spark.getId());
			json.put("cron", "");
			// 删除
			json.put("operation", "delete");
			RedisUtils.publish(EtlChannelConstant.ETL_SPARK_REDIS_CHANNEL,
					json.toJSONString());
		}

	}

	@Override
	public SparkRestResponse dealSparkJob(String id) {
		long startTime = System.currentTimeMillis(); // 获取开始时间
		EtlJob sparkJob = etlJobService.findOne(id);
		// 查询spark参数
		OptionDto sparkParam = optionService.getAllOptionParam("spark");
		OptionDto hdfsParam = optionService.getAllOptionParam("hdfs");
		SparkRestResponse response = SparkRestUtils.submit(sparkJob,
				sparkParam, hdfsParam.getFrameParamMap().get("hdfs_url"));
		sparkJob.setAllFileNames(response.getSubmissionId());
		sparkJob.setLastCommitTime(new Date());
		saveLog(sparkJob, startTime, response);

		setJobStatus(sparkJob, response);

		etlJobService.update(sparkJob, id, new String[] { "allFileNames",
				"modifyTime", "lastCommitState", "lastCommitTime" });

		return response;
	}

	@Override
	public SparkRestResponse deleteSparkJob(String id) {
		EtlJob etlJob = etlJobService.findOne(id);
		try {
			// 删除任务
			etlJobService.delete(id);
			// 查询spark参数
			OptionDto sparkParam = optionService.getAllOptionParam("spark");
			return SparkRestUtils.killJob(etlJob.getAllFileNames(), sparkParam);
		} catch (Exception e) {
			return saveErrorLog(etlJob, e.getMessage());
		}
	}

	@Override
	public SparkRestResponse monitorSparkJob(String id) {
		EtlJob sparkJob = etlJobService.findOne(id);
		try {
			OptionDto sparkParam = optionService.getAllOptionParam("spark");
			SparkRestResponse response = SparkRestUtils.monitorJob(
					sparkJob.getAllFileNames(), sparkParam);

			if ("false".equals(response.getSuccess())) {
				return response;
			}

			if (EtlJob.STATE_ERROR == sparkJob.getLastCommitState()) {
				return response;
			}

			if (EtlJob.STATE_TRUE == sparkJob.getLastCommitState()
					&& SparkRestUtils.FINISHED
							.equals(response.getDriverState())) {
				return response;
			}

			if (0 == sparkJob.getLastCommitState()
					&& SparkRestUtils.SUBMITTED.equals(response
							.getDriverState())) {
				return response;
			}

			if (EtlJob.STATE_STOP == sparkJob.getLastCommitState()
					&& SparkRestUtils.KILLED.equals(response.getDriverState())) {
				return response;
			}

			if (EtlJob.STATE_RUNNING != sparkJob.getLastCommitState()
					&& 0 != sparkJob.getLastCommitState()) {
				saveLog(sparkJob, 0, response);
			} else {
				updateLog(sparkJob, response);
			}
			setJobStatus(sparkJob, response);
			etlJobService.update(sparkJob, id, new String[] {
					"lastCommitState", "lastCommitTime" });
			return response;
		} catch (Exception e) {
			return saveErrorLog(sparkJob, e.getMessage());
		}
	}

	private void saveLog(EtlJob sparkJob, long startTime,
			SparkRestResponse response) {
		EtlJobLog log = new EtlJobLog();
		log.setId(UuidUtils.generateUuid());
		log.setLogTime(new Date());
		log.setJobId(sparkJob.getId());
		log.setName(sparkJob.getName());
		log.setType(sparkJob.getJobType());
		log.setUnitId(sparkJob.getUnitId());
		log.setLogDescription(JSON.toJSONString(response));
		setJobStatus(sparkJob, response);
		log.setState(sparkJob.getLastCommitState());
		if (startTime != 0) {
			long endTime = System.currentTimeMillis();
			log.setDurationTime((endTime - startTime));
		}
		etlJobLogService.save(log);
	}

	private void updateLog(EtlJob sparkJob, SparkRestResponse response) {
		Pagination page = new Pagination(1, 1, false);
		List<EtlJobLog> lastLog = etlJobLogService.findByJobId(
				sparkJob.getId(), page);
		if (lastLog.size() > 0) {
			EtlJobLog log = lastLog.get(0);
			log.setLogTime(new Date());
			log.setUnitId(sparkJob.getUnitId());
			log.setLogDescription(JSON.toJSONString(response));
			setJobStatus(sparkJob, response);
			log.setState(sparkJob.getLastCommitState());
			etlJobLogService.update(log, log.getId(), new String[] { "logTime",
					"unitId", "logDescription", "state" });
		}
	}

	@Override
	public SparkRestResponse stopSparkJob(String id) {
		EtlJob sparkJob = etlJobService.findOne(id);
		try {
			OptionDto sparkParam = optionService.getAllOptionParam("spark");
			SparkRestResponse response = SparkRestUtils.killJob(
					sparkJob.getAllFileNames(), sparkParam);
			setJobStatus(sparkJob, response);
			etlJobService.update(sparkJob, id, new String[] {
					"lastCommitState", "lastCommitTime" });
			return response;
		} catch (Exception e) {
			return saveErrorLog(sparkJob, e.getMessage());
		}
	}

	private SparkRestResponse saveErrorLog(EtlJob sparkJob, String errorMsg) {
		EtlJobLog log = new EtlJobLog();
		log.setId(UuidUtils.generateUuid());
		log.setLogTime(new Date());
		log.setJobId(sparkJob.getId());
		log.setName(sparkJob.getName());
		log.setType(sparkJob.getJobType());
		log.setUnitId(sparkJob.getUnitId());
		log.setLogDescription(errorMsg);
		sparkJob.setLastCommitState(EtlJob.STATE_ERROR);
		log.setState(sparkJob.getLastCommitState());
		etlJobLogService.save(log);
		SparkRestResponse response = new SparkRestResponse();
		response.setSuccess("false");
		response.setDriverState(SparkRestUtils.FAILED);
		return response;
	}

	private void setJobStatus(EtlJob etlJob, SparkRestResponse response) {
		if (response.getDriverState() == null) {
			etlJob.setLastCommitState(0);
		}
		if (SparkRestUtils.FINISHED.equals(response.getDriverState())) {
			etlJob.setLastCommitState(EtlJob.STATE_TRUE);
		} else if (SparkRestUtils.FAILED.equals(response.getDriverState())) {
			etlJob.setLastCommitState(EtlJob.STATE_ERROR);
		} else if (SparkRestUtils.RUNNING.equals(response.getDriverState())) {
			etlJob.setLastCommitState(EtlJob.STATE_RUNNING);
		} else if (SparkRestUtils.KILLED.equals(response.getDriverState())) {
			etlJob.setLastCommitState(EtlJob.STATE_STOP);
		} else if (SparkRestUtils.SUBMITTED.equals(response.getDriverState())) {
			etlJob.setLastCommitState(0);
		}
	}

}
