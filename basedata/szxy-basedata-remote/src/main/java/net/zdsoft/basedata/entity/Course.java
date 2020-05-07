package net.zdsoft.basedata.entity;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="base_course")
public class Course extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	
	@ColumnInfo(mcodeId = "DM-RKXD",nullable = false,displayName = "适用学段",vtype = ColumnInfo.VTYPE_SELECT)
	private String section;
	@ColumnInfo(displayName = "科目编号", nullable = false, maxLength = 20,regex="/^\\d+$/",regexTip="只能输入数字")
	private String subjectCode;
	@ColumnInfo(displayName = "科目名称",nullable = false,maxLength = 100)
	private String subjectName;
	@ColumnInfo(mcodeId = "DM-BOOLEAN",nullable = false,displayName = "是否启用",vtype = ColumnInfo.VTYPE_SELECT)
	private Integer isUsing;
	@ColumnInfo(hide = true,displayName = "单位")
	private String unitId;
	@ColumnInfo(displayName = "是否删除")
	private Integer isDeleted;
	@ColumnInfo(displayName = "是否系统内")
	private Integer eventSource;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	@ColumnInfo(displayName = "简称",maxLength = 150,readonly=true)
	private String shortName;
	@ColumnInfo(displayName = "排序号",nullable = false,vtype = ColumnInfo.VTYPE_INT)
	private Integer orderId;
	@ColumnInfo(hide = true,displayName = "单位类型：1教育局2：学校")
	private String subjectType;
	private Integer fullMark;
	/**
	 * 总课时
	 */
	private Integer totalHours;
	/**
	 * 相对其他表中的subjectType 变量值为：BaseConstants。subjectType
	 */
	private String type;
	@ColumnInfo(displayName = "课程类型")
	private String courseTypeId;  //当课程为选修课时要从微代码中获取 mcodeId= DM-HWXXKLX
//	@Transient
	@ColumnInfo(displayName = "学分")
	private Integer initCredit;
//	@Transient
	@ColumnInfo(displayName = "默认合格分数")
	private Integer initPassMark;	
	private String bgColor;// 页面用背景色边框色，以,隔开，保存内容为：背景色,边框色
	/**********************辅助字段**********************/
	@Transient
	private String unitName;
	@Transient
	private String isUsingName;
	@Transient
	private String courseTypeName;
	@Transient
	private String sectionName;
	
	public static final Integer FALSE_0 = 0;
	public static final Integer TRUE_1 = 1;

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getFullMark() {
		return fullMark;
	}

	public void setFullMark(Integer fullMark) {
		this.fullMark = fullMark;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	@Override
	public String fetchCacheEntitName() {
		return "course";
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getIsUsingName() {
		return isUsingName;
	}

	public void setIsUsingName(String isUsingName) {
		this.isUsingName = isUsingName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getCourseTypeId() {
		return courseTypeId;
	}

	public void setCourseTypeId(String courseTypeId) {
		this.courseTypeId = courseTypeId;
	}

	public Integer getInitCredit() {
		return initCredit;
	}

	public void setInitCredit(Integer initCredit) {
		this.initCredit = initCredit;
	}

	public String getCourseTypeName() {
		return courseTypeName;
	}

	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Integer getInitPassMark() {
		return initPassMark;
	}

	public void setInitPassMark(Integer initPassMark) {
		this.initPassMark = initPassMark;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public Integer getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(Integer totalHours) {
		this.totalHours = totalHours;
	}
}
