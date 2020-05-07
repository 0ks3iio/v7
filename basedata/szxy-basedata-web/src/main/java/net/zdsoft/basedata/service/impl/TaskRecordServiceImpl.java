package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.JobConstants;
import net.zdsoft.basedata.dao.TaskRecordDao;
import net.zdsoft.basedata.dto.TaskJobDto;
import net.zdsoft.basedata.entity.TaskParameter;
import net.zdsoft.basedata.entity.TaskRecord;
import net.zdsoft.basedata.job.TaskErrorException;
import net.zdsoft.basedata.job.TaskJobDataParam;
import net.zdsoft.basedata.job.TaskJobReply;
import net.zdsoft.basedata.job.TaskJobService;
import net.zdsoft.basedata.service.TaskParameterService;
import net.zdsoft.basedata.service.TaskRecordService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Service("taskRecordService")
public class TaskRecordServiceImpl extends BaseServiceImpl<TaskRecord, String> implements TaskRecordService {

	@Autowired
	private TaskRecordDao taskRecordDao;
	@Autowired
	private TaskParameterService taskParameterService;
	
	private TaskJobService taskJobService = null;
	
	@Override
	protected BaseJpaRepositoryDao<TaskRecord, String> getJpaDao() {
		return taskRecordDao;
	}

	@Override
	protected Class<TaskRecord> getEntityClass() {
		return TaskRecord.class;
	}

	@Override
	public List<TaskRecord> findList(String serverType, String type, int status, Pageable pageable) {
		List<TaskRecord> findList = taskRecordDao.findList(serverType,type,status,pageable);
		if(CollectionUtils.isNotEmpty(findList)){
			Set<String> jobIds = new HashSet<String>();
			for (TaskRecord item : findList) {
				jobIds.add(item.getId());
			}
			Map<String, Map<String, String>> findMap = taskParameterService.findMap(jobIds.toArray(new String[0]));
			for (TaskRecord item : findList) {
				item.setCustomParamMap(findMap.get(item.getId()));
			}
		}
		return findList;
	}

	@Override
	public void updateJobNoHand(int resetTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		taskRecordDao.updateJobNoHand(calendar.getTime());
	}

