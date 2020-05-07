package net.zdsoft.teacherasess.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teacherasess_convert")
public class TeacherasessConvert extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	private String unitId;
	private String acadyear;
	private String gradeId;
	private String name;
	private int examNum;//包含考试数
	private String status; //统计状态
	private Date creationTime;
	private int isDeleted;
	
	private String xuankaoType;//物化生政史地技科目是否设为选考
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExamNum() {
		return examNum;
	}

	public void setExamNum(int examNum) {
		this.examNum = examNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teacherasessConvert";
	}

	public String getXuankaoType() {
		return xuankaoType;
	}

	public void setXuankaoType(String xuankaoType) {
		this.xuankaoType = xuankaoType;
	}
	
}