package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "timetable_teach_plan_ex")
public class TeachPlanEx extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    
	private String acadyear;
	private Integer semester;
	private String primaryTableId;//授课计划表id或者是课程表id
	private String teacherId;
	private String unitId;
	private String type;//1:授课计划,2:课程表

	@Override
	public String fetchCacheEntitName() {
		return "teachPlanEx";
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public String getPrimaryTableId() {
		return primaryTableId;
	}

	public void setPrimaryTableId(String primaryTableId) {
		this.primaryTableId = primaryTableId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
