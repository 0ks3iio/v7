package net.zdsoft.activity.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="famdear_threeInTwo_report")
public class FamDearThreeInTwoReport extends BaseEntity<String>{
	
	private String createUserId;
	private Date createTime;
	private String unitId;
	private Date startTime;
	private Date endTime;
	private String title;
	private String type;
	private String objStu;
	private String content;
	private String existProblem;
	private String soluteProcess;
	private String resultDetail;
	private String isDelete;
	
	@Transient
	private String stuNames;
	@Transient
	private String typeStr;
	@Transient
	private String titleStr;
	@Transient
	private String deptName;
	@Transient
	private String teaName;
	
	@Transient
	private String[] typeArray;
	@Transient
	private String[] stuArray;

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getObjStu() {
		return objStu;
	}

	public void setObjStu(String objStu) {
		this.objStu = objStu;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExistProblem() {
		return existProblem;
	}

	public void setExistProblem(String existProblem) {
		this.existProblem = existProblem;
	}

	public String getSoluteProcess() {
		return soluteProcess;
	}

	public void setSoluteProcess(String soluteProcess) {
		this.soluteProcess = soluteProcess;
	}

	public String getResultDetail() {
		return resultDetail;
	}

	public void setResultDetail(String resultDetail) {
		this.resultDetail = resultDetail;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String[] getTypeArray() {
		return typeArray;
	}

	public void setTypeArray(String[] typeArray) {
		this.typeArray = typeArray;
	}

	public String[] getStuArray() {
		return stuArray;
	}

	public void setStuArray(String[] stuArray) {
		this.stuArray = stuArray;
	}

	public String getStuNames() {
		return stuNames;
	}

	public void setStuNames(String stuNames) {
		this.stuNames = stuNames;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getTitleStr() {
		return titleStr;
	}

	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTeaName() {
		return teaName;
	}

	public void setTeaName(String teaName) {
		this.teaName = teaName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "famDearThreeInTwoReport";
	}

}