	@Override
	public String TaskJobStart(TaskJobDto taskJobDto) throws Exception{
		if(StringUtils.isBlank(taskJobDto.getServiceName())){
			throw new Exception("参数有误！");
		}
		final String jobId = UuidUtils.generateUuid();
		final TaskJobDataParam taskJobDataParam = new TaskJobDataParam();
		taskJobDataParam.setJobId(jobId);
		final TaskJobReply taskJobReply = new TaskJobReply();
		taskJobReply.setHasTask(taskJobDto.isHasTask());
		if(taskJobDto.isHasTask()){
			if(taskJobDto.getLoginInfo() == null || StringUtils.isBlank(taskJobDto.getBusinessType())){
				throw new Exception("参数有误！");
			}
			TaskRecord taskRecord = new TaskRecord();
			taskRecord.setId(jobId);
			taskRecord.setUnitId(taskJobDto.getLoginInfo().getUnitId());
			taskRecord.setUserId(taskJobDto.getLoginInfo().getUserId());
			taskRecord.setName(taskJobDto.getName());
			taskRecord.setBusinessType(taskJobDto.getBusinessType());
			taskRecord.setType(JobConstants.TYPE_1);
			taskRecord.setServerType(JobConstants.SERVER_TYPE_7);
			taskRecord.setStatus(JobConstants.TASK_STATUS_NO_HAND);
			taskJobDto.getCustomParamMap().put(JobConstants.KEY_SYS_SERVICE_NAME, taskJobDto.getServiceName());
			taskRecord.setCustomParamMap(taskJobDto.getCustomParamMap());
			try{
				saveTaskRecord(taskRecord);
				cacheSave(jobId, taskJobReply);
			}catch (Exception e) {
				e.printStackTrace();
				throw new Exception("任务保存失败！");
			}
		}else{
			taskJobDataParam.setCustomParamMap(taskJobDto.getCustomParamMap());
			try{
				taskJobService = (TaskJobService)Evn.getBean(taskJobDto.getServiceName());
			}catch (Exception e) {
				e.printStackTrace();
				throw new Exception("配置未找到！");
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						addActionMessage(jobId,taskJobReply,"开始处理任务······");
						taskJobService.jobDatas(taskJobDataParam, taskJobReply);
					}catch (TaskErrorException e) {
						addActionError(jobId,taskJobReply,e.getMessage());
					}catch (Exception e) {
						addActionError(jobId,taskJobReply,"出现其他错误！");
					}finally {
						if (null != taskJobReply.getActionErrors() && taskJobReply.getActionErrors().size() > 0) {
							taskJobReply.setActionMessages(new ArrayList<String>());
						}
						taskJobReply.setValue(JobConstants.STATUS_END);
						cacheSave(jobId, taskJobReply);
			        }
				}
			}).start();
		}
		return jobId;
	}
	
	private void addActionMessage(String jobId,TaskJobReply taskJobReply,String msg){
		taskJobReply.addActionMessage(msg);
		cacheSave(jobId,taskJobReply);
	}
	
	private void addActionError(String jobId,TaskJobReply taskJobReply,String msg){
		taskJobReply.addActionError(msg);
		cacheSave(jobId,taskJobReply);
	}
	
	private void cacheSave(String jobId,TaskJobReply taskJobReply){
		RedisUtils.setObject(JobConstants.CACHE_KEY_TASK_JOB_+jobId, taskJobReply, JobConstants.CACHE_SECONDS);
	}
	
	public TaskRecord saveTaskRecord(TaskRecord taskRecord){
		Map<String, String> customParamMap = taskRecord.getCustomParamMap();
		if(customParamMap!=null){
			List<TaskParameter> addList = new ArrayList<TaskParameter>();
			TaskParameter tp = null;
			for (Map.Entry<String, String> entry : customParamMap.entrySet()) {
				tp = new TaskParameter();
				tp.setJobId(taskRecord.getId());
				tp.setName(entry.getKey());
				tp.setValue(entry.getValue());
				addList.add(tp);
			}
			taskParameterService.saveAllEntitys(addList.toArray(new TaskParameter[0]));
		}
		return this.saveAllEntitys(taskRecord).get(0);
	}
	
	@Override
	public List<TaskRecord> saveAllEntitys(TaskRecord... taskRecord) {
		return taskRecordDao.saveAll(checkSave(taskRecord));
	}

	@Override
	public void deleteByIds(String... jobId) {
		List<TaskParameter> findList = taskParameterService.findList(jobId);
		Set<String> tpIds = new HashSet<String>();
		for (TaskParameter item : findList) {
			tpIds.add(item.getId());
		}
		taskParameterService.deleteAllByIds(tpIds.toArray(new String[0]));
		taskRecordDao.deleteAllByIds(jobId);
	}
	
	@Override
	public TaskRecord findTaskJob(String jobId) {
		TaskRecord findTaskJob = this.findOne(jobId);
		if(findTaskJob != null && JobConstants.TASK_STATUS_NO_HAND == findTaskJob.getStatus()){
			List<TaskRecord> findList = taskRecordDao.findList(JobConstants.SERVER_TYPE_7,JobConstants.TYPE_1,JobConstants.TASK_STATUS_NO_HAND);
			for (int i = 1; i <= findList.size(); i++) {
				if(findTaskJob.getId().equals(findList.get(i-1).getId())){
					findTaskJob.setJobPos(i);
					break;
				}
			}
		}
		return findTaskJob;
	}

	@Override
	public List<TaskRecord> findListByUnitId(String serverType, String type, String unitId, String businessType, Pagination page) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -3);
		page.setMaxRowCount(taskRecordDao.countListByUnitId(serverType, type, unitId, businessType,calendar.getTime()));
		return taskRecordDao.findListByUnitId(serverType, type, unitId, businessType, calendar.getTime(),Pagination.toPageable(page));
	}

	@Override
	public void deleteOld() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		List<TaskRecord> list = taskRecordDao.findOldList(calendar.getTime());
		if(CollectionUtils.isNotEmpty(list)){
			Set<String> trIds = new HashSet<String>();
			for (TaskRecord item : list) {
				trIds.add(item.getId());
			}
			List<TaskParameter> findList = taskParameterService.findList(trIds.toArray(new String[0]));
			Set<String> tpIds = new HashSet<String>();
			for (TaskParameter item : findList) {
				tpIds.add(item.getId());
			}
			taskParameterService.deleteAllByIds(tpIds.toArray(new String[0]));
			taskRecordDao.deleteAllByIds(trIds.toArray(new String[0]));
		}
	}

}
