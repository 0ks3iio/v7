package net.zdsoft.studevelop.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * studevelop_honor
 * @author 
 * 
 */
@Entity
@Table(name="studevelop_honor")
public class StudevelopHonor extends BaseEntity<String>{
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
	private String classId;

	/**
	 * 
	 */
	private Date creationTime;
	/**
	 * 
	 */
	private Date modifyTime;
	@Transient
	private List<StudevelopAttachment> atts;

	public List<StudevelopAttachment> getAtts() {
		return atts;
	}

	public void setAtts(List<StudevelopAttachment> atts) {
		this.atts = atts;
	}

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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
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
	@Override
	public String fetchCacheEntitName() {
		return "studevelopHonor";
	}
}