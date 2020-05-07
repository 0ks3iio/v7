package net.zdsoft.api.base.entity.eis;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_openapi_dataset")
public class ApiDataSet extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Override
	public String fetchCacheEntitName() {
		return "apiDataSet";
	}
    
	private String name;
	private String mdId; 
	private String remark;
	private Date creationTime;
	private Date modifyTime;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMdId() {
		return mdId;
	}
	public void setMdId(String mdId) {
		this.mdId = mdId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
