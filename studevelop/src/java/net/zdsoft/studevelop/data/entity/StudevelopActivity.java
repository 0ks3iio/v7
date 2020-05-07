package net.zdsoft.studevelop.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * studevelop_activity
 * @author 
 * 
 */
@Entity
@Table(name="studevelop_activity")
public class StudevelopActivity extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String unitId;
	private String acadyear;
	private Integer semester;
	private String rangeId;
	/**
	 * 1学校2班级3个人
	 */
	private String rangeType;
	private String actType;
	private String actTheme;
	private String actRemark;
	@Column(updatable=false)
	private Date creationTime;
	private Date modifyTime;
	
	@Transient
	private boolean saveWithFile;
	
	@Transient
	private String coverAttId;
	@Transient
	private boolean newEn = true;
	@Transient
	private boolean hasUpload = false;
	@Transient
	private String delAttIds;
	//关联的图片
	@Transient
	private List<StudevelopAttachment> atts;
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
	public void setRangeId(String rangeId){
		this.rangeId = rangeId;
	}
	/**
	 * 获取
	 */
	public String getRangeId(){
		return this.rangeId;
	}
	/**
	 * 设置1学校2班级3个人
	 */
	public void setRangeType(String rangeType){
		this.rangeType = rangeType;
	}
	/**
	 * 获取1学校2班级3个人
	 */
	public String getRangeType(){
		return this.rangeType;
	}
	/**
	 * 设置
	 */
	public void setActType(String actType){
		this.actType = actType;
	}
	/**
	 * 获取
	 */
	public String getActType(){
		return this.actType;
	}
	/**
	 * 设置
	 */
	public void setActTheme(String actTheme){
		this.actTheme = actTheme;
	}
	/**
	 * 获取
	 */
	public String getActTheme(){
		return this.actTheme;
	}
	/**
	 * 设置
	 */
	public void setActRemark(String actRemark){
		this.actRemark = actRemark;
	}
	/**
	 * 获取
	 */
	public String getActRemark(){
		return this.actRemark;
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
		return "studevelopActivity";
	}
	public boolean isSaveWithFile() {
		return saveWithFile;
	}
	public void setSaveWithFile(boolean saveWithFile) {
		this.saveWithFile = saveWithFile;
	}
	public String getCoverAttId() {
		return coverAttId;
	}
	public void setCoverAttId(String coverAttId) {
		this.coverAttId = coverAttId;
	}
	public boolean isNewEn() {
		return newEn;
	}
	public void setNewEn(boolean newEn) {
		this.newEn = newEn;
	}
	public boolean isHasUpload() {
		return hasUpload;
	}
	public void setHasUpload(boolean hasUpload) {
		this.hasUpload = hasUpload;
	}
	public String getDelAttIds() {
		return delAttIds;
	}
	public void setDelAttIds(String delAttIds) {
		this.delAttIds = delAttIds;
	}

	public List<StudevelopAttachment> getAtts() {
		return atts;
	}

	public void setAtts(List<StudevelopAttachment> atts) {
		this.atts = atts;
	}
}