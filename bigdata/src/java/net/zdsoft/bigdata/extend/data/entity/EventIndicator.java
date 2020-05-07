package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bg_event_indicator")
public class EventIndicator extends BaseEntity<String> {

	private static final long serialVersionUID = 5003612013632813244L;

	@Override
	public String fetchCacheEntitName() {
		return "eventIndicator";
	}

	private String unitId;// 单位id
	private String eventId;// 事件id
	private String indicatorName;// 指标名称
	private String aggType;// 指标聚合方式
	private String aggField;// 指标聚合列
	private String aggOutputName;// 指标聚合输出字段名
	private int orderId;// 排序号
	private String remark;// 备注

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

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public String getAggType() {
		return aggType;
	}

	public void setAggType(String aggType) {
		this.aggType = aggType;
	}

	public String getAggField() {
		return aggField;
	}

	public void setAggField(String aggField) {
		this.aggField = aggField;
	}

	public String getAggOutputName() {
		return aggOutputName;
	}

	public void setAggOutputName(String aggOutputName) {
		this.aggOutputName = aggOutputName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
}
