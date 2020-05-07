package net.zdsoft.basedata.entity;

import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_task_record")
public class TaskRecord extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String userId;
	private String unitId;
	private String name;
	private Integer status;
	private String businessType;
	private String serverType;
	private String resultMsg;
	private Date jobStartTime;
	private Date jobEndTime;
	private Date creationTime;
	private String type;
	private int isDeleted;
	private Date modifyTime;
	
	@Transient
	private Map<String,String> customParamMap;
	@Transient
	private int jobPos;
	@Transient
	private String jobStartTimeStr;
	@Transient
	private String jobEndTimeStr;
	@Transient
	private String creationTimeStr;
	
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getJobStartTimeStr() {
		return jobStartTimeStr;
	}

	public void setJobStartTimeStr(String jobStartTimeStr) {
		this.jobStartTimeStr = jobStartTimeStr;
	}

	public String getJobEndTimeStr() {
		return jobEndTimeStr;
	}

	public void setJobEndTimeStr(String jobEndTimeStr) {
		this.jobEndTimeStr = jobEndTimeStr;
	}

	public String getCreationTimeStr() {
		return creationTimeStr;
	}

	public void setCreationTimeStr(String creationTimeStr) {
		this.creationTimeStr = creationTimeStr;
	}

	public int getJobPos() {
		return jobPos;
	}

	public void setJobPos(int jobPos) {
		this.jobPos = jobPos;
	}

	public Map<String, String> getCustomParamMap() {
		return customParamMap;
	}

	public void setCustomParamMap(Map<String, String> customParamMap) {
		this.customParamMap = customParamMap;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Date getJobStartTime() {
		return jobStartTime;
	}

	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	public Date getJobEndTime() {
		return jobEndTime;
	}

	public void setJobEndTime(Date jobEndTime) {
		this.jobEndTime = jobEndTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String fetchCacheEntitName() {
		return "taskRecord";
	}

}
