package net.zdsoft.bigdata.taskScheduler.entity;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.framework.entity.BaseEntity;

public class KylinCube extends BaseEntity<String> {

	private static final long serialVersionUID = -7825738132329062368L;

	private String name;

	private String descriptor;

	private String status;

	private String project;

	private String model;

	private EtlJob etlJob;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public EtlJob getEtlJob() {
		return etlJob;
	}

	public void setEtlJob(EtlJob etlJob) {
		this.etlJob = etlJob;
	}

	@Override
	public String fetchCacheEntitName() {
		return "kylin";
	}
}
