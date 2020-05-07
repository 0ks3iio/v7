package net.zdsoft.api.base.entity.eis;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.api.base.entity.base.DataCenterBaseEntity;


@Entity
@Table(name = "bg_openapi_push_job")
public class ApiPushJob extends DataCenterBaseEntity{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "basePushJob";
	}
	private String type;   //数据的类型
	private String cron;   //定时周期表达式
	private String pushUrl; //推送的地址
	private String interfaceId; //接口的id
	private Date  updateStamp; //更新的时间
	private String dataType;  //1--基础数据  2--扩展库  3--业务数据
//	private String pushParam;  //  参数
//	private Integer paramType;  //  1--参数是地址  2---双方约定的字符串
	private String  requestUrl;  //请求的地址，  3--需要维护请求地址
	private Integer  jsonType;    // 1---fastJson  2--net.sf.json.JSONObject
	public Integer getJsonType() {
		return jsonType;
	}
	public void setJsonType(Integer jsonType) {
		this.jsonType = jsonType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getPushUrl() {
		return pushUrl;
	}
	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}
	public Date getUpdateStamp() {
		return updateStamp;
	}
	public void setUpdateStamp(Date updateStamp) {
		this.updateStamp = updateStamp;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
}
