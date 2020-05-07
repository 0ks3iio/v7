package net.zdsoft.bigdata.extend.data.entity;

import com.alibaba.fastjson.annotation.JSONField;
import net.zdsoft.framework.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 事件 Created by wangdongdong on 2018/9/25 16:52.
 */
@Entity
@Table(name = "bg_event")
public class Event extends BaseEntity<String> {

	private static final long serialVersionUID = 4109654568850617364L;

	@Override
	public String fetchCacheEntitName() {
		return "Event";
	}

	private String unitId;

	private String typeId;

	private String eventName;

	private Integer orderId;

	private String topicName;

	private Short userProperty;

	private Short envProperty;

	private Short timeProperty;

	private String remark;

	private String tableName;

	private String eventCode;

	private String urls;

	private String granularity;

	private int intervalTime;
	
	private int importSwitch;
	
	private String metadata;
	
	private String kafkaInfo;

	private Integer isCustom;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastCommitDate;
	
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

	@Transient
	private String eventTypeName;

	@Transient
	private Integer eventTypeOrderId;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Short getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(Short userProperty) {
		this.userProperty = userProperty;
	}

	public Short getEnvProperty() {
		return envProperty;
	}

	public void setEnvProperty(Short envProperty) {
		this.envProperty = envProperty;
	}

	public Short getTimeProperty() {
		return timeProperty;
	}

	public void setTimeProperty(Short timeProperty) {
		this.timeProperty = timeProperty;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public Date getLastCommitDate() {
		return lastCommitDate;
	}

	public void setLastCommitDate(Date lastCommitDate) {
		this.lastCommitDate = lastCommitDate;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public int getImportSwitch() {
		return importSwitch;
	}

	public void setImportSwitch(int importSwitch) {
		this.importSwitch = importSwitch;
	}

	public Integer getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(Integer isCustom) {
		this.isCustom = isCustom;
	}

	public Integer getEventTypeOrderId() {
		return eventTypeOrderId;
	}

	public void setEventTypeOrderId(Integer eventTypeOrderId) {
		this.eventTypeOrderId = eventTypeOrderId;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getKafkaInfo() {
		return kafkaInfo;
	}

	public void setKafkaInfo(String kafkaInfo) {
		this.kafkaInfo = kafkaInfo;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}
