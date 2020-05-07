package net.zdsoft.scoremanage.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 权限设置  只受单位影响
 */
@Entity
@Table(name="scoremanage_not_limit")
public class NotLimit extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String teacherId;
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "notLimit";
	}

}
