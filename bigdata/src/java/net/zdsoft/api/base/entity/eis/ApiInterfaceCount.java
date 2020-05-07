package net.zdsoft.api.base.entity.eis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年1月5日上午10:03:43
 * 统计调用接口的信息
 */
@Entity
@Table(name = "bg_openapi_interface_count")
public class ApiInterfaceCount extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "openApiInterfaceCount";
	}
	@Column(nullable = false, length = 32)
	private String ticketKey;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@JSONField(name="num")
	private int count;
	private String interfaceId;
	private String type;
	private String resultType;
	private String message;
	private int dataType;
	private String pushUrl;
	private int isWarn;   //是否超时  1--是 0--否
	private long time;//耗时
	private String appIds;  //应用
	
	@Transient
	private String developerName;
	@Transient
	private String appName; //应用名称
	@Transient
	private int sequenceId; //序列号
	@Transient
	private String dataStatus;
	@Transient
	private int times;//次数
	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getIsWarn() {
		return isWarn;
	}

	public void setIsWarn(int isWarn) {
		this.isWarn = isWarn;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getAppIds() {
		return appIds;
	}

	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}

	public String getDeveloperName() {
		return developerName;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
