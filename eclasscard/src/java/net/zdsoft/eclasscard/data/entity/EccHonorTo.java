package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_honor_to")
public class EccHonorTo extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private Integer type;
	private String honorId;
	private String objectId;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardHonorTo";
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHonorId() {
		return honorId;
	}

	public void setHonorId(String honorId) {
		this.honorId = honorId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
}
