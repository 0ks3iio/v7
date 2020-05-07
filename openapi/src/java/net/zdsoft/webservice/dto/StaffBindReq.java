package net.zdsoft.webservice.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="StaffBindReq")
public class StaffBindReq {
	private Head head = new Head();
	private String body;
	public Head getHead() {
		return head;
	}
	@XmlElement(name = "HEAD")
	public void setHead(Head head) {
		this.head = head;
	}
	public String getBody() {
		return body;
	}
	@XmlElement(name = "BODY")
	public void setBody(String body) {
		this.body = body;
	}
	

	
}
