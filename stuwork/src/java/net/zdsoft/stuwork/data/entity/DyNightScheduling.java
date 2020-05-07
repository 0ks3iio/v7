package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2017年11月30日下午2:41:12
 * 晚自习排班表
 */
@Entity
@Table(name="dy_night_scheduling")
public class DyNightScheduling extends BaseEntity<String>{
    
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String teacherId;
	private String classId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date nightTime;
	
	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "dyNightScheduling";
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Date getNightTime() {
		return nightTime;
	}

	public void setNightTime(Date nightTime) {
		this.nightTime = nightTime;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
    
	
	
}
