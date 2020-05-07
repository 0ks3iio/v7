package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * studevelop_month_performance
 * @author 
 * 
 */
@Entity
@Table(name="studevelop_month_performance")
public class StudevelopMonthPerformance extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String unitId;
	/**
	 * 
	 */
	private String acadyear;
	/**
	 * 
	 */
	private Integer semester;
	/**
	 * 
	 */
	private String studentId;
	/**
	 * 
	 */
	private Integer performMonth;
	/**
	 * 
	 */
	private String itemId;
	/**
	 * 
	 */
	private String resultId;
	/**
	 * 
	 */
	private Date creationTime;
	/**
	 * 
	 */
	private Date modifyTime;
	@Transient
	private String classId;

	/**
	 * 设置
	 */
	public void setUnitId(String unitId){
		this.unitId = unitId;
	}
	/**
	 * 获取
	 */
	public String getUnitId(){
		return this.unitId;
	}
	/**
	 * 设置
	 */
	public void setAcadyear(String acadyear){
		this.acadyear = acadyear;
	}
	/**
	 * 获取
	 */
	public String getAcadyear(){
		return this.acadyear;
	}
	/**
	 * 设置
	 */
	public void setSemester(Integer semester){
		this.semester = semester;
	}
	/**
	 * 获取
	 */
	public Integer getSemester(){
		return this.semester;
	}
	/**
	 * 设置
	 */
	public void setStudentId(String studentId){
		this.studentId = studentId;
	}
	/**
	 * 获取
	 */
	public String getStudentId(){
		return this.studentId;
	}
	/**
	 * 设置
	 */
	public void setPerformMonth(Integer performMouth){
		this.performMonth = performMouth;
	}
	/**
	 * 获取
	 */
	public Integer getPerformMonth(){
		return this.performMonth;
	}
	/**
	 * 设置
	 */
	public void setItemId(String itemId){
		this.itemId = itemId;
	}
	/**
	 * 获取
	 */
	public String getItemId(){
		return this.itemId;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	/**
	 * 设置
	 */
	public void setCreationTime(Date creationTime){
		this.creationTime = creationTime;
	}
	/**
	 * 获取
	 */
	public Date getCreationTime(){
		return this.creationTime;
	}
	/**
	 * 设置
	 */
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取
	 */
	public Date getModifyTime(){
		return this.modifyTime;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "studevelopMonthPerformance";
	}
}