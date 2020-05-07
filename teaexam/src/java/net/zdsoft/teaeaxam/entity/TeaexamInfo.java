package net.zdsoft.teaeaxam.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teaexam_info")
public class TeaexamInfo extends BaseEntity<String>{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3770995228727293915L;
	private String unitId;
	private String examCode;
	private String examName;
	private int state;
	private Date registerBegin;
	private Date registerEnd;
	private String description;
	private String schoolIds;
	private Date examStart;
	private Date examEnd;
	private Date creationTime;
	private Date modifyTime;
	private int infoYear;
	private int infoType;// 0考试，1培训
	private String trainItems;// 培训项目
	@Transient
    private String subNames;
	@Transient
	private String status;

	@Override
	public String fetchCacheEntitName() {
		return "teaexamInfo";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getRegisterBegin() {
		return registerBegin;
	}

	public void setRegisterBegin(Date registerBegin) {
		this.registerBegin = registerBegin;
	}

	public Date getRegisterEnd() {
		return registerEnd;
	}

	public void setRegisterEnd(Date registerEnd) {
		this.registerEnd = registerEnd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchoolIds() {
		return schoolIds;
	}

	public void setSchoolIds(String schoolIds) {
		this.schoolIds = schoolIds;
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

	public String getSubNames() {
		return subNames;
	}

	public void setSubNames(String subNames) {
		this.subNames = subNames;
	}

	public Date getExamStart() {
		return examStart;
	}

	public void setExamStart(Date examStart) {
		this.examStart = examStart;
	}

	public Date getExamEnd() {
		return examEnd;
	}

	public void setExamEnd(Date examEnd) {
		this.examEnd = examEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getInfoYear() {
		return infoYear;
	}

	public void setInfoYear(int infoYear) {
		this.infoYear = infoYear;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getTrainItems() {
		return trainItems;
	}

	public void setTrainItems(String trainItems) {
		this.trainItems = trainItems;
	}
	
}
