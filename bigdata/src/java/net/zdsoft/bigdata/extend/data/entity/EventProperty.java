package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 事件属性 Created by wangdongdong on 2018/9/25 16:52.
 */
@Entity
@Table(name = "bg_event_property")
public class EventProperty extends BaseEntity<String> {

	private static final long serialVersionUID = -3375475613478230165L;

	@Override
	public String fetchCacheEntitName() {
		return "EventProperty";
	}
	
	public static final String EVENT_USER = "0000000000000000000000000000user";
	
	public static final String EVENT_ENV = "00000000000000000000000000000env";
	
	public static final String EVENT_TIME = "0000000000000000000000000000time";

	private String unitId;

	private String eventId;

	private String propertyName;

	private String fieldName;

	private Integer orderId;

	private String orderJson;

	private Short isShowChart;
	
	private Short isSequential;

	private String remark;

	private String dataDictionary;

	@Transient
	private String eventName;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderJson() {
		return orderJson;
	}

	public void setOrderJson(String orderJson) {
		this.orderJson = orderJson;
	}

	public Short getIsShowChart() {
		return isShowChart;
	}

	public void setIsShowChart(Short isShowChart) {
		this.isShowChart = isShowChart;
	}

	public Short getIsSequential() {
		return isSequential;
	}

	public void setIsSequential(Short isSequential) {
		this.isSequential = isSequential;
	}

	public String getDataDictionary() {
		return dataDictionary;
	}

	public void setDataDictionary(String dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
