package net.zdsoft.bigdata.extend.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  系统监控url entity
 * @author jiangf
 *
 */
@Entity
@Table(name = "bg_monitor_url")
public class MonitorUrl extends BaseEntity<String> {

	private static final long serialVersionUID = -7421469356145893500L;

	private String monitorName;

    private String monitorUrl;

    private Integer orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public String getMonitorUrl() {
		return monitorUrl;
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
    public String fetchCacheEntitName() {
        return "monitor";
    }
}
