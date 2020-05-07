package net.zdsoft.api.base.entity.eis;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_openapi_monitor_census")
public class ApiMonitorCensus extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "apiMonitorCensus";
	}
	private String type; //allC--头部  2--dayC  MouthC  WeekC  saveD  saveDT(developer) findD  findDT(developer)  
	private String key1;
	private String value1;
	private String key2;
	private String value2;
	private Date   creationTime;
	private String ticketKey; 
	public String getType() {
		return type;
	}
	public ApiMonitorCensus (String type) {
		this.type = type;
	}
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getKey2() {
		return key2;
	}
	public void setKey2(String key2) {
		this.key2 = key2;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}
}
