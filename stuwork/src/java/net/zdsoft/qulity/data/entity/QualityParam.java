package net.zdsoft.qulity.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "quality_param")
public class QualityParam extends BaseEntity<String> {
	private static final long serialVersionUID = 6496862172463014998L;

	private String unitId;
	private String paramType;
	private int param;
	private int paramPer;
	private String gradeId;

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

	public int getParam() {
		return param;
	}

	public void setParam(int param) {
		this.param = param;
	}

	public int getParamPer() {
		return paramPer;
	}

	public void setParamPer(int paramPer) {
		this.paramPer = paramPer;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "qualityParam";
	}

}
