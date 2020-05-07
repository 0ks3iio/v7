package net.zdsoft.bigdata.taskScheduler.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bg_etl_job_step")
public class EtlJobStep extends BaseEntity<String> {

	private static final long serialVersionUID = 8625050826901651210L;

	private String groupId;

	private String jobId;
	
	private Integer step;

	@Transient
	private String jobName;


	@Override
	public String fetchCacheEntitName() {
		return "etl_job_step";
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}
