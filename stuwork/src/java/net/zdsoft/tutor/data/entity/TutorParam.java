package net.zdsoft.tutor.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年9月11日下午5:46:59
 *   导师参数表
 */
@Entity
@Table(name="tutor_param")
public class TutorParam extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String paramType;
	private String param;
	@Override
	public String fetchCacheEntitName() {
		return "tutorParam";
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
}
