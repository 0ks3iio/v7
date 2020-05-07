package net.zdsoft.webservice.dto;

import javax.xml.bind.annotation.XmlElement;

public class Head {

	private String code;
	private String sid;
	private String timestamp;
	private String serviceid;

	public String getCode() {
		return code;
	}
	@XmlElement(name = "CODE")
	public void setCode(String code) {
		this.code = code;
	}

	public String getSid() {
		return sid;
	}
	@XmlElement(name = "SID")
	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTimestamp() {
		return timestamp;
	}
	@XmlElement(name = "TIMESTAMP")
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getServiceid() {
		return serviceid;
	}
	@XmlElement(name = "SERVICEID")
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

}
