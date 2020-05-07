package net.zdsoft.bigdata.taskScheduler.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "bg_etl_job")
public class EtlJob extends BaseEntity<String> {

	private static final long serialVersionUID = 8625050826901651210L;

	public static final Integer STATE_TRUE = 1;

	public static final Integer STATE_ERROR = 2;

	/**
	 * 运行中
	 */
	public static final Integer STATE_RUNNING = 3;

	/**
	 * 停止
	 */
	public static final Integer STATE_STOP = -1;

	public static final String KETTLE_JOB = "job";

	public static final String KETTLE_TRANSFORMATION = "Transformation";
	
	public static final String KYLIN_JOB = "kylin";
	
	public static final String SHELL_JOB = "shell";

	private String unitId;

	private String name;
	
	private Integer etlType;

	private String jobType;
	
	private String jobCode;

	private String path;

	private String fileName;
	
	private String allFileNames;

	private Integer lastCommitState;

	private Date lastCommitTime;

	private String lastCommitLogId;

	private String remark;

	private Integer isSchedule;
	
	private Integer hasParam;

	private String scheduleParam;

	private String createUserId;

	private String businessFile;

	private String sourceId;

	private String targetId;

	private String nodeId;

	private String sourceType;

	private String targetType;

	private Integer runType;

	private String flowChartJson;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

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

	public Integer getEtlType() {
		return etlType;
	}

	public void setEtlType(Integer etlType) {
		this.etlType = etlType;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getLastCommitState() {
		return lastCommitState;
	}

	public void setLastCommitState(Integer lastCommitState) {
		this.lastCommitState = lastCommitState;
	}

	public Date getLastCommitTime() {
		return lastCommitTime;
	}

	public void setLastCommitTime(Date lastCommitTime) {
		this.lastCommitTime = lastCommitTime;
	}

	public String getLastCommitLogId() {
		return lastCommitLogId;
	}

	public void setLastCommitLogId(String lastCommitLogId) {
		this.lastCommitLogId = lastCommitLogId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsSchedule() {
		return isSchedule;
	}

	public void setIsSchedule(Integer isSchedule) {
		this.isSchedule = isSchedule;
	}

	public Integer getHasParam() {
		return hasParam;
	}

	public void setHasParam(Integer hasParam) {
		this.hasParam = hasParam;
	}

	public String getScheduleParam() {
		return scheduleParam;
	}

	public void setScheduleParam(String scheduleParam) {
		this.scheduleParam = scheduleParam;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAllFileNames() {
		return allFileNames;
	}

	public void setAllFileNames(String allFileNames) {
		this.allFileNames = allFileNames;
	}

	public String getBusinessFile() {
		return businessFile;
	}

	public void setBusinessFile(String businessFile) {
		this.businessFile = businessFile;
	}

	public Integer getRunType() {
		return runType;
	}

	public void setRunType(Integer runType) {
		this.runType = runType;
	}

	@Override
	public String fetchCacheEntitName() {
		return "etl";
	}

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

	public String getFlowChartJson() {
		return flowChartJson;
	}

	public void setFlowChartJson(String flowChartJson) {
		this.flowChartJson = flowChartJson;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
}
