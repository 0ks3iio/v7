package net.zdsoft.teacherasess.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="teacherasess_asess_rank")
public class TeacherAsessRank extends BaseEntity<String>{
	
	private String unitId;
	private String assessId;
	private String subjectId;
	private String name;
	private int aslice;
	private int bslice;
	private float scale;
	private int rankOrder;
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAssessId() {
		return assessId;
	}

	public void setAssessId(String assessId) {
		this.assessId = assessId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAslice() {
		return aslice;
	}

	public void setAslice(int aslice) {
		this.aslice = aslice;
	}

	public int getBslice() {
		return bslice;
	}

	public void setBslice(int bslice) {
		this.bslice = bslice;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherAsessRank";
	}

	public int getRankOrder() {
		return rankOrder;
	}

	public void setRankOrder(int rankOrder) {
		this.rankOrder = rankOrder;
	}

}
