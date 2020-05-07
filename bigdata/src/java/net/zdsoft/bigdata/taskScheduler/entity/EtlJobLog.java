package net.zdsoft.bigdata.taskScheduler.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_etl_job_log")
public class EtlJobLog extends BaseEntity<String> {

	private static final long serialVersionUID = 2714716935534120845L;

	public static final int state_success =1;
	
	public static final int state_fail =2;
	
	private String unitId;

	private String jobId;
	
	private String name;

	private String type;
	
	private int state;
	
	private long durationTime;

	private String logDescription;

	@Temporal(TemporalType.TIMESTAMP)
	private Date logTime;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime;
	}

	@Lob
	public String getLogDescription() {
		return logDescription;
	}

	public void setLogDescription(String logDescription) {
		this.logDescription = logDescription;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "etlLog";
	}

}
