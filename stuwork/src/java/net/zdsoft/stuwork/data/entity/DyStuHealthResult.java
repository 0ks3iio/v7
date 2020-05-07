package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;


@Entity
@Table(name ="dy_stu_health_result")
public class DyStuHealthResult extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitId;
	
	private String acadyear;
	
	private String semester;
	
	private String studentId;
	
	private String sex;
	
	private String className;
	
	private int orderId;
	
	private String itemId;//指标id
	
	private String itemName;
	
	private String itemUnit;
	
	private String itemResult;//指标结果
	
	@Transient
    private String studentName;
	@Transient
	private String schoolYearTeam;  //学年学期
	
	
	
	public String getSchoolYearTeam() {
		return schoolYearTeam;
	}


	public void setSchoolYearTeam(String schoolYearTeam) {
		this.schoolYearTeam = schoolYearTeam;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
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


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemUnit() {
		return itemUnit;
	}


	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}


	public String getItemResult() {
		return itemResult;
	}


	public void setItemResult(String itemResult) {
		this.itemResult = itemResult;
	}


	@Override
	public String fetchCacheEntitName() {
		return "getHealthResult";
	}

}
