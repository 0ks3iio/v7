package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="dy_stu_punishment")
public class DyStuPunishment extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String acadyear;
	private String semester;
	private String studentId;
	private String punishTypeId;
	private String punishName;
	private Date punishDate; 
	private String punishContent;
    private float score;
    @Transient
    private String studentName;
    @Transient
    private String studentCode;
    @Transient
    private String optionName;
    @Transient
    private String className;
    
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentCode() {
		return studentCode;
	}



	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}



	public String getOptionName() {
		return optionName;
	}



	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}



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



	public String getSemester() {
		return semester;
	}



	public void setSemester(String semester) {
		this.semester = semester;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getPunishTypeId() {
		return punishTypeId;
	}



	public void setPunishTypeId(String punishTypeId) {
		this.punishTypeId = punishTypeId;
	}



	public String getPunishName() {
		return punishName;
	}



	public void setPunishName(String punishName) {
		this.punishName = punishName;
	}



	public Date getPunishDate() {
		return punishDate;
	}



	public void setPunishDate(Date punishDate) {
		this.punishDate = punishDate;
	}



	public String getPunishContent() {
		return punishContent;
	}



	public void setPunishContent(String punishContent) {
		this.punishContent = punishContent;
	}



	public float getScore() {
		return score;
	}



	public void setScore(float score) {
		this.score = score;
	}



	@Override
	public String fetchCacheEntitName() {
		return "dyStuPunishment";
	}

}
