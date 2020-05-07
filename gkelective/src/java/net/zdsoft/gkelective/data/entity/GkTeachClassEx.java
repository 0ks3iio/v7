package net.zdsoft.gkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="gkelective_teach_class_ex")
public class GkTeachClassEx extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String roundsId;
	private String teachClassId;
	private Double averageScore;
	
	public String getRoundsId() {
		return roundsId;
	}

	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}

	public String getTeachClassId() {
		return teachClassId;
	}

	public void setTeachClassId(String teachClassId) {
		this.teachClassId = teachClassId;
	}

	public Double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gkTeachClassEx";
	}

}
