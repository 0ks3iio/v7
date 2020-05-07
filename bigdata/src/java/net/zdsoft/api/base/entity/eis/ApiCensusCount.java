package net.zdsoft.api.base.entity.eis;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_openapi_census_count")
public class ApiCensusCount extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "apiCensusCount";
	}
	private String key;
	private String value;
	
	private int typeCount; //类型总数
	private int apiCount;  //接口总数
	private int saveCount; //推送的总数
	private int findCount; //拉取的总数
	private int saveNum;   //推送的次数
	private int findNum;   //拉取的次数
	
	private String mouthCensus; //月统计次数
	private String weekCensus;  //周统计次数
	private String dayCensus;   //天统计次数
	private String allCensus;   //总统计次数

	private String allApiCensus; //总的各个接口调用
	private String mouthApiCensus;//月的各个接口调用
	private String weekApiCensus;//周的各个接口调用
	private String dayApiCensus;//天的各个接口调用
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime; //创建时间
	
	public int getTypeCount() {
		return typeCount;
	}
	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}
	public int getApiCount() {
		return apiCount;
	}
	public void setApiCount(int apiCount) {
		this.apiCount = apiCount;
	}
	public int getSaveCount() {
		return saveCount;
	}
	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}
	public int getFindCount() {
		return findCount;
	}
	public void setFindCount(int findCount) {
		this.findCount = findCount;
	}
	public int getSaveNum() {
		return saveNum;
	}
	public void setSaveNum(int saveNum) {
		this.saveNum = saveNum;
	}
	public int getFindNum() {
		return findNum;
	}
	public void setFindNum(int findNum) {
		this.findNum = findNum;
	}
	public String getMouthCensus() {
		return mouthCensus;
	}
	public void setMouthCensus(String mouthCensus) {
		this.mouthCensus = mouthCensus;
	}
	public String getWeekCensus() {
		return weekCensus;
	}
	public void setWeekCensus(String weekCensus) {
		this.weekCensus = weekCensus;
	}
	public String getDayCensus() {
		return dayCensus;
	}
	public void setDayCensus(String dayCensus) {
		this.dayCensus = dayCensus;
	}
	public String getAllCensus() {
		return allCensus;
	}
	public void setAllCensus(String allCensus) {
		this.allCensus = allCensus;
	}
	public String getAllApiCensus() {
		return allApiCensus;
	}
	public void setAllApiCensus(String allApiCensus) {
		this.allApiCensus = allApiCensus;
	}
	public String getMouthApiCensus() {
		return mouthApiCensus;
	}
	public void setMouthApiCensus(String mouthApiCensus) {
		this.mouthApiCensus = mouthApiCensus;
	}
	public String getWeekApiCensus() {
		return weekApiCensus;
	}
	public void setWeekApiCensus(String weekApiCensus) {
		this.weekApiCensus = weekApiCensus;
	}
	public String getDayApiCensus() {
		return dayApiCensus;
	}
	public void setDayApiCensus(String dayApiCensus) {
		this.dayApiCensus = dayApiCensus;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
