package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sym_user_ah")
public class UserAhSync extends BaseEntity<String> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String SYNC_TYPE_UNIT = "1";
	public static final String SYNC_TYPE_USER = "2";
	public static final String SYNC_TYPE_GRADE = "3";
	public static final String SYNC_TYPE_CLASS = "4";
	
	private String objectId;
	private String objectType;
	private String ahObjectId;
	private String ahUnitId;
	
	public String getAhUnitId() {
		return ahUnitId;
	}

	public void setAhUnitId(String ahUnitId) {
		this.ahUnitId = ahUnitId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}



	public String getObjectType() {
		return objectType;
	}



	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	public String getAhObjectId() {
		return ahObjectId;
	}



	public void setAhObjectId(String ahObjectId) {
		this.ahObjectId = ahObjectId;
	}



	@Override
	public String fetchCacheEntitName() {
		return "userAhSync";
	}

}
