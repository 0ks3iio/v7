package net.zdsoft.studevelop.data.entity;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
/**
 * studevelop_attachment
 * @author 
 * 
 */
@Entity
@Table(name = "studevelop_attachment")
public class StudevelopAttachment extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String objecttype;
	/**
	 * 
	 */
	private String filename;
	/**
	 * 
	 */
	private long filesize;
	/**
	 * 
	 */
	private String contenttype;
	/**
	 * 
	 */
	private String description;
	/**
	 * 
	 */
	private Integer creationdate;
	/**
	 * 
	 */
	private Integer modificationdate;
	/**
	 * 
	 */
	private String objId;
	/**
	 * 
	 */
	private String unitId;
	/**
	 * 
	 */
	private String dirId;
	/**
	 * 
	 */
	private String filePath;
	/**
	 * 
	 */
	private String acadyear;
	/**
	 * 
	 */
	private String semester;
	/**
	 * 
	 */
	@Column(updatable=false)
	private Date creationTime;
	/**
	 * 
	 */
	private Date modifyTime;
	/**
	 * 
	 */
	private String extName;
	private int isDeleted=0;
	
	@Transient
	private String originFilePath;
	@Transient
	public File originFile;
	@Transient
	public File smallFile;
	@Transient
	public String smallFullPath;// fullpath

	public static enum StudevelopAttachmentObjType{
		STUDEVELOP_STU_PIC(StuDevelopConstant.OBJTYPE_STU_PIC, 130, 130), 
		FALMILY_PIC(StuDevelopConstant.OBJTYPE_FALMILY_PIC, 500,400), 
		SCHOOL_INFO(StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_INFO, 750, 650), 
		MASTER_PIC(StuDevelopConstant.OBJTYPE_MASTER_PIC,130,130),
		SCHOOL_ACTIVITY(StuDevelopConstant.ACTIVITY_TYPE_SCHOOL_ACTIVITY,600,500), 
		CLASS_ACTIVITY(StuDevelopConstant.ACTIVITY_TYPE_CLASS_ACTIVITY,600,500),
		CLASS_HONOR(StuDevelopConstant.ACTIVITY_TYPE_CLASS_HONOR,600,500),
		THEME_ACTIVITY(StuDevelopConstant.ACTIVITY_TYPE_THEME_ACTIVITY,600,500), 
		OUT_SCHOOL_PERFORMANCE(StuDevelopConstant.ACTIVITY_TYPE_OUT_SCHOOL_PERFORMANCE,600,500), 
		KIDS_HOLIDAY(StuDevelopConstant.ACTIVITY_TYPE_KIDS_HOLIDAY,600,500), 
		STUDEV_HONOR(StuDevelopConstant.OBJTYPE_STUDEV_HONOR,300,200);
		
		StudevelopAttachmentObjType(String value, int wight, int height) {
			this.value = value;
			this.wight = wight;
			this.height = height;
		}
		
		private String value;
		private int wight;
		private int height;

		public String getValue() {
			return value;
		}

		public int getWight() {
			return wight;
		}

		public int getHeight() {
			return height;
		}
	
		public static StudevelopAttachmentObjType getType(String value){
			for(StudevelopAttachmentObjType ent : values()){
				if(ent.getValue().equals(value))
					return ent;
			}
			return null;
		}
	}
	
	
	/**
	 * 设置
	 */
	public void setObjecttype(String objecttype){
		this.objecttype = objecttype;
	}
	/**
	 * 获取
	 */
	public String getObjecttype(){
		return this.objecttype;
	}
	/**
	 * 设置
	 */
	public void setFilename(String filename){
		this.filename = filename;
	}
	/**
	 * 获取
	 */
	public String getFilename(){
		return this.filename;
	}
	/**
	 * 设置
	 */
	public void setFilesize(long filesize){
		this.filesize = filesize;
	}
	/**
	 * 获取
	 */
	public long getFilesize(){
		return this.filesize;
	}
	/**
	 * 设置
	 */
	public void setContenttype(String contenttype){
		this.contenttype = contenttype;
	}
	/**
	 * 获取
	 */
	public String getContenttype(){
		return this.contenttype;
	}
	/**
	 * 设置
	 */
	public void setDescription(String description){
		this.description = description;
	}
	/**
	 * 获取
	 */
	public String getDescription(){
		return this.description;
	}
	/**
	 * 设置
	 */
	public void setCreationdate(Integer creationdate){
		this.creationdate = creationdate;
	}
	/**
	 * 获取
	 */
	public Integer getCreationdate(){
		return this.creationdate;
	}
	/**
	 * 设置
	 */
	public void setModificationdate(Integer modificationdate){
		this.modificationdate = modificationdate;
	}
	/**
	 * 获取
	 */
	public Integer getModificationdate(){
		return this.modificationdate;
	}
	/**
	 * 设置
	 */
	public void setObjId(String objId){
		this.objId = objId;
	}
	/**
	 * 获取
	 */
	public String getObjId(){
		return this.objId;
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
	public void setDirId(String dirId){
		this.dirId = dirId;
	}
	/**
	 * 获取
	 */
	public String getDirId(){
		return this.dirId;
	}
	/**
	 * 设置
	 */
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	/**
	 * 获取
	 */
	public String getFilePath(){
		return this.filePath;
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
	/**
	 * 设置
	 */
	public void setExtName(String extName){
		this.extName = extName;
	}
	/**
	 * 获取
	 */
	public String getExtName(){
		return this.extName;
	}
	@Override
	public String fetchCacheEntitName() {
		return "studevelopAttachment";
	}
	public File getOriginFile() {
		return originFile;
	}
	public void setOriginFile(File originFile) {
		this.originFile = originFile;
	}
	public File getSmallFile() {
		return smallFile;
	}
	public void setSmallFile(File smallFile) {
		this.smallFile = smallFile;
	}
	public String getOriginFilePath() {
		return originFilePath;
	}
	public void setOriginFilePath(String originFilePath) {
		this.originFilePath = originFilePath;
	}
	public String getSmallFullPath() {
		return smallFullPath;
	}
	public void setSmallFullPath(String smallFullPath) {
		this.smallFullPath = smallFullPath;
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
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}