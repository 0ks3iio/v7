package net.zdsoft.teacherasess.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teacherasess_convert_exam")
public class TeacherasessConvertExam extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	private String unitId;
	private String examId;
	private float scale;//比例
	private String acadyear;
	private String convertId;//折算分方案id
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getConvertId() {
		return convertId;
	}

	public void setConvertId(String convertId) {
		this.convertId = convertId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherasessConvertExam";
	}
	
}