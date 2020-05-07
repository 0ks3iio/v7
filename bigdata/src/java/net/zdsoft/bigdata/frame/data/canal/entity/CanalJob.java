package net.zdsoft.bigdata.frame.data.canal.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_etl_canal_job")
public class CanalJob  extends BaseEntity<String> {

	private static final long serialVersionUID = 4109654568850617364L;

	@Override
    public String fetchCacheEntitName() {
        return "canal_job";
    }

    private String jobName;

    private String jobCode;

    private String hostName;
    
    private Long port;
    
    private String destination;
    
    private Long batchSize;
    
    private String topicName;
    
    private String subscribeRule;
    
    private Long orderId;
    
    private String remark;
    
    private BigDecimal totalSize;
    
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastCommitDate;
	
	private Integer status;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Long getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Long batchSize) {
		this.batchSize = batchSize;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getSubscribeRule() {
		return subscribeRule;
	}

	public void setSubscribeRule(String subscribeRule) {
		this.subscribeRule = subscribeRule;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(BigDecimal totalSize) {
		this.totalSize = totalSize;
	}

	public Date getLastCommitDate() {
		return lastCommitDate;
	}

	public void setLastCommitDate(Date lastCommitDate) {
		this.lastCommitDate = lastCommitDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
