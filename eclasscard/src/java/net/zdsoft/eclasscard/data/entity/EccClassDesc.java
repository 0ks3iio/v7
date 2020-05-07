package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_class_desc")
public class EccClassDesc  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String classId;
	private String pictrueId;
	private String content;
	
	@Transient
	private String picUrl;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardClassDesc";
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPictrueId() {
		return pictrueId;
	}

	public void setPictrueId(String pictrueId) {
		this.pictrueId = pictrueId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


}
