package net.zdsoft.base.entity.eis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name= "eis_app")
public class RegisterAp extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return null;
	}
	private String apName;
	@Column(updatable = false, nullable = false)
	private String apKey;
	@Temporal(TemporalType.TIMESTAMP)
	private Date   creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date   modifyTime;

	public String getApName() {
		return apName;
	}
	public void setApName(String apName) {
		this.apName = apName;
	}
	public String getApKey() {
		return apKey;
	}
	public void setApKey(String apKey) {
		this.apKey = apKey;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
